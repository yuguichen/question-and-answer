package com.nowcode.question_answer.controller;

import com.nowcode.question_answer.model.*;
import com.nowcode.question_answer.service.*;
import com.nowcode.question_answer.utils.MyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class QuestionController {
    private static final Logger logger = LoggerFactory.getLogger(QuestionController.class);

    @Autowired
    QuestionService questionService;
    @Autowired
    UserService userService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    CommentService commentService;
    @Autowired
    LikeService likeService;
    @Autowired
    FollowService followService;

    /**
     * 提问
     * @param title
     * @param content
     * @return
     */
    @RequestMapping(value = {"/question/add"},method = {RequestMethod.POST})
    @ResponseBody
    public String addQuestion(@RequestParam("title") String title,
                              @RequestParam("content") String content){
        try{
            //新问题
            Question question = new Question();
            question.setTitle(title);
            question.setContent(content);
            question.setCreatedDate(new Date());
            question.setCommentCount(0);
            if(hostHolder.getUser()==null){
                //question.setUserID(MyUtils.ANONYMOUS_USER_ID);
                return MyUtils.getJSONString(999);
            }else{
                question.setUserID(hostHolder.getUser().getId());
            }

            if(questionService.addQuestion(question)>0){
                return MyUtils.getJSONString(0);
            }

        }catch (Exception e){
            logger.error("增加题目失败"+e.getMessage());
        }

        return MyUtils.getJSONString(1,"失败");
    }


    /**
     * 评论中心
     * @param model
     * @param qid
     * @return
     */
    @RequestMapping(value = {"/question/{qid}"})
    public String questionDetail(Model model, @PathVariable("qid") int qid){
        Question question = questionService.selectByID(qid);
        model.addAttribute("question",question);
        model.addAttribute("user",userService.getUser(question.getUserID()));

        List<Comment> comments = commentService.getCommentsByEntity(qid,MyUtils.ENTITY_QUESTION);
        List<ViewObject> commentsVOList = new ArrayList<>();
        for(Comment comment : comments){
            ViewObject viewObject = new ViewObject();
            viewObject.set("comment",comment);
            viewObject.set("user",userService.getUser(comment.getUserId()));
            if(hostHolder.getUser()==null)
                viewObject.set("liked",0);
            else
                viewObject.set("liked",likeService.getLikeStatus(hostHolder.getUser().getId(),MyUtils.ENTITY_COMMENT,comment.getId()));
            viewObject.set("likeCount",likeService.getLikeCount(MyUtils.ENTITY_COMMENT,comment.getId()));
            commentsVOList.add(viewObject);
        }
        model.addAttribute("comments",commentsVOList);

        List<ViewObject> followUsers = new ArrayList<ViewObject>();
        // 获取关注的用户信息
        List<Integer> users = followService.getFollowers(MyUtils.ENTITY_QUESTION, qid, 20);
        for (Integer userId : users) {
            ViewObject vo = new ViewObject();
            User user = userService.getUser(userId);
            if (user == null) {
                continue;
            }
            vo.set("name", user.getUsername());
            vo.set("headUrl", user.getHeadUrl());
            vo.set("id", user.getId());
            followUsers.add(vo);
        }
        model.addAttribute("followUsers", followUsers);
        if (hostHolder.getUser() != null) {
            model.addAttribute("followed", followService.isFollower(hostHolder.getUser().getId(), MyUtils.ENTITY_QUESTION, qid));
        } else {
            model.addAttribute("followed", false);
        }

        return "detail";
    }

}
