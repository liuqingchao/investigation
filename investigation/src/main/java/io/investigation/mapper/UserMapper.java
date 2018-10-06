package io.investigation.mapper;

import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import io.investigation.model.User;

@Component
public interface UserMapper extends BaseMapper<User> {
}
