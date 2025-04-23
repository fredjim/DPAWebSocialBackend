package com.infsis.socialpagebackend.replies.repository;

import com.infsis.socialpagebackend.replies.model.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReplyRepository extends JpaRepository<Reply, Integer> {
    Reply findByUuid(String uuid);
    List<Reply> findByCommentUuid(String commentUuid);

    @Query("SELECT r FROM Reply r LEFT JOIN FETCH r.replies WHERE r.comment.uuid = :commentUuid AND r.parentReply IS NULL ORDER BY r.createdDate ASC")
List<Reply> findTopLevelRepliesByCommentUuid(@Param("commentUuid") String commentUuid);


}