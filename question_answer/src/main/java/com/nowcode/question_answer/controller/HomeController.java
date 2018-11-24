package com.nowcode.question_answer.controller;


import com.nowcode.question_answer.model.HostHolder;
import com.nowcode.question_answer.model.Question;
import com.nowcode.question_answer.model.User;
import com.nowcode.question_answer.model.ViewObject;
import com.nowcode.question_answer.service.CommentService;
import com.nowcode.question_answer.service.FollowService;
import com.nowcode.question_answer.service.QuestionService;
import com.nowcode.question_answer.service.UserService;
import com.nowcode.question_answer.utils.MyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    QuestionService questionService;
    @Autowired
    UserService userService;
    @Autowired
    CommentService commentService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    FollowService followService;

    private List<ViewObject> getQuestions(int userID,int offset,int limit){
        List<ViewObject> viewObjectList = new ArrayList<>();
        List<Question> questionList = questionService.getLatestQuestions(userID,offset,limit);
        for(Question question : questionList){
            ViewObject viewObject = new ViewObject();
            viewObject.set("question",question);
            viewObject.set("followCount", followService.getFollowerCount(MyUtils.ENTITY_QUESTION, question.getId()));
            viewObject.set("user",userService.getUser(question.getUserID()));
            viewObjectList.add(viewObject);
        }
        return viewObjectList;
    }

    @RequestMapping(path = {"/","/index"},method = {RequestMethod.GET})
    public String index(Model model){
        List<ViewObject> viewObjectList = getQuestions(0,0,10);
        model.addAttribute("viewObjectList",viewObjectList);
        return "index";
    }

    @RequestMapping(path = {"/user/{userID}"},method = {RequestMethod.GET,RequestMethod.POST})
    public String userIndex(Model model, @PathVariable("userID") int userID){
        model.addAttribute("viewObjectList",getQuestions(userID,0,10));

        User user = userService.getUser(userID);
        ViewObject vo = new ViewObject();
        vo.set("user", user);
        vo.set("commentCount", commentService.getUserCommentCount(userID));
        vo.set("followerCount", followService.getFollowerCount(MyUtils.ENTITY_USER, userID));
        vo.set("followeeCount", followService.getFolloweeCount(userID, MyUtils.ENTITY_USER));
        if (hostHolder.getUser() != null) {
            vo.set("followed", followService.isFollower(hostHolder.getUser().getId(), MyUtils.ENTITY_USER, userID));
        } else {
            vo.set("followed", false);
        }
        model.addAttribute("profileUser", vo);
        return "profile";
    }
}
