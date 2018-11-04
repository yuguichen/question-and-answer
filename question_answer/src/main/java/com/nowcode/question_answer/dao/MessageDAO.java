package com.nowcode.question_answer.dao;

import com.nowcode.question_answer.model.Message;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MessageDAO {
    String TABLE_NAME = " message ";
    String INSERT_FIELDS = " from_id, to_id, content, created_date, has_read, conversation_id ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Insert({"insert into",TABLE_NAME,"(",INSERT_FIELDS,
            ") values(#{fromId},#{toId},#{content},#{createdDate},#{hasRead},#{conversationId})"})
    int addMessage(Message message);

    @Update({"update",TABLE_NAME,"set has_read=#{hasRead} where conversation_id=#{conversationId} and to_id=#{userId} and has_read=0"})
    void updateHasRead(@Param("hasRead") int hasRead,
                       @Param("conversationId") String conversationId,
                       @Param("userId") int userId);

    @Select({"select",SELECT_FIELDS,"from",TABLE_NAME,
            "where conversation_id=#{conversationId} order by created_date desc limit #{offset},#{limit}"})
    List<Message> getMessagerByConversationId(@Param("conversationId") String conversationId,
                                              @Param("offset") int offset,
                                              @Param("limit") int limit);

    //未读信息数
    @Select({"select count(id) from",TABLE_NAME,
            "where conversation_id=#{conversationId} and has_read=0 and to_id=#{userId}"})
    int getConversationUnreadCount(@Param("userId") int userId,
                                   @Param("conversationId") String conversationId);


    @Select({"select ",INSERT_FIELDS,",count(id) as id from (select * from ",TABLE_NAME,
            "where from_id=#{userId} or to_id=#{userId} order by id desc) tt group by conversation_id order by created_date desc limit #{offset}, #{limit}"})
    List<Message> getConversationList(@Param("userId") int userId,
                                      @Param("offset") int offset,
                                      @Param("limit") int limit);

}
