package com.nowcode.question_answer.controller;


import com.nowcode.question_answer.model.Question;
import com.nowcode.question_answer.model.ViewObject;
import com.nowcode.question_answer.service.QuestionService;
import com.nowcode.question_answer.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    QuestionService questionService;
    @Autowired
    UserService userService;

    @RequestMapping(path = {"/","/index"},method = {RequestMethod.GET})
    public String index(Model model){
        List<Question> questionList = questionService.getLatestQuestions(0,0,10);
        //model.addAttribute("questions",questionList);
        List<ViewObject> viewObjectList = new ArrayList<>();
        for(Question question : questionList){
            ViewObject viewObject = new ViewObject();
            viewObject.set("question",question);
            //System.out.println(question.getUserID());
            viewObject.set("user",userService.getUser(question.getUserID()));
            viewObjectList.add(viewObject);
        }
        model.addAttribute("viewObjectList",viewObjectList);
        return "index";
    }
}
