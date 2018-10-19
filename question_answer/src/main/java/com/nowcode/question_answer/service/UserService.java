package com.nowcode.question_answer.service;

import com.nowcode.question_answer.dao.UserDAO;
import com.nowcode.question_answer.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserDAO userDAO;

    public User getUser(int id){
        return userDAO.findUserById(id);
    }
}
