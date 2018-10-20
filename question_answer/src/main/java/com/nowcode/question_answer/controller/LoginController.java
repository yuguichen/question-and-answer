package com.nowcode.question_answer.controller;

import com.nowcode.question_answer.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;


@Controller
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    UserService userService;
    //登录请求
    @RequestMapping(path = {"/login/"},method = {RequestMethod.POST})
    public String login(Model model,
                        @RequestParam("username") String username,
                        @RequestParam("password") String password,
                        @RequestParam(value = "rememberme",defaultValue = "false") boolean rememberme,
                        HttpServletResponse response){
        try{
            Map<String, String> loginMap = userService.login(username, password);
            if(loginMap.containsKey("ticket")){
                Cookie cookie = new Cookie("ticket",loginMap.get("ticket"));
                cookie.setPath("/");
                response.addCookie(cookie);
                return "redirect:/";
            } else {
                model.addAttribute("msg",loginMap.get("msg"));
                return "login";
            }
        }catch (Exception e){
            logger.error("登录异常："+e.getMessage());
            return "login";
        }
    }

    //注册POST请求
    @RequestMapping(path = {"/register/"},method = {RequestMethod.POST})
    public String register(Model model,
                           @RequestParam("username") String username,
                           @RequestParam("password") String password,
                           HttpServletResponse response){

        try{
            Map<String,String> regMap = userService.register(username,password);
            if(regMap.containsKey("ticket")){
                Cookie cookie = new Cookie("ticket",regMap.get("ticket"));
                cookie.setPath("/");
                response.addCookie(cookie);
                return "redirect:/";
            } else {
                model.addAttribute("msg",regMap.get("msg"));
                return "login";
            }
        }catch (Exception e){
            logger.error("注册异常："+e.getMessage());
            return "login";
        }
    }


    //登录注册页面
    @RequestMapping(path = {"/regLogin"},method = {RequestMethod.GET})
    public String regLogin(Model model){
        return "login";
    }


}
