package io.investigation.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;

import io.investigation.model.User;
import io.investigation.response.BaseResponse;
import io.investigation.service.IUserService;
import io.investigation.utils.JWTUtil;

@RestController
@RequestMapping("/user")
public class UserController {
	@Autowired
	private Producer kaptchaProducer;
	@Autowired
    private MessageSource messageSource;
	@Autowired
	private IUserService userService;

	@GetMapping(value = "/kaptcha")
	public ModelAndView getCaptchaImage(HttpServletRequest request, HttpServletResponse response) {
		response.setDateHeader("Expires", 0);
		response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
		response.addHeader("Cache-Control", "post-check=0, pre-check=0");
		response.setHeader("Pragma", "no-cache");
		response.setContentType("image/jpeg");
		String capText = kaptchaProducer.createText();

		request.getSession().setAttribute(Constants.KAPTCHA_SESSION_KEY, capText);
		BufferedImage bi = kaptchaProducer.createImage(capText);

		try (ServletOutputStream out = response.getOutputStream()) {
			ImageIO.write(bi, "jpg", out);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@PostMapping("/login")
	public BaseResponse login(@RequestParam("username") String username, @RequestParam("password") String password,
			@RequestParam("kaptcha") String kaptcha,
			HttpServletRequest request) {
		BaseResponse result = new BaseResponse();
		String kaptchaExpected = (String) request.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY);
		if (kaptchaExpected == null || !kaptchaExpected.equals(kaptcha)) {
			result.setCode(10000);
			result.setMsg(messageSource.getMessage("errors.login.InvalidKaptcha", null, request.getLocale()));
			return result;
		}

		User user = userService.selectByName(username);
		if (user.getPassword().equals(password)) {
			return new BaseResponse("Login success", JWTUtil.sign(username, password));
		} else {
			throw new UnauthorizedException();
		}
	}

	@GetMapping("/article")
	public BaseResponse article() {
		Subject subject = SecurityUtils.getSubject();
		if (subject.isAuthenticated()) {
			return new BaseResponse(200, "You are already logged in", null);
		} else {
			return new BaseResponse(200, "You are guest", null);
		}
	}

	@GetMapping("/require_auth")
	@RequiresAuthentication
	public BaseResponse requireAuth() {
		return new BaseResponse(200, "You are authenticated", null);
	}

	@GetMapping("/require_role")
	@RequiresRoles("admin")
	public BaseResponse requireRole() {
		return new BaseResponse(200, "You are visiting require_role", null);
	}

	@GetMapping("/require_permission")
	@RequiresPermissions(logical = Logical.AND, value = { "view", "edit" })
	public BaseResponse requirePermission() {
		return new BaseResponse(200, "You are visiting permission require edit,view", null);
	}

	@RequestMapping(path = "/401")
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public BaseResponse unauthorized() {
		return new BaseResponse(401, "Unauthorized", null);
	}

	@GetMapping("/demos")
	public List<User> allDemo() {
		List<User> list = userService.selectAll();
		for (User u : list) {
			System.out.println("**********" + u.getUserId());
		}
		return userService.selectAll();
	}

	public IUserService getUserService() {
		return userService;
	}

	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	public Producer getKaptchaProducer() {
		return kaptchaProducer;
	}

	public void setKaptchaProducer(Producer kaptchaProducer) {
		this.kaptchaProducer = kaptchaProducer;
	}

	public MessageSource getMessageSource() {
		return messageSource;
	}

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
}
