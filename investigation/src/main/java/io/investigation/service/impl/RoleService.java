package io.investigation.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;

import io.investigation.mapper.RoleMapper;
import io.investigation.mapper.UserRoleMapper;
import io.investigation.model.Role;
import io.investigation.model.UserRole;
import io.investigation.service.IRoleService;

@Service
public class RoleService implements IRoleService {
	@Autowired
	private RoleMapper roleDao;
	@Autowired
	private UserRoleMapper userRoleDao;

	@Override
	public List<Role> selectRolesByUser(Integer userId) {
		List<UserRole> userRoles = userRoleDao.selectList(new EntityWrapper<UserRole>().eq("userId", userId));
		List<Integer> roleIds = userRoles.stream().map(ur -> ur.getRoleId()).collect(Collectors.toList());
		return roleDao.selectBatchIds(roleIds);
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

}
