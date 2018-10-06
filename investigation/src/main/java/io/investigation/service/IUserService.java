package io.investigation.service;

import java.util.List;

import io.investigation.model.User;

public interface IUserService {
	boolean insert(User model);

    User select(int id);
    
    User selectByName(String userName);

    List<User> selectAll();

    boolean updateValue(User model);

    boolean delete(Integer id);
}
