package com.nowcode.question_answer.service;

import com.nowcode.question_answer.dao.CommentDAO;
import com.nowcode.question_answer.model.Comment;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    CommentDAO commentDAO;

    public int addComment(Comment comment){
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
