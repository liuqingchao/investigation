package io.investigation.service;

import java.util.List;

import io.investigation.model.Role;

public interface IRoleService {
	List<Role> selectRolesByUser(Integer userId);
}
