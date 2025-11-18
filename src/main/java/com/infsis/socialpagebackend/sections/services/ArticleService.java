package com.infsis.socialpagebackend.sections.services;

import com.infsis.socialpagebackend.authentication.models.Users;
import com.infsis.socialpagebackend.authentication.repositories.UserRepository;
import com.infsis.socialpagebackend.exceptions.NotFoundException;
import com.infsis.socialpagebackend.sections.dtos.ArticleDTO;
import com.infsis.socialpagebackend.sections.dtos.ArticleMediaDTO;
import com.infsis.socialpagebackend.sections.mappers.ArticleMapper;
import com.infsis.socialpagebackend.sections.mappers.ArticleMediaMapper;
import com.infsis.socialpagebackend.sections.mappers.LinkMapper;
import com.infsis.socialpagebackend.sections.models.Article;
import com.infsis.socialpagebackend.sections.models.ArticleMedia;
import com.infsis.socialpagebackend.sections.models.Section;
import com.infsis.socialpagebackend.sections.models.Link;
import com.infsis.socialpagebackend.sections.dtos.LinkDTO;
import com.infsis.socialpagebackend.enums.OwnerType;
import com.infsis.socialpagebackend.sections.repositories.ArticleMediaRepository;
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
    private ArticleMediaMapper mediaMapper;

    @Autowired
    private ArticleMediaRepository articleMediaRepository;

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
        return articleRepository
                .findAll()
                .stream()
                .filter(article -> !article.isDeleted())
                // proteger por posibles fechas nulas y ordenar por fecha ascendente (más antigua primero)
                .sorted((a, b) -> {
                    if (a.getCreatedDate() == null && b.getDate() == null) return 0;
                    if (a.getCreatedDate() == null) return 1;
                    if (b.getCreatedDate() == null) return -1;
                    return a.getCreatedDate().compareTo(b.getCreatedDate());
                })
                .map(article -> articleMapper.toDTO(article))
                .collect(Collectors.toList());
    }

    public List<ArticleDTO> getAllBySection(String sectionUuid) {
        return articleRepository
                .findAll()
                .stream()
                .filter(article -> {
                    if (article == null || article.isDeleted()) return false;
                    if (article.getSection() == null || article.getSection().getUuid() == null) return false;
                    return article.getSection().getUuid().equals(sectionUuid);
                })
                // ordenar por fecha ascendente
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
            List<ArticleMedia> medias = saveMedia(articleDTO.getMedias(), article);

            // guardar links (polimórfico): asignar ownerType=ARTICLE ownerUuid=article.uuid
            saveLinks(articleDTO.getLinks(), article.getUuid());

            article.setUsers(user);
            article.setSection(section);
            article.setTitle(articleDTO.getTitle());
            article.setText(articleDTO.getText());
            article.setDate(articleDTO.getDate());
            article.setArticle_medias(medias);

            articleRepository.save(article);

            // links ya guardados en la tabla link asociados por ownerType/ownerUuid

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

        // marcar el artículo como deleted (soft delete)
        article.setDeleted(true);
        articleRepository.save(article);

        // marcar medias asociadas como deleted (consistente)
        if (article.getArticle_medias() != null && !article.getArticle_medias().isEmpty()) {
            article.getArticle_medias().forEach(media -> {
                media.setDeleted(true);
                articleMediaRepository.save(media);
            });
        }

        // marcar también los links asociados (ownerType=ARTICLE) como deleted para mantener consistencia
        java.util.List<Link> links = linkRepository.findAllByOwnerTypeAndOwnerUuid(OwnerType.ARTICLE, articleUuid);
        if (links != null && !links.isEmpty()) {
            links.forEach(link -> { link.setDeleted(true); linkRepository.save(link); });
        }

        return articleMapper.toDTO(article);
    }

    private List<ArticleMedia> saveMedia(List<ArticleMediaDTO> mediaDTOS, Article article){
        List<ArticleMedia> medias = new ArrayList<>();
        if(mediaDTOS != null ) {
            medias = mediaDTOS
                    .stream()
                    .map(media -> mediaMapper.getMedia(media, article))
                    .collect(Collectors.toList());

            medias.forEach(media -> articleMediaRepository.save(media));
        }
        return medias;
    }

    // guardar links helper (polimórfico)
    private List<Link> saveLinks(List<LinkDTO> linkDTOS, String ownerUuid) {
        List<com.infsis.socialpagebackend.sections.models.Link> links = new ArrayList<>();
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

        // Eliminar todas las medias antiguas si se envían nuevas medias
        if (articleDTO.getMedias() != null) {
            // Eliminar las medias antiguas de la base de datos
            if (foundArticle.getArticle_medias() != null && !foundArticle.getArticle_medias().isEmpty()) {
                foundArticle.getArticle_medias().forEach(media -> {
                    media.setDeleted(true);
                    articleMediaRepository.save(media);
                });
            }
            
            // Guardar las nuevas medias
            List<ArticleMedia> newMedias = saveMedia(articleDTO.getMedias(), foundArticle);
            foundArticle.setArticle_medias(newMedias);
        }

        // manejar links: si se envían nuevos links, eliminamos los antiguos y guardamos los nuevos
        if (articleDTO.getLinks() != null) {
            // marcar como deleted los links antiguos asociados a este article
            List<Link> oldLinks = linkRepository.findAllByOwnerTypeAndOwnerUuid(OwnerType.ARTICLE, foundArticle.getUuid());
            if (oldLinks != null) {
                oldLinks.forEach(link -> { link.setDeleted(true); linkRepository.save(link); });
            }

            // guardar los nuevos links asociados por ownerUuid
            List<Link> newLinks = saveLinks(articleDTO.getLinks(), foundArticle.getUuid());
            // no seteamos en la entidad Article la lista de links (se consultan desde LinkRepository)
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
