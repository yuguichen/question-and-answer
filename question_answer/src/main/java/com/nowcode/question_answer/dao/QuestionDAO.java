package com.nowcode.question_answer.dao;

import com.nowcode.question_answer.model.Question;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface QuestionDAO {
    String TABLE_NAME = " question ";
    @Insert({"insert into",TABLE_NAME,"(title,content,created_date,user_id,comment_count)" +
            "values (#{title},#{content},#{createdDate},#{userID},#{commentCount})"})
    int addQuestion(Question question);

    //xml配置
    List<Question> selectLatestQuestions(@Param("userID") int userID,
                                         @Param("offset") int offset,
                                         @Param("limit") int limit);
}
