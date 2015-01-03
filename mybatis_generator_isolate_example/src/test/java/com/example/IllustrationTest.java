/**
 * 
 */
package com.example;

import org.apache.ibatis.session.SqlSession;
import org.junit.Assert;
import org.junit.Test;

import com.example.domain.User;
import com.example.mapper.UserMapper;

/**
 * @author hlw
 * 
 */
public class IllustrationTest extends MyBatisTestCase {
	@Test
	public void testBasicUsage() {
		SqlSession session = sqlSessionFactory.openSession();
		UserMapper mapper = session.getMapper(UserMapper.class);

		User entity = mapper.selectByPrimaryKeyWithRoles(1l);
		Assert.assertNotNull(entity);
		Assert.assertEquals("Jack", entity.getName());
		Assert.assertEquals("管理员", entity.getRoles().get(0).getName());
		session.commit();
		session.close();
	}
}
