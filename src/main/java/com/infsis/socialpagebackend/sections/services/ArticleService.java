package com.infsis.socialpagebackend.sections.services;

import com.infsis.socialpagebackend.authentication.models.Users;
import com.infsis.socialpagebackend.authentication.repositories.UserRepository;
import com.infsis.socialpagebackend.exceptions.NotFoundException;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class ArticleService {

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

    public List<ArticleDTO> getAllBySection(String sectionUuid) {
        String tenantId = TenantContext.getCurrentTenant();
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
        Article article = articleRepository.save(new Article());
        if (section != null && user != null) {
            List<Media> medias = saveMedia(articleDTO.getMedias(), article);

            saveLinks(articleDTO.getLinks(), article.getUuid());

            article.setUsers(user);
            article.setSection(section);
            article.setTitle(articleDTO.getTitle());
            article.setText(articleDTO.getText());
            article.setDate(articleDTO.getDate());
            article.setArticle_medias(medias);

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

        article.setDeleted(true);
        articleRepository.save(article);

        if (article.getArticle_medias() != null && !article.getArticle_medias().isEmpty()) {
            article.getArticle_medias().forEach(media -> {
                media.setDeleted(true);
                mediaRepository.save(media);
            });
        }

        List<Link> links = linkRepository.findAllByOwnerTypeAndOwnerUuid(OwnerType.ARTICLE, articleUuid);
        if (links != null && !links.isEmpty()) {
            links.forEach(link -> { link.setDeleted(true); linkRepository.save(link); });
        }

        return articleMapper.toDTO(article);
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

    public ArticleDTO updateArticle(String articleUuid, ArticleDTO articleDTO) {
        Article foundArticle = articleRepository.findOneByUuid(articleUuid);

        if (foundArticle == null || foundArticle.isDeleted()) {
            throw new NotFoundException("Article", articleUuid);
        }

        if (articleDTO.getMedias() != null) {
            if (foundArticle.getArticle_medias() != null && !foundArticle.getArticle_medias().isEmpty()) {
                foundArticle.getArticle_medias().forEach(media -> {
                    media.setDeleted(true);
                    mediaRepository.save(media);
                });
            }

            List<Media> newMedias = saveMedia(articleDTO.getMedias(), foundArticle);
            foundArticle.setArticle_medias(newMedias);
        }

        if (articleDTO.getLinks() != null) {
            List<Link> oldLinks = linkRepository.findAllByOwnerTypeAndOwnerUuid(OwnerType.ARTICLE, foundArticle.getUuid());
            if (oldLinks != null) {
                oldLinks.forEach(link -> { link.setDeleted(true); linkRepository.save(link); });
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

        Article updatedArticle = articleRepository.save(foundArticle);

        return articleMapper.toDTO(updatedArticle);
    }
}
