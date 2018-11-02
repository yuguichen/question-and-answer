package com.nowcode.question_answer.controller;


import com.nowcode.question_answer.model.Comment;
import com.nowcode.question_answer.model.EntityType;
import com.nowcode.question_answer.model.HostHolder;
import com.nowcode.question_answer.service.CommentService;
import com.nowcode.question_answer.service.QuestionService;
import com.nowcode.question_answer.service.SensitiveService;
import com.nowcode.question_answer.utils.MyUtils;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.HtmlUtils;

import java.util.Date;

@Controller
public class CommentController {
    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);

    @Autowired
    CommentService commentService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    SensitiveService sensitiveService;
    @Autowired
    QuestionService questionService;

    @RequestMapping(path = {"/addComment"},method = {RequestMethod.POST})
    public String addComment(@Param("content") String content,
                             @Param("questionId") int questionId){
        try{
            //过滤评论内容
            content = HtmlUtils.htmlEscape(content);
            content = sensitiveService.filter(content);
            //创建Comment
            Comment comment = new Comment();
            if(hostHolder.getUser()!=null)
                comment.setUserId(hostHolder.getUser().getId());
            else
                comment.setUserId(MyUtils.ANONYMOUS_USER_ID);
            comment.setContent(content);
            comment.setCreatedDate(new Date());
            comment.setEntityId(questionId);
            comment.setEntityType(EntityType.ENTITY_QUESTION);
            comment.setStatus(0);
            //addComment
            commentService.addComment(comment);

            int count = commentService.getCommentCount(comment.getEntityId(),comment.getEntityType());
            questionService.updateCommentCount(count,comment.getEntityId());

        } catch(Exception e){
            logger.error("addComment error:"+e.getMessage());
        }
        return "redirect:/question/"+String.valueOf(questionId);
    }


}
