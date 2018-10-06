package io.investigation.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;

import io.investigation.mapper.PermissionMapper;
import io.investigation.mapper.RoleMapper;
import io.investigation.mapper.RolePermissionMapper;
import io.investigation.mapper.UserRoleMapper;
import io.investigation.model.Permission;
import io.investigation.model.RolePermission;
import io.investigation.model.UserRole;
import io.investigation.service.IPermissionService;

@Service
public class PermissionService implements IPermissionService {
	@Autowired
	private RoleMapper roleDao;
	@Autowired
	private UserRoleMapper userRoleDao;
	@Autowired
	private PermissionMapper permissionDao;
	@Autowired
	private RolePermissionMapper rolePermissionDao;

	@Override
	public List<Permission> selectPermissionsByUser(Integer userId) {
		List<UserRole> userRoles = userRoleDao.selectList(new EntityWrapper<UserRole>().eq("userId", userId));
		List<Integer> roleIds = userRoles.stream().map(ur -> ur.getRoleId()).collect(Collectors.toList());
		List<RolePermission> rolePermissions = rolePermissionDao.selectList(new EntityWrapper<RolePermission>().in("roleId", roleIds));
		List<Integer> permissionIds = rolePermissions.stream().map(ur -> ur.getPermissionId()).collect(Collectors.toList());
		return permissionDao.selectBatchIds(permissionIds);
	}

	public RoleMapper getRoleDao() {
		return roleDao;
	}

	public void setRoleDao(RoleMapper roleDao) {
		this.roleDao = roleDao;
	}

	public UserRoleMapper getUserRoleDao() {
		return userRoleDao;
	}

	public void setUserRoleDao(UserRoleMapper userRoleDao) {
		this.userRoleDao = userRoleDao;
	}

	public PermissionMapper getPermissionDao() {
		return permissionDao;
	}

	public void setPermissionDao(PermissionMapper permissionDao) {
		this.permissionDao = permissionDao;
	}

	public RolePermissionMapper getRolePermissionDao() {
		return rolePermissionDao;
	}

	public void setRolePermissionDao(RolePermissionMapper rolePermissionDao) {
		this.rolePermissionDao = rolePermissionDao;
	}

}
