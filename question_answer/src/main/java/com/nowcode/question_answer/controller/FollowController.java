package com.nowcode.question_answer.controller;

import com.nowcode.question_answer.async.EventModel;
import com.nowcode.question_answer.async.EventProducer;
import com.nowcode.question_answer.async.EventType;
import com.nowcode.question_answer.model.HostHolder;
import com.nowcode.question_answer.model.Question;
import com.nowcode.question_answer.model.User;
import com.nowcode.question_answer.model.ViewObject;
import com.nowcode.question_answer.service.CommentService;
import com.nowcode.question_answer.service.FollowService;
import com.nowcode.question_answer.service.QuestionService;
import com.nowcode.question_answer.service.UserService;
import com.nowcode.question_answer.utils.MyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class FollowController {
    @Autowired
    FollowService followService;
    @Autowired
    UserService userService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    EventProducer eventProducer;
    @Autowired
    QuestionService questionService;
    @Autowired
    CommentService commentService;


    @RequestMapping(path = {"/followUser"},method = {RequestMethod.POST})
    @ResponseBody
    public String followUser(@RequestParam("userId") int userId){
        if(hostHolder.getUser()==null){
            return MyUtils.getJSONString(999);
        }

        boolean ret =followService.follow(hostHolder.getUser().getId(),MyUtils.ENTITY_USER,userId);

        eventProducer.fireEvent(new EventModel(EventType.FOLLOW).setActorId(hostHolder.getUser().getId())
        .setEntityId(userId).setEntityType(MyUtils.ENTITY_USER).setEntityOwnerId(userId));

        return MyUtils.getJSONString(ret?0:1
                ,String.valueOf(followService.getFolloweeCount(hostHolder.getUser().getId(),MyUtils.ENTITY_USER)));
    }

    @RequestMapping(path = {"/unfollowUser"},method = {RequestMethod.POST})
    @ResponseBody
    public String unfollowUser(@RequestParam("userId") int userId){
        if(hostHolder.getUser()==null){
            return MyUtils.getJSONString(999);
        }

        boolean ret =followService.unfollow(hostHolder.getUser().getId(),MyUtils.ENTITY_USER,userId);

        eventProducer.fireEvent(new EventModel(EventType.UNFOLLOW).setActorId(hostHolder.getUser().getId())
                .setEntityId(userId).setEntityType(MyUtils.ENTITY_USER).setEntityOwnerId(userId));

        return MyUtils.getJSONString(ret?0:1
                ,String.valueOf(followService.getFolloweeCount(hostHolder.getUser().getId(),MyUtils.ENTITY_USER)));
    }

    @RequestMapping(path = {"/followQuestion"},method = {RequestMethod.POST})
    @ResponseBody
    public String followQuestion(@RequestParam("questionId") int questionId){
        if(hostHolder.getUser()==null){
            return MyUtils.getJSONString(999);
        }

        //判断问题是否存在
        Question question = questionService.selectByID(questionId);
        if(question == null)
            return MyUtils.getJSONString(1,"问题不存在");
        //关注并异步处理关注消息
        boolean ret =followService.follow(hostHolder.getUser().getId(),MyUtils.ENTITY_QUESTION,questionId);
        eventProducer.fireEvent(new EventModel(EventType.FOLLOW).setActorId(hostHolder.getUser().getId())
                .setEntityId(questionId).setEntityType(MyUtils.ENTITY_QUESTION).setEntityOwnerId(question.getUserID()));

        Map<String,Object> info = new HashMap<>();
        info.put("headUrl", hostHolder.getUser().getHeadUrl());
        info.put("name", hostHolder.getUser().getUsername());
        info.put("id", hostHolder.getUser().getId());
        info.put("count", followService.getFollowerCount(MyUtils.ENTITY_QUESTION, questionId));

        return MyUtils.getJSONString(ret?0:1,info);
    }


    @RequestMapping(path = {"/unfollowQuestion"},method = {RequestMethod.POST})
    @ResponseBody
    public String unfollowQuestion(@RequestParam("questionId") int questionId){
        if(hostHolder.getUser()==null){
            return MyUtils.getJSONString(999);
        }
        //判断问题是否存在
        Question question = questionService.selectByID(questionId);
        if(question == null)
            return MyUtils.getJSONString(1,"问题不存在");
        //关注并异步处理关注消息
        boolean ret =followService.unfollow(hostHolder.getUser().getId(),MyUtils.ENTITY_QUESTION,questionId);
        eventProducer.fireEvent(new EventModel(EventType.UNFOLLOW).setActorId(hostHolder.getUser().getId())
                .setEntityId(questionId).setEntityType(MyUtils.ENTITY_QUESTION).setEntityOwnerId(question.getUserID()));

        Map<String,Object> info = new HashMap<>();
        info.put("id", hostHolder.getUser().getId());
        info.put("count", followService.getFollowerCount(MyUtils.ENTITY_QUESTION, questionId));
        return MyUtils.getJSONString(ret?0:1,info);
    }

    @RequestMapping(path = {"/user/{uid}/followers"}, method = {RequestMethod.GET})
    public String followers(Model model, @PathVariable("uid") int userId) {
        List<Integer> followerIds = followService.getFollowers(MyUtils.ENTITY_USER, userId, 0, 10);
        if (hostHolder.getUser() != null) {
            model.addAttribute("followers", getUsersInfo(hostHolder.getUser().getId(), followerIds));
        } else {
            model.addAttribute("followers", getUsersInfo(0, followerIds));
        }
        model.addAttribute("followerCount", followService.getFollowerCount(MyUtils.ENTITY_USER, userId));
        model.addAttribute("curUser", userService.getUser(userId));
        return "followers";
    }


    @RequestMapping(path = {"/user/{uid}/followees"}, method = {RequestMethod.GET})
    public String followees(Model model, @PathVariable("uid") int userId) {
        List<Integer> followeeIds = followService.getFollowees(userId, MyUtils.ENTITY_USER, 0, 10);

        if (hostHolder.getUser() != null) {
            model.addAttribute("followees", getUsersInfo(hostHolder.getUser().getId(), followeeIds));
        } else {
            model.addAttribute("followees", getUsersInfo(0, followeeIds));
        }
        model.addAttribute("followeeCount", followService.getFolloweeCount(userId, MyUtils.ENTITY_USER));
        model.addAttribute("curUser", userService.getUser(userId));
        return "followees";
    }

    private List<ViewObject> getUsersInfo(int localUserId, List<Integer> userIds) {
        List<ViewObject> userInfos = new ArrayList<ViewObject>();
        for (Integer uid : userIds) {
            User user = userService.getUser(uid);
            if (user == null) {
                continue;
            }
            ViewObject vo = new ViewObject();
            vo.set("user", user);
            vo.set("commentCount", commentService.getUserCommentCount(uid));
            vo.set("followerCount", followService.getFollowerCount(MyUtils.ENTITY_USER, uid));
            vo.set("followeeCount", followService.getFolloweeCount(uid, MyUtils.ENTITY_USER));
            if (localUserId != 0) {
                vo.set("followed", followService.isFollower(localUserId, MyUtils.ENTITY_USER, uid));
            } else {
                vo.set("followed", false);
            }
            userInfos.add(vo);
        }
        return userInfos;
    }


}
