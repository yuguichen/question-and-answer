package com.nowcode.question_answer.service;

import com.nowcode.question_answer.dao.QuestionDAO;
import com.nowcode.question_answer.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class QuestionService {

    @Autowired
    QuestionDAO questionDAO;
    @Autowired
    SensitiveService sensitiveService;

    public Question selectByID(int id){
        return questionDAO.selectById(id);
    }

    public int addQuestion(Question question){
        //html标签敏感词过滤
        question.setContent(HtmlUtils.htmlEscape(question.getContent()));
        question.setTitle(HtmlUtils.htmlEscape(question.getTitle()));
        //非法敏感词过滤
        question.setTitle(sensitiveService.filter(question.getTitle()));
        question.setContent(sensitiveService.filter(question.getContent()));

       return questionDAO.addQuestion(question)>0?question.getId():0;
    }

    public List<Question> getLatestQuestions(int userID,int offset,int limit){
        return questionDAO.selectLatestQuestions(userID,offset,limit);
    }
}
