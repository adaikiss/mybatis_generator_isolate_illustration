/**
 * 
 */
package com.example;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.junit.Assert;
import org.junit.Test;

import com.example.domain.User;
import com.example.domain.UserExample;
import com.example.mapper.UserMapper;

/**
 * @author hlw
 * 
 */
public class OverloadTest extends MyBatisTestCase {

	@Test
	public void testWithOutRowBounds() {
		SqlSession session = sqlSessionFactory.openSession();
		UserMapper mapper = session.getMapper(UserMapper.class);
		List<User> list = mapper.selectByExample(new UserExample());
		Assert.assertEquals(6, list.size());
		User entity = list.get(0);
		Assert.assertEquals("Jack", entity.getName());
		session.commit();
		session.close();
	}

	@Test
	public void testWithRowBounds() {
		SqlSession session = sqlSessionFactory.openSession();
		UserMapper mapper = session.getMapper(UserMapper.class);
		List<User> list = mapper.selectByExample(new UserExample(), new RowBounds(2, 2));
		Assert.assertEquals(2, list.size());
		User entity = list.get(0);
		Assert.assertEquals("Jack1", entity.getName());
		session.commit();
		session.close();
	}
}
