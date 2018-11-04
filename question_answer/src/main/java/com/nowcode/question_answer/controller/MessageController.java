package com.nowcode.question_answer.controller;

import com.nowcode.question_answer.model.HostHolder;
import com.nowcode.question_answer.model.Message;
import com.nowcode.question_answer.model.User;
import com.nowcode.question_answer.model.ViewObject;
import com.nowcode.question_answer.service.MessageService;
import com.nowcode.question_answer.service.UserService;
import com.nowcode.question_answer.utils.MyUtils;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class MessageController {
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    HostHolder hostHolder;
    @Autowired
    MessageService messageService;
    @Autowired
    UserService userService;

    @RequestMapping(path = {"/msg/list"},method = {RequestMethod.GET})
    public String getConversationList(Model model){
        try{
            int localUserId = hostHolder.getUser().getId();
            List<ViewObject> conversations = new ArrayList<>();
            List<Message> conversationList = messageService.getConversationList(localUserId,0,10);
            for(Message message:conversationList){
                ViewObject viewObject = new ViewObject();
                User user = userService.getUser( message.getFromId()==localUserId? message.getToId():message.getFromId() );

                viewObject.set("message",message);
                viewObject.set("user",user);
                viewObject.set("unread",messageService.getConversationUnreadCount(localUserId,message.getConversationId()));

                conversations.add(viewObject);
            }
            model.addAttribute("conversations",conversations);
        }catch (Exception e){
            logger.error("获取站内信列表失败:"+e.getMessage());
        }
        return "letter";
    }

    @RequestMapping(path = {"/msg/detail"},method = {RequestMethod.GET})
    public String getConversationDetail(Model model, @Param("conversationId") String conversationId){
        try{
            List<Message> messagerList = messageService.getMessagerByConversationId(conversationId, 0, 10);
            List<ViewObject> messages = new ArrayList<>();
            for(Message message:messagerList){

                ViewObject viewObject = new ViewObject();
                viewObject.set("message",message);
                viewObject.set("user",userService.getUser(message.getFromId()));
                messages.add(viewObject);
            }
            model.addAttribute("messages",messages);
            //更新未读记录
            messageService.updateHasRead(1,conversationId,hostHolder.getUser().getId());
        }catch (Exception e){
            logger.error("获取详情失败:"+e.getMessage());
        }
        return "letterDetail";
    }

    @RequestMapping(path = {"/msg/addMessage"},method = {RequestMethod.POST})
    @ResponseBody
    public String addMessage(@RequestParam("toName") String toName,
                             @RequestParam("content") String content){
        try{
            if(hostHolder.getUser() == null)
                return MyUtils.getJSONString(999,"未登录!");

            User user = userService.getUserByName(toName);
            if(user==null)
                return MyUtils.getJSONString(1,"用户不存在！");

            Message message = new Message();
            message.setFromId(hostHolder.getUser().getId());
            message.setToId(user.getId());
            message.setContent(content);
            message.setCreatedDate(new Date());
            messageService.addMessage(message);
            return MyUtils.getJSONString(0);

        }catch (Exception e){
            logger.error("私信错误："+e.getMessage());
            return MyUtils.getJSONString(1,"发信失败！");
        }
    }
}
