package com.nowcode.question_answer.service;

import com.nowcode.question_answer.dao.LoginTicketDAO;
import com.nowcode.question_answer.dao.UserDAO;
import com.nowcode.question_answer.model.LoginTicket;
import com.nowcode.question_answer.model.User;
import com.nowcode.question_answer.utils.SaltUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.lang.StringUtils;

import java.util.*;

@Service
public class UserService {
    @Autowired
    UserDAO userDAO;

    @Autowired
    LoginTicketDAO loginTicketDAO;

    //退出登录
    public void logout(String ticket){
        loginTicketDAO.updateStatus(ticket,1);
    }

    //添加用于判断用户是否在线的ticket
    public String addLoginTicket(int userID){
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserID(userID);
        Date expiredTime = new Date();
        expiredTime.setTime(expiredTime.getTime()+3600*24*1000);
        loginTicket.setExpired(expiredTime);
        loginTicket.setStatus(0);
        loginTicket.setTicket(UUID.randomUUID().toString().replace("-",""));
        loginTicketDAO.addTicket(loginTicket);
        return loginTicket.getTicket();
    }

    /**
     *  完成登录逻辑判断,返回登录信息
     * @param username
     * @param password
     * @return
     */
    public Map<String,Object> login(String username,String password){
        Map<String,Object> map = new HashMap<>();

        if(StringUtils.isBlank(username)||StringUtils.isBlank(password)){
            map.put("msg","用户名或密码不能为空！");
            return map;
        }
        User user = userDAO.findUserByName(username);
        if(user==null){
            map.put("msg","用户名不存在，请注册！");
            return  map;
        }
        if(!SaltUtil.MD5(password+user.getSalt()).equals(user.getPassword())){
            map.put("msg","用户密码错误！");
            return map;
        }

        String ticket = addLoginTicket(user.getId());
        map.put("ticket",ticket);
        map.put("userId", user.getId());
        return map;
    }

    //注册，判断用户名和密码是否符合条件,并完成注册功能
    public Map<String,Object> register(String username,String password){
        Map<String,Object> map = new HashMap<>();
        if(StringUtils.isBlank(username)){
            map.put("msg","用户名不能为空！");
            return map;
        }
        if(StringUtils.isBlank(password)){
            map.put("msg","密码不能为空！");
            return map;
        }

        User user = userDAO.findUserByName(username);
        if(user!=null){
            map.put("msg","用户名已存在");
            return map;
        }

        user = new User();
        user.setUsername(username);
        user.setSalt(UUID.randomUUID().toString().substring(0,5));
        user.setPassword(SaltUtil.MD5(password+user.getSalt()));
        user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        userDAO.addUser(user);

        String ticket = addLoginTicket(user.getId());
        map.put("ticket",ticket);
        return map;
    }

    public User getUser(int id){
        return userDAO.findUserById(id);
    }

    public User getUserByName(String username){
        return  userDAO.findUserByName(username);
    }

}
