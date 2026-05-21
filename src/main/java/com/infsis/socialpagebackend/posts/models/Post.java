package com.infsis.socialpagebackend.posts.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.infsis.socialpagebackend.authentication.models.Users;
import com.infsis.socialpagebackend.comments.models.Comment;
import com.infsis.socialpagebackend.institutions.models.Institution;
import com.infsis.socialpagebackend.reactions.models.PostReaction;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "post")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(updatable = false, nullable = false, unique = true, length = 36)
    private String uuid;

    @ManyToOne
    @JoinColumn(name = "institution_id", referencedColumnName = "uuid", nullable = false)
    private Institution institution;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "uuid", nullable = false)
    private Users users;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "content_id", referencedColumnName = "uuid", nullable = false, unique = true)
    private Content content;

    @Column(nullable = false, columnDefinition = "BOOLEAN NOT NULL DEFAULT TRUE")
    private boolean commentsEnabled = true;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostReaction> postReactions;

    @Column(nullable = false)
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private Date post_date;

    @Column(nullable = false)
    private String post_type;

    @CreatedDate
    @Column(updatable = false)
    private Date createdDate;

    @LastModifiedDate
    @Column(updatable = false)
    private Date lastModifiedDate;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.ALL})
    @JoinTable(name = "post_group", joinColumns = { @JoinColumn(name = "post_id", referencedColumnName = "uuid") },
            inverseJoinColumns = { @JoinColumn(name = "group_id", referencedColumnName = "uuid") })
    private List<Group> groups = new ArrayList<>();

    @Column(nullable = false, columnDefinition = "BOOLEAN NOT NULL DEFAULT '0'")
    private boolean deleted;

    @PrePersist
    public void initializeUuid() {
        this.setUuid(UUID.randomUUID().toString());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return deleted == post.deleted && commentsEnabled == post.commentsEnabled && Objects.equals(id, post.id) && Objects.equals(uuid, post.uuid) && Objects.equals(institution, post.institution) && Objects.equals(users, post.users) && Objects.equals(content, post.content) && Objects.equals(postReactions, post.postReactions) && Objects.equals(post_date, post.post_date) && Objects.equals(createdDate, post.createdDate) && Objects.equals(lastModifiedDate, post.lastModifiedDate) && Objects.equals(comments, post.comments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, uuid, institution, users, content, commentsEnabled, postReactions, post_date, createdDate, lastModifiedDate, comments, deleted);
    }
}
