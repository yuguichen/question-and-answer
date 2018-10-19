package com.nowcode.question_answer.service;

import com.nowcode.question_answer.dao.QuestionDAO;
import com.nowcode.question_answer.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionService {

    @Autowired
    QuestionDAO questionDAO;

    public List<Question> getLatestQuestions(int userID,int offset,int limit){
        return questionDAO.selectLatestQuestions(userID,offset,limit);
    }
}
