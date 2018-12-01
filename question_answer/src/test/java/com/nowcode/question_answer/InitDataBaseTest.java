package com.nowcode.question_answer;

import com.nowcode.question_answer.dao.QuestionDAO;
import com.nowcode.question_answer.dao.UserDAO;
import com.nowcode.question_answer.model.Question;
import com.nowcode.question_answer.model.User;
import com.nowcode.question_answer.utils.SaltUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;
import java.util.Random;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = QuestionAnswerApplication.class)
//@Sql("/init-schema.sql")
public class InitDataBaseTest {

	@Autowired
	UserDAO userDAO ;
	@Autowired
	QuestionDAO questionDAO;

	@Test
	public void initDatabase(){
		Random random = new Random();

		for(int i=20;i<=31;i++){
//			User user = new User();
//			user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", random.nextInt(1000)));
//			user.setUsername(String.format("USER%d", i));
//			user.setPassword("");
//			user.setSalt("");
//			userDAO.addUser(user);


			Question question = new Question();
			question.setCommentCount(i);
			Date date = new Date();
			date.setTime(date.getTime()+1000*3600*i);
			question.setCreatedDate(date);
			question.setUserID(i+1);
			question.setTitle(String.format("TITLE{%d}",i));
			question.setContent("hhhhhhhhhhhhhhhhhhhhhhhhhh");
			questionDAO.addQuestion(question);
		}
	}

	@Test
	public void updateTest(){
		//Random random = new Random();
		List<User> userList = userDAO.findAllUser();
		for(User user:userList){
			//user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", random.nextInt(1000)));
			if(user.getUsername().equals("ygc")){
				user.setPassword(SaltUtil.MD5("ygc"+user.getSalt()));
				userDAO.updatePassword(user);
			}

		}
	}

	@Test
	public void selectTest(){
		//org.apache.ibatis.binding.BindingException:
		// 一般的原因是Mapper interface和xml文件的定义对应不上
//		List<Question> questionList = questionDAO.selectLatestQuestions(0,0,10);
//		System.out.println(questionList);
		List<Question> questionList = questionDAO.selectLatestQuestions(0, 0, 10);
		for(Question question:questionList){
			System.out.println(question.getUserID()+":"+question.getCommentCount());
		}
	}

	@Test
	public void contextLoads() {
		Random random = new Random();
		for (int i = 0; i < 11; ++i) {
			User user = new User();
			user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", random.nextInt(1000)));
			user.setUsername(String.format("USER%d", i));
			user.setPassword("newpassword");
			user.setSalt("");
			userDAO.addUser(user);

			Question question = new Question();
			question.setCommentCount(i);
			Date date = new Date();
			date.setTime(date.getTime() + 1000 * 3600 * 5 * i);
			question.setCreatedDate(date);
			question.setUserID(i + 1);
			question.setTitle(String.format("TITLE{%d}", i));
			question.setContent(String.format("Balaababalalalal Content %d", i));
			questionDAO.addQuestion(question);
		}

	}


}
