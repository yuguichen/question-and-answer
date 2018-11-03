package com.nowcode.question_answer.dao;

import com.nowcode.question_answer.model.Message;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MessageDAO {
    String TABLE_NAME = " message ";
    String INSERT_FIELDS = " from_id, to_id, content, created_date, has_read, conversation_id ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Insert({"insert into",TABLE_NAME,"(",INSERT_FIELDS,
            ") values(#{fromId},#{toId},#{content},#{createdDate},#{hasRead},#{conversationId})"})
    int addMessage(Message message);

    @Select({"select",SELECT_FIELDS,"from",TABLE_NAME,
            "where conversation_id=#{conversationId} order by created_date desc limit #{offset},#{limit}"})
    List<Message> getMessagerByConversationId(@Param("conversationId") String conversationId,
                                              @Param("offset") int offset, @Param("limit") int limit);

    //未读信息数
    @Select({"select",SELECT_FIELDS,"from",TABLE_NAME,
            "where conversation_id=#{conversationId} and has_read=0 and to_id=#{userId}"})
    int getConversationUnreadCount(@Param("userId") int userId,
                                   @Param("conversationId") String conversationId);


}
