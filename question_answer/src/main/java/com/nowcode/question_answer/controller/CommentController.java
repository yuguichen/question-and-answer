package com.nowcode.question_answer.controller;


import com.nowcode.question_answer.async.EventModel;
import com.nowcode.question_answer.async.EventProducer;
import com.nowcode.question_answer.async.EventType;
import com.nowcode.question_answer.model.Comment;
import com.nowcode.question_answer.model.HostHolder;
import com.nowcode.question_answer.service.CommentService;
import com.nowcode.question_answer.service.QuestionService;
import com.nowcode.question_answer.utils.MyUtils;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;

@Controller
public class CommentController {
    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);

    @Autowired
    CommentService commentService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    QuestionService questionService;
    @Autowired
    EventProducer eventProducer;

    @RequestMapping(path = {"/addComment"},method = {RequestMethod.POST})
    public String addComment(@Param("content") String content,
                             @Param("questionId") int questionId){
        try{
            //创建Comment
            Comment comment = new Comment();
            if(hostHolder.getUser()!=null)
                comment.setUserId(hostHolder.getUser().getId());
            else
                comment.setUserId(MyUtils.ANONYMOUS_USER_ID);
            comment.setContent(content);
            comment.setCreatedDate(new Date());
            comment.setEntityId(questionId);
            comment.setEntityType(MyUtils.ENTITY_QUESTION);
            comment.setStatus(0);
            //addComment
            commentService.addComment(comment);
            //评论数量
            int count = commentService.getCommentCount(comment.getEntityId(),comment.getEntityType());
            questionService.updateCommentCount(count,comment.getEntityId());

            eventProducer.fireEvent(new EventModel(EventType.COMMENT).setActorId(comment.getUserId())
                    .setEntityId(questionId));

        } catch(Exception e){
            logger.error("addComment error:"+e.getMessage());
        }
        return "redirect:/question/"+String.valueOf(questionId);
    }


}
