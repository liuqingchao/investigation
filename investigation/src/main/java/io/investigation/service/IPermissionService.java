package io.investigation.service;

import java.util.List;

import io.investigation.model.Permission;

public interface IPermissionService {
	List<Permission> selectPermissionsByUser(Integer userId);
}
