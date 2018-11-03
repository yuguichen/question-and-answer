package com.nowcode.question_answer.service;

import com.nowcode.question_answer.dao.CommentDAO;
import com.nowcode.question_answer.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    CommentDAO commentDAO;
    @Autowired
    SensitiveService sensitiveService;

    public int addComment(Comment comment){
        //过滤评论内容
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        comment.setContent(sensitiveService.filter(comment.getContent()));
        return commentDAO.addComment(comment);
    }

    public void deleteComment(int entityId, int entityType,int status){
        commentDAO.updateStatus(entityId,entityType,status);
    }

    public List<Comment> getCommentsByEntity( int entityId, int entityType){
        return commentDAO.selectByEntity(entityId,entityType);
    }

    public int getCommentCount(int entityId, int entityType){
        return commentDAO.getCommentCount(entityId,entityType);
    }


}
