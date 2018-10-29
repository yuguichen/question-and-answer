package com.nowcode.question_answer.controller;

import com.nowcode.question_answer.model.HostHolder;
import com.nowcode.question_answer.model.Question;
import com.nowcode.question_answer.service.QuestionService;
import com.nowcode.question_answer.utils.MyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Controller
public class QuestionController {

    private static final Logger logger = LoggerFactory.getLogger(QuestionController.class);

    @Autowired
    QuestionService questionService;
    @Autowired
    HostHolder hostHolder;

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

}
