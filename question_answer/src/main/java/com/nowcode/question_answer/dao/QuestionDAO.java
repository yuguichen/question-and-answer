package com.nowcode.question_answer.dao;

import com.nowcode.question_answer.model.Question;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface QuestionDAO {
    String TABLE_NAME = " question ";
    String INSERT_FIELDS = " title, content, created_date, user_id, comment_count ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Insert({"insert into",TABLE_NAME,"(title,content,created_date,user_id,comment_count)" +
            "values (#{title},#{content},#{createdDate},#{userID},#{commentCount})"})
    int addQuestion(Question question);

    @Select({"select",SELECT_FIELDS,"from",TABLE_NAME," where id = #{id}"})
    Question selectById(int id);

    //xml配置
    List<Question> selectLatestQuestions(@Param("userID") int userID,
                                         @Param("offset") int offset,
                                         @Param("limit") int limit);
}
