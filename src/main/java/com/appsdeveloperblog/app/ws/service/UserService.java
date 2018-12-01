package com.appsdeveloperblog.app.ws.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.appsdeveloperblog.app.ws.shared.dto.UserDto;

public interface UserService extends UserDetailsService {
	UserDto createUser(UserDto user);
	UserDto getUser(String email);
	UserDto getUserById(String userId);
	UserDto updatedUser(String userId,UserDto userDto);
	void deleteUser(String userId);
	List<UserDto> getUsers(int pagesize, int pageoffset);
}
