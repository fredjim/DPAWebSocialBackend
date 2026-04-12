package com.infsis.socialpagebackend.sections.mappers;

import com.infsis.socialpagebackend.authentication.models.Users;
import com.infsis.socialpagebackend.posts.mappers.MediaMapper;
import com.infsis.socialpagebackend.posts.models.Media;
import com.infsis.socialpagebackend.sections.dtos.ArticleDTO;
import com.infsis.socialpagebackend.sections.models.Article;
import com.infsis.socialpagebackend.sections.models.Section;
import com.infsis.socialpagebackend.enums.OwnerType;
import com.infsis.socialpagebackend.sections.repositories.LinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ArticleMapper {

    @Autowired
    private MediaMapper mediaMapper;

    @Autowired
    private LinkMapper linkMapper;

    @Autowired
    private LinkRepository linkRepository;

    public ArticleDTO toDTO(Article article) {

        ArticleDTO articleDTO = new ArticleDTO();

        articleDTO.setUuid(article.getUuid());
        articleDTO.setTitle(article.getTitle());
        articleDTO.setText(article.getText());
        articleDTO.setDate(article.getDate());
        articleDTO.setUser_id(article.getUsers().getUuid());
        articleDTO.setSection_id(article.getSection().getUuid());
        articleDTO.setMedias(
                article.getArticle_medias()
                        .stream()
                        .map(media -> mediaMapper.toDTO(media))
                        .collect(Collectors.toList())
        );

        List<com.infsis.socialpagebackend.sections.models.Link> links =
                linkRepository.findAllByOwnerTypeAndOwnerUuidAndDeletedFalse(OwnerType.ARTICLE, article.getUuid());
        if (links != null) {
            articleDTO.setLinks(
                    links.stream()
                            .map(link -> linkMapper.toDTO(link))
                            .collect(Collectors.toList())
            );
        }

        return articleDTO;
    }

    public Article getArticle(ArticleDTO articleDTO, Section section, Users user) {

        Article article = new Article();

        article.setUsers(user);
        article.setSection(section);
        article.setTitle(articleDTO.getTitle());
        article.setText(articleDTO.getText());
        article.setDate(articleDTO.getDate());

        return article;
    }

    public Article getArticle(ArticleDTO articleDTO, List<Media> medias, Section section, Users user) {

        Article article = new Article();

        article.setUsers(user);
        article.setSection(section);
        article.setTitle(articleDTO.getTitle());
        article.setText(articleDTO.getText());
        article.setDate(articleDTO.getDate());
        article.setArticle_medias(medias);

        return article;
    }
}
