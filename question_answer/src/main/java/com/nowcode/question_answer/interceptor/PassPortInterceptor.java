package com.nowcode.question_answer.interceptor;

import com.nowcode.question_answer.dao.LoginTicketDAO;
import com.nowcode.question_answer.dao.UserDAO;
import com.nowcode.question_answer.model.HostHolder;
import com.nowcode.question_answer.model.LoginTicket;
import com.nowcode.question_answer.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * 拦截器
 * 判断用户身份
 */
@Component
public class PassPortInterceptor implements HandlerInterceptor {
    @Autowired
    LoginTicketDAO loginTicketDAO;

    @Autowired
    UserDAO userDAO;

    @Autowired
    HostHolder hostHolder;

    @Override
    //请求之前进行拦截，通过cookie中的ticket确认用户身份
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ticket = null;
        //找到ticket
        if(request.getCookies()!=null){
            for(Cookie cookie:request.getCookies()){
                if(cookie.getName().equals("ticket")){
                    ticket=cookie.getValue();
                    break;
                }
            }
        }

        if(ticket!=null){
            //对比cookie中的ticket和用户的ticket是否有效
            LoginTicket loginTicket = loginTicketDAO.selectByTicket(ticket);
            if(loginTicket.getTicket()==null||loginTicket.getExpired().before(new Date())||loginTicket.getStatus()!=0){
                return true;
            }

            User user = userDAO.findUserById(loginTicket.getUserID());
            hostHolder.setUser(user);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if(modelAndView!=null){
            //可以再view中访问user
            modelAndView.addObject("user",hostHolder.getUser());
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        hostHolder.clear();
    }
}
