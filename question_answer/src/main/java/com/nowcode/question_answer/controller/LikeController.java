package com.nowcode.question_answer.controller;

import com.nowcode.question_answer.async.EventModel;
import com.nowcode.question_answer.async.EventProducer;
import com.nowcode.question_answer.async.EventType;
import com.nowcode.question_answer.model.HostHolder;
import com.nowcode.question_answer.service.CommentService;
import com.nowcode.question_answer.service.LikeService;
import com.nowcode.question_answer.utils.MyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LikeController {
    private static final Logger logger = LoggerFactory.getLogger(LikeController.class);

    @Autowired
    HostHolder hostHolder;
    @Autowired
    LikeService likeService;
    @Autowired
    EventProducer eventProducer;
    @Autowired
    CommentService commentService;

    @RequestMapping(value = {"/like"},method = {RequestMethod.POST})
    @ResponseBody
    public String like(@RequestParam("commentId") int commentId){
        if(hostHolder.getUser()==null){
            return MyUtils.getJSONString(999);
        }

        //点赞后系统发私信，将点赞事件加入消息队列
        eventProducer.fireEvent(new EventModel(EventType.LIKE).setActorId(hostHolder.getUser().getId())
                .setEntityType(MyUtils.ENTITY_COMMENT).setEntityId(commentId)
                .setEntityOwnerId(commentService.getCommentById(commentId).getUserId())
                .setExts("questionId",String.valueOf(commentService.getCommentById(commentId).getEntityId())));

        long likeCount = likeService.like(hostHolder.getUser().getId(),MyUtils.ENTITY_COMMENT,commentId);
        return MyUtils.getJSONString(0,String.valueOf(likeCount));
    }

    @RequestMapping(value = {"/dislike"},method = {RequestMethod.POST})
    @ResponseBody
    public String dislike(@RequestParam("commentId") int commentId){
        if(hostHolder.getUser()==null)
            return MyUtils.getJSONString(999);

        long likeCount = likeService.disLike(hostHolder.getUser().getId(),MyUtils.ENTITY_COMMENT,commentId);
        return MyUtils.getJSONString(0,String.valueOf(likeCount));
    }
}
