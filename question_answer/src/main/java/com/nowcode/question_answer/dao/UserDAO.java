package com.nowcode.question_answer.dao;

import com.nowcode.question_answer.model.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
//org.springframework.jdbc.BadSqlGrammarException:
// 注意sql语句一定要写对，逗号用英文的，空格不能少
public interface UserDAO {
    String TABLE_NAME = " user ";//注意空格

    @Insert({"insert into",TABLE_NAME,"( username,password,salt,head_url ) " +
            "values(#{username},#{password},#{salt},#{headUrl})"})
    int addUser(User user);

    @Select({"select * from",TABLE_NAME })
    List<User> findAllUser();

    @Select({"select * from",TABLE_NAME,"where id = #{id}"})
    User findUserById(int id);

    @Select({"select * from",TABLE_NAME,"where username = #{username}"})
    User findUserByName(String username);

    @Update({"update",TABLE_NAME,"set head_url = #{headUrl} where id = #{id}"})
    int updateUrl(User user);

    @Update({"update",TABLE_NAME,"set password = #{password} where id = #{id}"})
    int updatePassword(User user);

    @Update({"update",TABLE_NAME,"set salt = #{salt} where id = #{id}"})
    int updateSalt(User user);

    @Delete({"delete * from",TABLE_NAME,"where id = #{id}"})
    void deleteUserById(User user);


}
