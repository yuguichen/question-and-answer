package com.nowcode.question_answer.service;

import com.nowcode.question_answer.dao.MessageDAO;
import com.nowcode.question_answer.model.Message;
import com.nowcode.question_answer.utils.MyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class MessageService {
    @Autowired
    MessageDAO messageDAO;
    @Autowired
    SensitiveService sensitiveService;

    public int addMessage(Message message){
        message.setContent(sensitiveService.filter( HtmlUtils.htmlEscape(message.getContent()) ));
        return messageDAO.addMessage(message);
    }

    public List<Message> getMessagerByConversationId(String conversationId, int offset, int limit){
        return messageDAO.getMessagerByConversationId(conversationId,offset,limit);
    }

    public List<Message> getConversationList(int userId, int offset, int limit){
        return messageDAO.getConversationList(userId,offset,limit);
    }

    public int getConversationUnreadCount(int userId,String conversationId){
        return messageDAO.getConversationUnreadCount(userId,conversationId);
    }
}
