package io.investigation.service;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.investigation.model.Permission;
import io.investigation.model.Role;
import io.investigation.model.User;
import io.investigation.utils.JWTToken;
import io.investigation.utils.JWTUtil;

@Service
public class MyRealm extends AuthorizingRealm {
	@Autowired
	private IUserService userService;
	@Autowired
	private IRoleService roleService;
	@Autowired
	private IPermissionService permissionService;
	/**
     * 大坑！，必须重写此方法，不然Shiro会报错
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }
    /**
     * 只有当需要检测用户权限的时候才会调用此方法，例如checkRole,checkPermission之类的
     */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		String username = JWTUtil.getUsername(principals.toString());
        User user = userService.selectByName(username);
        List<Role> roles = roleService.selectRolesByUser(user.getUserId());
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.addRoles(roles.stream().map(r -> r.getRoleName()).collect(Collectors.toList()));
        List<Permission> permissions = permissionService.selectPermissionsByUser(user.getUserId());
        simpleAuthorizationInfo.addStringPermissions(permissions.stream().map(r -> r.getResourceUrl()).collect(Collectors.toList()));
        return simpleAuthorizationInfo;
	}
	/**
     * 默认使用此方法进行用户名正确与否验证，错误抛出异常即可。
     */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) throws AuthenticationException {
		String token = (String) auth.getCredentials();
        // 解密获得username，用于和数据库进行对比
        String username = JWTUtil.getUsername(token);
        if (username == null) {
            throw new AuthenticationException("token invalid");
        }

        User user = userService.selectByName(username);
        if (user == null) {
            throw new AuthenticationException("User didn't existed!");
        }

        if (! JWTUtil.verify(token, username, user.getPassword())) {
            throw new AuthenticationException("Username or password error");
        }
        return new SimpleAuthenticationInfo(token, token, "my_realm");
	}

	public IUserService getUserService() {
		return userService;
	}

	public void setUserService(IUserService userService) {
		this.userService = userService;
	}
	public IRoleService getRoleService() {
		return roleService;
	}
	public void setRoleService(IRoleService roleService) {
		this.roleService = roleService;
	}
	public IPermissionService getPermissionService() {
		return permissionService;
	}
	public void setPermissionService(IPermissionService permissionService) {
		this.permissionService = permissionService;
	}

}
