package com.appsdeveloperblog.app.ws.ui.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
//will recive http request

import com.appsdeveloperblog.app.ws.service.UserService;
import com.appsdeveloperblog.app.ws.shared.dto.UserDto;
import com.appsdeveloperblog.app.ws.ui.model.request.UserDetailsRequestModel;
import com.appsdeveloperblog.app.ws.ui.model.response.UserRest;

@RestController
@RequestMapping("users") //http://localhost:8080/users
public class UserController {

	@Autowired
	UserService userService;

	@GetMapping
	public String getUser() {
		return "get user called";
	}
	
	@PostMapping
	// request body annoatation will convert incoming request json into JAVA OBJECT
	public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) {
		UserRest returnValue = new UserRest();
		UserDto userDto = new UserDto();
		//copy userdetails in to user dto as , user dto will used in all layers
		BeanUtils.copyProperties(userDetails, userDto);
		// create new user ,call service layer which performs bussiness login in service
		UserDto createdUser = userService.createUser(userDto);
		// this is gonna restruned to UI , java object gonna to retiurn to UI layer
		BeanUtils.copyProperties(createdUser, returnValue);
		return returnValue;
	}
	
	@PutMapping
	public String updateUser() {
		return "update user called";
	}
	
	@DeleteMapping
	public String deletetUser() {
		return "delete user called";
	}
}