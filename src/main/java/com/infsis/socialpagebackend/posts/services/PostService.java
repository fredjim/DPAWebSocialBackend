package com.infsis.socialpagebackend.posts.services;

import com.infsis.socialpagebackend.authentication.models.Users;
import com.infsis.socialpagebackend.authentication.repositories.UserRepository;
import com.infsis.socialpagebackend.comments.dtos.CommentCounterDTO;
import com.infsis.socialpagebackend.comments.models.Comment;
import com.infsis.socialpagebackend.comments.repositories.CommentRepository;
import com.infsis.socialpagebackend.configuration.AppUrlProperties;
import com.infsis.socialpagebackend.enums.CommentState;
import com.infsis.socialpagebackend.exceptions.NotFoundException;
import com.infsis.socialpagebackend.institutions.models.Institution;
import com.infsis.socialpagebackend.institutions.repositories.InstitutionRepository;
import com.infsis.socialpagebackend.enums.FileCategory;
import com.infsis.socialpagebackend.medias.services.FileStorageService;
import com.infsis.socialpagebackend.multitenant.TenantContext;
import com.infsis.socialpagebackend.posts.clients.FacebookApiClient;
import com.infsis.socialpagebackend.social_networks.services.InstitutionFacebookConfigService;
import com.infsis.socialpagebackend.posts.dtos.*;
import com.infsis.socialpagebackend.posts.mappers.MediaMapper;
import com.infsis.socialpagebackend.posts.mappers.PostMapper;
import com.infsis.socialpagebackend.posts.models.*;
import com.infsis.socialpagebackend.posts.repositories.*;
import com.infsis.socialpagebackend.reactions.models.EmojiType;
import com.infsis.socialpagebackend.reactions.models.PostReaction;
import com.infsis.socialpagebackend.reactions.repositories.EmojiTypeRepository;
import com.infsis.socialpagebackend.reactions.repositories.PostReactionRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
public class PostService {

    private static final String ANONYMOUS_USER = "anonymousUser";

    private static final String VIDEOS_DIRECTORY    = System.getProperty("user.dir") + "/storage/institution/posts/videos/";
    private static final String DOCUMENTS_DIRECTORY = System.getProperty("user.dir") + "/storage/institution/posts/documents/";
    private static final String PHOTOS_DIRECTORY    = System.getProperty("user.dir") + "/storage/institution/posts/photos/";

    public enum GroupStatus {
        CREATED, SAVED, REMOVED, UPDATED
    }

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private MediaMapper mediaMapper;

    @Autowired
    private AppUrlProperties appUrlProperties;

    @Autowired
    private MediaRepository mediaRepository;

    @Autowired
    private TextRepository textRepository;

    @Autowired
    private ContentRepository contentRepository;

    @Autowired
    private CommentConfigRepository commentConfigRepository;

    @Autowired
    private InstitutionRepository institutionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostReactionRepository postReactionRepository;

    @Autowired
    private EmojiTypeRepository emojiTypeRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private FacebookApiClient facebookApiClient;

    @Autowired
    private InstitutionFacebookConfigService facebookConfigService;

    @Autowired
    private FileStorageService fileStorageService;

    @PersistenceContext
    private EntityManager entityManager;

    public PostDTO getPost(String postUuid) {
        Post post = postRepository.findOneByUuid(postUuid);

        if(post == null || post.isDeleted()) {
            throw new NotFoundException("Post", postUuid);
        }
        ReactionCounterDTO reactionCounterDTO = getPostReactionCounterDTO(post);

        CommentCounterDTO commentCounterDTO = getCommentCounter(postUuid);

        return postMapper.toDTO(post, reactionCounterDTO, commentCounterDTO);
    }

    public List<PostDTO> getAllPost() {
        String tenantId = TenantContext.getCurrentTenant();
        return postRepository
                .findAllByInstitutionUuid(tenantId)
                .stream()
                .map(post -> postMapper.toDTO(post, getPostReactionCounterDTO(post), getCommentCounter(post.getUuid())))
                .collect(Collectors.toList());
    }

    public List<PostDTO> getAllByGroup(String groupUuid) {
        String tenantId = TenantContext.getCurrentTenant();
        return postRepository
                .findAllByInstitutionUuid(tenantId)
                .stream()
                .filter(post -> isFromGroup(groupUuid, post))
                .map(post -> postMapper.toDTO(post, getPostReactionCounterDTO(post), getCommentCounter(post.getUuid())))
                .collect(Collectors.toList());
    }

    public List<PostDTO> getPostsByType(String postType) {
        String tenantId = TenantContext.getCurrentTenant();
        return postRepository
                .findAllByInstitutionUuid(tenantId)
                .stream()
                .filter(post -> post.getPost_type().equals(postType))
                .map(post -> postMapper.toDTO(post, getPostReactionCounterDTO(post), getCommentCounter(post.getUuid())))
                .collect(Collectors.toList());
    }

    public PostDTO savePost(PostDTO postDTO) {

        Content content = contentRepository.save(new Content());

        CommentConfig commentConfig = commentConfigRepository.findOneByUuid(postDTO.getComment_config_id());
        Institution institution = institutionRepository.findOneByUuid(postDTO.getInstitution_id());
        //Users user = userRepository.findOneByUuid(postDTO.getUser_id());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found: ", email));

        log.info("User id :" + user.getUuid());

        Post post = new Post();
        PostDTO resDTO = new PostDTO();
        List<Media> savedMedias = new ArrayList<>();
        if (commentConfig != null && institution != null & user != null) {
            savedMedias = saveMedia(postDTO.getContent(), content);
            Text text = saveText(postDTO.getContent(), content);

            content.setText(text);
            content.setMedia(savedMedias);

            post = postMapper.getPost(postDTO, content, commentConfig, institution, user);
            postRepository.save(post);

            content.setPost(post);
            contentRepository.save(content);

            resDTO = postMapper.toDTO(post);

        }

        // Publish in Facebook
        if (Boolean.TRUE.equals(postDTO.getFb_post_enable())) {
            String institutionId = TenantContext.getCurrentTenant();
            final List<Media> mediasForFb = savedMedias;
            facebookConfigService.getActiveConfig(institutionId).ifPresentOrElse(
                config -> publishToFacebook(
                        postDTO, mediasForFb, config.getPageId(),
                        facebookConfigService.decryptToken(config)),
                () -> log.warn("Post {} marcado para Facebook pero la institución {} no tiene configuración activa.",
                    postDTO.getUuid(), institutionId)
            );
        }

        return resDTO;
    }

    public PostDTO updateePost(String postUuid, PostDTO postDTO) {

        Post post = postRepository.findOneByUuid(postUuid);

        if(post == null || post.isDeleted()) {
            throw new NotFoundException("Post", postUuid);
        }

        return savePost(postDTO);

    }

    @Transactional
    public PostDTO deletePost(String postUuid) {
        Post post = postRepository.findOneByUuid(postUuid);
        if (post == null || post.isDeleted()) {
            throw new NotFoundException("Post", postUuid);
        }

        PostDTO dto = postMapper.toDTO(post);
        Content content = post.getContent();
        Integer postId = post.getId();

        // 1. Delete physical files + UploadedFile records
        if (content != null && content.getMedia() != null) {
            content.getMedia().forEach(media -> {
                if (media.getUploadedFile() != null) {
                    try {
                        fileStorageService.deleteFileOnly(
                                media.getUploadedFile().getUuid(),
                                resolveDirectory(media.getUploadedFile().getCategory()));
                    } catch (Exception e) {
                        log.warn("No se pudo eliminar archivo uuid={}: {}",
                                media.getUploadedFile().getUuid(), e.getMessage());
                    }
                }
            });
        }

        // 2. Detach Post from the persistence context.
        //    Without this, when Content is deleted Hibernate tries to set post.content_id = NULL
        //    before the DELETE, which violates the NOT NULL constraint.
        entityManager.detach(post);

        // 3. Delete Content — orphanRemoval cascades Media deletion; CascadeType.ALL cascades Text
        if (content != null) {
            contentRepository.delete(content);
        }

        // 4. Delete Post row via JPQL bulk delete (post is detached, bypasses entity lifecycle)
        entityManager.createQuery("DELETE FROM Post p WHERE p.id = :id")
                .setParameter("id", postId)
                .executeUpdate();

        return dto;
    }
    private void publishToFacebook(PostDTO postDTO, List<Media> savedMedias,
                                   String pageId, String accessToken) {
        List<Media> images = savedMedias.stream()
                .filter(m -> m.getUploadedFile() != null &&
                             m.getUploadedFile().getCategory() == FileCategory.IMAGE)
                .collect(Collectors.toList());

        List<Media> videos = savedMedias.stream()
                .filter(m -> m.getUploadedFile() != null &&
                             m.getUploadedFile().getCategory() == FileCategory.VIDEO)
                .collect(Collectors.toList());

        List<String> documentUrls = savedMedias.stream()
                .filter(m -> m.getUploadedFile() != null &&
                             m.getUploadedFile().getCategory() == FileCategory.DOCUMENT)
                .map(m -> appUrlProperties.buildResourceUrl(m.getUploadedFile().getUrlResource()))
                .collect(Collectors.toList());

        if (!videos.isEmpty()) {
            if (videos.size() > 1) {
                log.warn("Facebook solo soporta un video por post, se publicará solo el primero.");
            }
            if (!images.isEmpty()) {
                log.warn("Post contiene imágenes y video; Facebook no soporta medios mixtos, se publicará solo el video.");
            }
            Media video = videos.get(0);
            Path videoPath = Paths.get(VIDEOS_DIRECTORY, video.getUploadedFile().getUuid());
            String description = appendDocumentLinks(postDTO.getContent().getText(), documentUrls);
            facebookApiClient.postVideo(videoPath, video.getUploadedFile().getMimeType(),
                    video.getUploadedFile().getName(), description, pageId, accessToken);
            return;
        }

        if (!images.isEmpty()) {
            uploadImagesToFacebook(postDTO.getContent().getMedia(), savedMedias, pageId, accessToken);
            // link y attached_media no se pueden combinar: se añaden URLs de docs al mensaje
            String message = appendDocumentLinks(postDTO.getContent().getText(), documentUrls);
            facebookApiClient.postPublication(postDTO, message, null, pageId, accessToken);
            return;
        }

        if (!documentUrls.isEmpty()) {
            // Primer doc como link preview; los extra van en el mensaje
            String link = documentUrls.get(0);
            List<String> extraDocs = documentUrls.subList(1, documentUrls.size());
            String message = appendDocumentLinks(postDTO.getContent().getText(), extraDocs);
            facebookApiClient.postPublication(postDTO, message, link, pageId, accessToken);
            return;
        }

        facebookApiClient.postPublication(postDTO, pageId, accessToken);
    }

    private String appendDocumentLinks(String text, List<String> urls) {
        if (urls.isEmpty()) return text != null ? text : "";
        StringBuilder sb = new StringBuilder(text != null ? text : "");
        for (String url : urls) {
            sb.append("\n").append(url);
        }
        return sb.toString();
    }

    private void uploadImagesToFacebook(List<MediaDTO> requestMedias, List<Media> savedMedias,
                                        String pageId, String accessToken) {
        List<Path> imagePaths = new ArrayList<>();
        List<String> mimeTypes = new ArrayList<>();
        List<String> originalFilenames = new ArrayList<>();
        List<MediaDTO> imageDTOs = new ArrayList<>();

        for (MediaDTO dto : requestMedias) {
            if (dto.getUploaded_file_uuid() == null) continue;
            savedMedias.stream()
                .filter(m -> m.getUploadedFile() != null &&
                             m.getUploadedFile().getUuid().equals(dto.getUploaded_file_uuid()))
                .findFirst()
                .ifPresent(media -> {
                    FileCategory category = media.getUploadedFile().getCategory();
                    if (category == FileCategory.IMAGE) {
                        imagePaths.add(Paths.get(PHOTOS_DIRECTORY, media.getUploadedFile().getUuid()));
                        mimeTypes.add(media.getUploadedFile().getMimeType());
                        originalFilenames.add(media.getUploadedFile().getName());
                        imageDTOs.add(dto);
                    } else if (category == FileCategory.VIDEO) {
                        log.warn("Subida de video a Facebook no soportada aún, se omite uuid={}.",
                                dto.getUploaded_file_uuid());
                    }
                });
        }

        if (imagePaths.isEmpty()) return;

        List<String> fbPhotoIds = facebookApiClient.postLinkImages(imagePaths, mimeTypes, originalFilenames, pageId, accessToken);
        for (int i = 0; i < imageDTOs.size() && i < fbPhotoIds.size(); i++) {
            imageDTOs.get(i).setFb_media_id(fbPhotoIds.get(i));
        }
    }

    private List<Media> saveMedia(ContentDTO contentDTO, Content content){
        List<Media> medias = new ArrayList<>();
        if(contentDTO.getMedia() != null ) {
            medias = contentDTO.getMedia()
                    .stream()
                    .map(media -> mediaMapper.getMedia(media, content))
                    .collect(Collectors.toList());
            medias.forEach(mediaRepository::save);
        }
        return medias;
    }

    private List<Media> saveMedia(List<MediaDTO> mediaDTOS, Content content) {
        List<Media> medias = new ArrayList<>();
        if (mediaDTOS != null) {
            medias = mediaDTOS.stream()
                    .map(dto -> mediaMapper.getMedia(dto, content))
                    .collect(Collectors.toList());
            medias.forEach(mediaRepository::save);
        }
        return medias;
    }

    private String resolveDirectory(FileCategory category) {
        if (category == null) return PHOTOS_DIRECTORY;
        return switch (category) {
            case VIDEO    -> VIDEOS_DIRECTORY;
            case DOCUMENT -> DOCUMENTS_DIRECTORY;
            default       -> PHOTOS_DIRECTORY;
        };
    }

    private Text saveText(ContentDTO contentDTO, Content content) {
        Text text = new Text();
        if(contentDTO.getText() != null) {
            text.setText(contentDTO.getText());
            text.setContent(content);
            textRepository.save(text);
        }
        return text;
    }

    private ReactionCounterDTO getPostReactionCounterDTO(Post post) {

        ReactionCounterDTO reactionCounterDTO = new ReactionCounterDTO();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Users user = new Users();
        if (email != ANONYMOUS_USER) {
            user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new NotFoundException("User not found: ", email));
        }
        List<PostReaction> postReactions = postReactionRepository.findByPostId(post.getUuid());
        List<EmojiType> emojiTypes = emojiTypeRepository.findAll();
        //Users user = userRepository.findOneByUuid(postDTO.getUser_id());
        List<Users> postUsers =
                postReactions
                        .stream()
                        .map(postReaction -> userRepository.findOneByUuid(postReaction.getUsers().getUuid()))
                        .collect(Collectors.toList());

        List<ReactionUserDTO> reactionUsers =
                postUsers
                        .stream()
                        .map(postUser -> getReactionUserDTO(postUser, postReactions))
                        .collect(Collectors.toList());

        List<ReactionItemDTO> reactionItemDTOList = new ArrayList<>();
        Integer totalPostReactions = postReactions.size();

        for (EmojiType emojiType : emojiTypes) {
            Long reactionCounter =
                    postReactions
                            .stream()
                            .filter(postReaction -> postReaction.getEmoji_type().getEmoji_name().equals(emojiType.getEmoji_name()))
                            .count();

            ReactionItemDTO reactionItemDTO = new ReactionItemDTO();
            reactionItemDTO.setEmoji_type(emojiType.getEmoji_name());
            reactionItemDTO.setAmount(reactionCounter.intValue());

            reactionItemDTOList.add(reactionItemDTO);
        }

        String currentUserId = email != ANONYMOUS_USER ? user.getUuid() : "";

        Optional<PostReaction> optionalPostReaction = Optional.empty();
        if (currentUserId != "") {
            optionalPostReaction =
                    postReactions
                            .stream()
                            .filter(postReaction -> postReaction.getUsers().getUuid().equals(currentUserId))
                            .findFirst();
        }

        String currentUserEmojiReaction = "";
        if(optionalPostReaction.isPresent()) {
            PostReaction currentPostReaction = optionalPostReaction.get();
            currentUserEmojiReaction = currentPostReaction.getEmoji_type().getEmoji_name();
        }

        reactionCounterDTO.setMy_reaction_emoji(currentUserEmojiReaction);
        reactionCounterDTO.setTotal_reactions(totalPostReactions);
        reactionCounterDTO.setReactions_by_type(totalPostReactions != 0 ? reactionItemDTOList : new ArrayList<>());
        reactionCounterDTO.setReactions_by_user(reactionUsers);

        return reactionCounterDTO;
    }

    private ReactionUserDTO getReactionUserDTO(Users user, List<PostReaction> postReactions) {
        ReactionUserDTO reactionUserDTO = new ReactionUserDTO();

        Optional<PostReaction> postUserReaction =
                postReactions
                        .stream()
                        .filter(postReaction -> postReaction.getUsers().getUuid().equals(user.getUuid()))
                        .findFirst();

        if (postUserReaction.isPresent()) {
            reactionUserDTO.setUser_name(user.getName() + " " + user.getLastName());
            reactionUserDTO.setUser_photo(user.getPhoto_profile_path());
            reactionUserDTO.setUser_reaction(postUserReaction.get().getEmoji_type().getEmoji_name());
        }

        return reactionUserDTO;
    }

    public CommentCounterDTO getCommentCounter(String postId) {
        List<Comment> comments = commentRepository.findByPostId(postId);
        Long totalComments =
                comments
                        .stream()
                        .filter(comment -> comment.getState().equals(CommentState.VISIBLE.name()) ||
                                comment.getState().equals(CommentState.APPROVED.name()) )
                        .count();
        int totalReplies = comments.stream().mapToInt(comment -> comment.getReplies().size()).sum();

        CommentCounterDTO commentCounterDTO = new CommentCounterDTO();
        commentCounterDTO.setTotalComments(totalComments.intValue());
        commentCounterDTO.setTotalReplies(totalReplies);
        commentCounterDTO.setTotalCommentsAndReplies(totalComments.intValue() + totalReplies);

        return commentCounterDTO;
    }

    @Transactional
    public PostDTO updatePost(String postUuid, PostDTO postDTO) {
        Post existingPost = postRepository.findOneByUuid(postUuid);
        if (existingPost == null || existingPost.isDeleted()) {
            throw new NotFoundException("Post", postUuid);
        }

        if (postDTO.getContent() != null) {
            Content content = existingPost.getContent();

            if (postDTO.getContent().getMedia() != null) {
                Set<String> uuidsConservados = postDTO.getContent().getMedia().stream()
                        .map(MediaDTO::getUuid)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toSet());

                if (content.getMedia() != null) {
                    List<Media> toDelete = content.getMedia().stream()
                            .filter(m -> !uuidsConservados.contains(m.getUuid()))
                            .collect(Collectors.toList());

                    toDelete.forEach(media -> {
                        if (media.getUploadedFile() != null) {
                            try {
                                fileStorageService.deleteFileOnly(
                                        media.getUploadedFile().getUuid(),
                                        resolveDirectory(media.getUploadedFile().getCategory()));
                            } catch (Exception e) {
                                log.warn("No se pudo eliminar archivo uuid={}: {}",
                                        media.getUploadedFile().getUuid(), e.getMessage());
                            }
                        }
                    });

                    // Remover de la colección — orphanRemoval = true elimina las filas en BD
                    // al llamar contentRepository.save(content). NO llamar mediaRepository.delete()
                    // aquí porque dejaría las entidades en estado REMOVED dentro de la colección,
                    // causando "deleted instance passed to merge" cuando el save hace cascade ALL.
                    content.getMedia().removeAll(toDelete);
                }

                List<MediaDTO> nuevas = postDTO.getContent().getMedia().stream()
                        .filter(m -> m.getUuid() == null)
                        .collect(Collectors.toList());
                saveMedia(nuevas, content);
            }

            if (postDTO.getContent().getText() != null) {
                Text text = content.getText();
                if (text == null) {
                    saveText(postDTO.getContent(), content);
                } else {
                    text.setText(postDTO.getContent().getText());
                    textRepository.save(text);
                }
            }

            contentRepository.save(content);
        }

        if (postDTO.getComment_config_id() != null) {
            CommentConfig commentConfig = commentConfigRepository.findOneByUuid(postDTO.getComment_config_id());
            existingPost.setComment_conf(commentConfig);
        }

        if (postDTO.getInstitution_id() != null) {
            Institution institution = institutionRepository.findOneByUuid(postDTO.getInstitution_id());
            existingPost.setInstitution(institution);
        }

        existingPost.setPost_date(postDTO.getDate());

        return postMapper.toDTO(postRepository.save(existingPost));
    }

    /* Método para buscar publicaciones por texto  */
    public List<PostDTO> searchPosts(String text) {
        String tenantId = TenantContext.getCurrentTenant();
        return postRepository.searchPostsByTextAndTenant(text, tenantId)
                .stream()
                .map(post -> postMapper.toDTO(post))
                .collect(Collectors.toList());
    }

    public PostGroupDTO addToGroup(String postUuid, PostGroupDTO postGroupDTO) {

        Post currentPost = postRepository.findOneByUuid(postUuid);
        Group currentGroup = groupRepository.findOneByUuid(postGroupDTO.getGroup_uuid());

        if(!isFromGroup(postGroupDTO.getGroup_uuid(), currentPost)) {
            List<Group> groups;
            groups = currentPost.getGroups();
            groups.add(currentGroup);
            currentPost.setGroups(groups);

            postRepository.save(currentPost);
        }

        postGroupDTO.setPost_uuid(postUuid);
        postGroupDTO.setStatus(GroupStatus.SAVED.name());

        return postGroupDTO;
    }

    public PostGroupDTO removeFromGroup(String postUuid, PostGroupDTO postGroupDTO) {

        Post currentPost = postRepository.findOneByUuid(postUuid);
        Group currentGroup = groupRepository.findOneByUuid(postGroupDTO.getGroup_uuid());

        List<Group> groups;
        groups = currentPost.getGroups();
        groups.remove(currentGroup);
        currentPost.setGroups(groups);

        postRepository.save(currentPost);

        postGroupDTO.setPost_uuid(postUuid);
        postGroupDTO.setStatus(GroupStatus.REMOVED.name());

        return postGroupDTO;
    }

    private boolean isFromGroup(String groupUuid, Post post){

        return !post.getGroups()
                .stream()
                .filter(group -> group.getUuid().equals(groupUuid))
                .collect(Collectors.toList())
                .isEmpty();
    }

    public List<MediaItemDTO> getMediasInstitution(String institutionUuid, String type) {
        List<Post> posts = postRepository.findAll();
        List<MediaItemDTO> mediaItems = new ArrayList<>();
        for (Post post : posts) {
            if (post.getInstitution().getUuid().equals(institutionUuid)) {
                for (Media media : post.getContent().getMedia()) {
                    if (type.equalsIgnoreCase(media.getFile_type()) && media.getUploadedFile() != null) {
                        MediaItemDTO mediaItemDTO = new MediaItemDTO();
                        mediaItemDTO.setUuid_post(post.getUuid());
                        mediaItemDTO.setPath(appUrlProperties.buildResourceUrl(media.getUploadedFile().getUrlResource()));
                        mediaItems.add(mediaItemDTO);
                    }
                }
            }
        }
        return mediaItems;
    }

    public List<PostDTO> getPagedPosts(int page, int size) {
        String tenantId = TenantContext.getCurrentTenant();
        Pageable pageable = PageRequest.of(page, size, Sort.by("post_date").descending());
        Page<Post> posts = postRepository.findAllPagedByTenant(tenantId, pageable);

        return posts.stream()
                    .map(post -> postMapper.toDTO(post, getPostReactionCounterDTO(post), getCommentCounter(post.getUuid())))
                    .collect(Collectors.toList());
    }
    
}
