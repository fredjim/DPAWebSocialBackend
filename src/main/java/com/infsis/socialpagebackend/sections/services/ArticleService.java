package com.infsis.socialpagebackend.sections.services;

import com.infsis.socialpagebackend.authentication.models.Users;
import com.infsis.socialpagebackend.authentication.repositories.UserRepository;
import com.infsis.socialpagebackend.exceptions.NotFoundException;
import com.infsis.socialpagebackend.enums.FileCategory;
import com.infsis.socialpagebackend.medias.models.UploadedFile;
import com.infsis.socialpagebackend.medias.services.FileStorageService;
import com.infsis.socialpagebackend.sections.dtos.ArticleDTO;
import com.infsis.socialpagebackend.posts.dtos.MediaDTO;
import com.infsis.socialpagebackend.posts.mappers.MediaMapper;
import com.infsis.socialpagebackend.posts.models.Media;
import com.infsis.socialpagebackend.posts.repositories.MediaRepository;
import com.infsis.socialpagebackend.sections.mappers.ArticleMapper;
import com.infsis.socialpagebackend.sections.mappers.LinkMapper;
import com.infsis.socialpagebackend.sections.models.Article;
import com.infsis.socialpagebackend.sections.models.Section;
import com.infsis.socialpagebackend.sections.models.Link;
import com.infsis.socialpagebackend.sections.dtos.LinkDTO;
import com.infsis.socialpagebackend.enums.OwnerType;
import com.infsis.socialpagebackend.multitenant.TenantContext;
import com.infsis.socialpagebackend.sections.repositories.ArticleRepository;
import com.infsis.socialpagebackend.sections.repositories.LinkRepository;
import com.infsis.socialpagebackend.sections.repositories.SectionRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
public class ArticleService {

    private static final String VIDEOS_DIRECTORY    = System.getProperty("user.dir") + "/storage/institution/posts/videos/";
    private static final String DOCUMENTS_DIRECTORY = System.getProperty("user.dir") + "/storage/institution/posts/documents/";
    private static final String PHOTOS_DIRECTORY    = System.getProperty("user.dir") + "/storage/institution/posts/photos/";

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private MediaMapper mediaMapper;

    @Autowired
    private MediaRepository mediaRepository;

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LinkMapper linkMapper;

    @Autowired
    private LinkRepository linkRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @PersistenceContext
    private EntityManager entityManager;

    public ArticleDTO getArticle(String articleUuid) {
        Article article = articleRepository.findOneByUuid(articleUuid);

        if(article == null || article.isDeleted()) {
            throw new NotFoundException("Article", articleUuid);
        }

        return articleMapper.toDTO(article);
    }

    public List<ArticleDTO> getAllArticles() {
        String tenantId = TenantContext.getCurrentTenant();
        return articleRepository
                .findAllByInstitutionId(tenantId)
                .stream()
                .filter(article -> !article.isDeleted())
                .sorted((a, b) -> {
                    if (a.getCreatedDate() == null && b.getCreatedDate() == null) return 0;
                    if (a.getCreatedDate() == null) return 1;
                    if (b.getCreatedDate() == null) return -1;
                    return a.getCreatedDate().compareTo(b.getCreatedDate());
                })
                .map(article -> articleMapper.toDTO(article))
                .collect(Collectors.toList());
    }

    public List<ArticleDTO> getAllBySection(String sectionUuid, String tenantId) {
        Section section = sectionRepository.findOneByUuid(sectionUuid);
        if (section == null || section.isDeleted()) {
            throw new NotFoundException("Section", sectionUuid);
        }

        return articleRepository
                .findAllByInstitutionId(tenantId)
                .stream()
                .filter(article -> {
                    if (article == null || article.isDeleted()) return false;
                    if (article.getSection() == null || article.getSection().getUuid() == null) return false;
                    return article.getSection().getUuid().equals(sectionUuid);
                })
                .sorted((a, b) -> {
                    if (a.getCreatedDate() == null && b.getCreatedDate() == null) return 0;
                    if (a.getCreatedDate() == null) return 1;
                    if (b.getCreatedDate() == null) return -1;
                    return a.getCreatedDate().compareTo(b.getCreatedDate());
                })
                .map(article -> articleMapper.toDTO(article))
                .collect(Collectors.toList());
    }

    public ArticleDTO saveArticle(ArticleDTO articleDTO) {

        Section section = sectionRepository.findOneByUuid(articleDTO.getSection_id());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found: ", email));

        log.info("User id :" + user.getUuid());

        ArticleDTO resDTO = new ArticleDTO();
        String tenantId = TenantContext.getCurrentTenant();
        Article stub = new Article();
        stub.setInstitutionId(tenantId);
        Article article = articleRepository.save(stub);
        if (section != null && user != null) {
            List<Media> medias = saveMedia(articleDTO.getMedias(), article);

            saveLinks(articleDTO.getLinks(), article.getUuid());

            article.setUsers(user);
            article.setSection(section);
            article.setTitle(articleDTO.getTitle());
            article.setText(articleDTO.getText());
            article.setDate(articleDTO.getDate());
            article.setArticle_medias(medias);
            article.setInstitutionId(tenantId);

            articleRepository.save(article);

            resDTO = articleMapper.toDTO(article);
        }

        return resDTO;
    }

    @Transactional
    public ArticleDTO deleteArticle(String articleUuid) {
        Article article = articleRepository.findOneByUuid(articleUuid);

        if (article == null || article.isDeleted()) {
            throw new NotFoundException("Article", articleUuid);
        }

        ArticleDTO dto = articleMapper.toDTO(article);

        if (article.getArticle_medias() != null) {
            article.getArticle_medias().forEach(media -> {
                if (media.getUploadedFile() != null) {
                    try {
                        fileStorageService.deleteFileOnly(media.getUploadedFile().getUuid(), resolveDirectory(media.getUploadedFile().getCategory()));
                    } catch (Exception e) {
                        log.warn("No se pudo eliminar archivo uuid={}: {}", media.getUploadedFile().getUuid(), e.getMessage());
                    }
                }
            });
        }

        List<Link> links = linkRepository.findAllByOwnerTypeAndOwnerUuid(OwnerType.ARTICLE, articleUuid);
        if (links != null && !links.isEmpty()) {
            linkRepository.deleteAll(links);
        }

        articleRepository.delete(article);

        return dto;
    }

    private List<Media> saveMedia(List<MediaDTO> mediaDTOS, Article article) {
        List<Media> medias = new ArrayList<>();
        if (mediaDTOS != null) {
            medias = mediaDTOS
                    .stream()
                    .map(media -> mediaMapper.getMedia(media, article))
                    .collect(Collectors.toList());

            medias.forEach(media -> mediaRepository.save(media));
        }
        return medias;
    }

    private List<Link> saveLinks(List<LinkDTO> linkDTOS, String ownerUuid) {
        List<Link> links = new ArrayList<>();
        if (linkDTOS != null) {
            links = linkDTOS
                    .stream()
                    .map(link -> linkMapper.getLink(link, OwnerType.ARTICLE, ownerUuid))
                    .collect(Collectors.toList());

            links.forEach(linkRepository::save);
        }
        return links;
    }

    @Transactional
    public ArticleDTO update_Article(String articleUuid, ArticleDTO articleDTO) {
        Article foundArticle = articleRepository.findOneByUuid(articleUuid);

        if (foundArticle == null || foundArticle.isDeleted()) {
            throw new NotFoundException("Article", articleUuid);
        }

        if (articleDTO.getMedias() != null) {
            Set<String> uuidsConservados = articleDTO.getMedias().stream()
                    .map(MediaDTO::getUuid)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            Set<String> uploadedFileUuidsReutilizados = articleDTO.getMedias().stream()
                    .filter(m -> m.getUuid() == null && m.getUploaded_file_uuid() != null)
                    .map(MediaDTO::getUploaded_file_uuid)
                    .collect(Collectors.toSet());

            if (foundArticle.getArticle_medias() != null) {
                new ArrayList<>(foundArticle.getArticle_medias()).forEach(media -> {
                    if (!uuidsConservados.contains(media.getUuid())) {
                        if (media.getUploadedFile() != null && !uploadedFileUuidsReutilizados.contains(media.getUploadedFile().getUuid())) {
                            try {
                                fileStorageService.deleteFileOnly(media.getUploadedFile().getUuid(), resolveDirectory(media.getUploadedFile().getCategory()));
                            } catch (Exception e) {
                                log.warn("No se pudo eliminar archivo uuid={}: {}", media.getUploadedFile().getUuid(), e.getMessage());
                            }
                        }
                        mediaRepository.delete(media);
                    }
                });
            }

            List<MediaDTO> nuevas = articleDTO.getMedias().stream()
                    .filter(m -> m.getUuid() == null)
                    .collect(Collectors.toList());
            saveMedia(nuevas, foundArticle);
        }

        if (articleDTO.getLinks() != null) {
            List<Link> oldLinks = linkRepository.findAllByOwnerTypeAndOwnerUuid(OwnerType.ARTICLE, foundArticle.getUuid());
            if (oldLinks != null && !oldLinks.isEmpty()) {
                linkRepository.deleteAll(oldLinks);
            }
            saveLinks(articleDTO.getLinks(), foundArticle.getUuid());
        }

        if (articleDTO.getSection_id() != null) {
            Section section = sectionRepository.findOneByUuid(articleDTO.getSection_id());
            foundArticle.setSection(section);
        }

        foundArticle.setDate(articleDTO.getDate());
        foundArticle.setTitle(articleDTO.getTitle());
        foundArticle.setText(articleDTO.getText());

        articleRepository.saveAndFlush(foundArticle);
        entityManager.clear();
        return articleMapper.toDTO(articleRepository.findOneByUuid(articleUuid));
    }

    @Transactional
    public ArticleDTO updateArticle(String articleUuid, ArticleDTO articleDTO) {
        Article foundArticle = articleRepository.findOneByUuid(articleUuid);
        
        if (foundArticle == null || foundArticle.isDeleted()) {
            throw new NotFoundException("Article", articleUuid);
        }
        
        if (articleDTO.getMedias() != null) {
            Set<String> uuidsConservados = articleDTO.getMedias().stream()
                    .map(MediaDTO::getUuid)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());
            
            Set<String> uploadedFileUuidsReutilizados = articleDTO.getMedias().stream()
                    .filter(m -> m.getUuid() == null && m.getUploaded_file_uuid() != null)
                    .map(MediaDTO::getUploaded_file_uuid)
                    .collect(Collectors.toSet());
            
            if (foundArticle.getArticle_medias() != null) {
                // Primero, recolectar los archivos a eliminar
                List<Media> mediasToDelete = new ArrayList<>();
                List<UploadedFile> filesToDelete = new ArrayList<>();
                
                for (Media media : foundArticle.getArticle_medias()) {
                    if (!uuidsConservados.contains(media.getUuid())) {
                        if (media.getUploadedFile() != null && 
                            !uploadedFileUuidsReutilizados.contains(media.getUploadedFile().getUuid())) {
                            filesToDelete.add(media.getUploadedFile());
                        }
                        mediasToDelete.add(media);
                    }
                }
                
                // 1. Primero eliminar registros de Media de la BD
                if (!mediasToDelete.isEmpty()) {
                    if (foundArticle.getArticle_medias() != null) {
                        foundArticle.getArticle_medias().removeAll(mediasToDelete);
                    }
                    mediasToDelete.forEach(media -> media.setArticle(null));
                    mediaRepository.deleteAll(mediasToDelete);
                    mediaRepository.flush();
                }
                
                // 2. Luego eliminar archivos físicos (si se eliminaron correctamente de BD)
                for (UploadedFile file : filesToDelete) {
                    try {
                        fileStorageService.deleteFileOnly(file.getUuid(), resolveDirectory(file.getCategory()));
                        log.info("Archivo eliminado del disco: {}", file.getUuid());
                    } catch (Exception e) {
                        log.error("No se pudo eliminar archivo uuid={}: {}", file.getUuid(), e.getMessage());
                        // No lanzar excepción para no romper la transacción
                    }
                }
            }
            
            List<MediaDTO> nuevas = articleDTO.getMedias().stream()
                    .filter(m -> m.getUuid() == null)
                    .collect(Collectors.toList());
            saveMedia(nuevas, foundArticle);
        }
          if (articleDTO.getLinks() != null) {
            List<Link> oldLinks = linkRepository.findAllByOwnerTypeAndOwnerUuid(OwnerType.ARTICLE, foundArticle.getUuid());
            if (oldLinks != null && !oldLinks.isEmpty()) {
                linkRepository.deleteAll(oldLinks);
            }
            saveLinks(articleDTO.getLinks(), foundArticle.getUuid());
        }

        if (articleDTO.getSection_id() != null) {
            Section section = sectionRepository.findOneByUuid(articleDTO.getSection_id());
            foundArticle.setSection(section);
        }

        foundArticle.setDate(articleDTO.getDate());
        foundArticle.setTitle(articleDTO.getTitle());
        foundArticle.setText(articleDTO.getText());

        articleRepository.saveAndFlush(foundArticle);
        entityManager.clear();
        return articleMapper.toDTO(articleRepository.findOneByUuid(articleUuid));
        // Resto del código...
    }
    private String resolveDirectory(FileCategory category) {
        if (category == null) return PHOTOS_DIRECTORY;
        return switch (category) {
            case VIDEO    -> VIDEOS_DIRECTORY;
            case DOCUMENT -> DOCUMENTS_DIRECTORY;
            default       -> PHOTOS_DIRECTORY;
        };
    }
}
