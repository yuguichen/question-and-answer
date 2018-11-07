package com.nowcode.question_answer.controller;

import com.nowcode.question_answer.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 *    2018/10/20
 */

@Controller
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    UserService userService;

    @RequestMapping(path = {"/logout"},method = {RequestMethod.GET})
    public String logout(@CookieValue("ticket") String ticket){
        userService.logout(ticket);
        return "redirect:/";
    }


    //登录请求
    @RequestMapping(path = {"/login/"},method = {RequestMethod.POST})
    public String login(Model model,
                        @RequestParam("username") String username,
                        @RequestParam("password") String password,
                        @RequestParam(value = "rememberme",defaultValue = "false") boolean rememberme,
                        @RequestParam(value = "next",required = false) String next,
                        HttpServletResponse response){
        try{
            Map<String, Object> loginMap = userService.login(username, password);

            if(loginMap.containsKey("ticket")){
                Cookie cookie = new Cookie("ticket",loginMap.get("ticket").toString());
                cookie.setPath("/");
                response.addCookie(cookie);
                if(StringUtils.isNotBlank(next))
                    return "redirect:"+next;
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
                           @RequestParam(value = "next",required = false) String next,
                           HttpServletResponse response){

        try{
            Map<String,Object> regMap = userService.register(username,password);
            if(regMap.containsKey("ticket")){
                Cookie cookie = new Cookie("ticket",regMap.get("ticket").toString());
                cookie.setPath("/");
                response.addCookie(cookie);
                if(StringUtils.isNotBlank(next))
                    //判断是否需要跳转
                    return "redirect:"+next;
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
    public String regLogin(Model model,
                           @RequestParam(value = "next",required = false) String next){
        model.addAttribute("next",next);
        return "login";
    }


}
