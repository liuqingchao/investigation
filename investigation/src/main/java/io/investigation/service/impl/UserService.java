package io.investigation.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.investigation.mapper.UserMapper;
import io.investigation.model.User;
import io.investigation.service.IUserService;

@Service
public class UserService implements IUserService {
	@Autowired
	private UserMapper userDao;
	
	public UserMapper getUserDao() {
		return userDao;
	}

	public void setUserDao(UserMapper userDao) {
		this.userDao = userDao;
	}

	public boolean insert(User model) {
        return userDao.insert(model) > 0;
    }

    public User select(int id) {
        return userDao.selectById(id);
    }

    public List<User> selectAll() {
        return userDao.selectList(null);
    }

    public boolean updateValue(User model) {
        return userDao.updateById(model) > 0;
    }

    public boolean delete(Integer id) {
        return userDao.deleteById(id) > 0;
    }

	@Override
	public User selectByName(String userName) {
		User user = new User();
		user.setUserName(userName);
		return userDao.selectOne(user);
	}
}
