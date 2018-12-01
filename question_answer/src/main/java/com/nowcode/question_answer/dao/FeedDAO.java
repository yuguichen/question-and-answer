package com.nowcode.question_answer.dao;

import com.nowcode.question_answer.model.Feed;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FeedDAO {
    String TABLE_NAME = " feed ";
    String INSERT_FIELDS = " created_date, user_id, data, type ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Insert({"insert into",TABLE_NAME,"(",INSERT_FIELDS,")" +
            "values (#{createdDate},#{userId},#{data},#{type})"})
    int addFeed(Feed feed);

    @Select({"select",SELECT_FIELDS," from ",TABLE_NAME," where id = #{id}"})
    Feed getFeedById(int id);

    //xml配置
    List<Feed> selectUserFeeds(@Param("maxId") int maxId,
                               @Param("userIds") List<Integer> userIds,
                               @Param("count") int count);
}
