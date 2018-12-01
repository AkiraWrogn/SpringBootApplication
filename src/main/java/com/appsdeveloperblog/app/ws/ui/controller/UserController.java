package com.appsdeveloperblog.app.ws.ui.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
//will recive http request

import com.appsdeveloperblog.app.ws.service.UserService;
import com.appsdeveloperblog.app.ws.shared.dto.UserDto;
import com.appsdeveloperblog.app.ws.ui.model.request.RequestOperationName;
import com.appsdeveloperblog.app.ws.ui.model.request.UserDetailsRequestModel;
import com.appsdeveloperblog.app.ws.ui.model.response.ErrorMessages;
import com.appsdeveloperblog.app.ws.ui.model.response.OperationStatusModel;
import com.appsdeveloperblog.app.ws.ui.model.response.RequestOperationStatus;
import com.appsdeveloperblog.app.ws.ui.model.response.UserRest;

@RestController
@RequestMapping("users") // http://localhost:8080/users
public class UserController {

	@Autowired
	UserService userService;

	// byusing @pathvariable id given in path will be availble in @pathvariable
	@GetMapping(path = "/{id}",
			produces = {org.springframework.http.MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
	
	public UserRest getUser(@PathVariable String id) {
		UserRest returnValue = new UserRest();
		UserDto userDto = userService.getUserById(id);
		BeanUtils.copyProperties(userDto, returnValue);
		return returnValue;
	}

	@PostMapping(
			consumes = {org.springframework.http.MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE},
			produces = {org.springframework.http.MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE}
			)
	// request body annoatation will convert incoming request json into JAVA OBJECT
	public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) throws Exception{
		UserRest returnValue = new UserRest();
		//throw custom exception if first name not provided
		if(userDetails.getFirstName().isEmpty()) throw new Exception(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessages());
		if(userDetails.getLastName().isEmpty()) throw new NullPointerException("the object is null");
		UserDto userDto = new UserDto();
		// copy userdetails in to user dto as , user dto will used in all layers
		BeanUtils.copyProperties(userDetails, userDto);
		// create new user ,call service layer which performs bussiness login in service
		UserDto createdUser = userService.createUser(userDto);
		// this is gonna restruned to UI , java object gonna to retiurn to UI layer
		BeanUtils.copyProperties(createdUser, returnValue);
		return returnValue;
	}

	@PutMapping(path = "/{id}",
			consumes = {org.springframework.http.MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE},
			produces = {org.springframework.http.MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE}
			)
	public UserRest updateUser(@PathVariable String id,@RequestBody UserDetailsRequestModel userDetails) throws Exception {
		UserRest returnValue = new UserRest();
		//throw custom exception if first name not provided
		if(userDetails.getFirstName().isEmpty()) throw new Exception(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessages());
		if(userDetails.getLastName().isEmpty()) throw new NullPointerException("the object is null");
		UserDto userDto = new UserDto();
		// copy userdetails in to user dto as , user dto will used in all layers
		BeanUtils.copyProperties(userDetails, userDto);
		// create new user ,call service layer which performs bussiness login in service
		UserDto updatedUser = userService.updatedUser(id,userDto);
		// this is gonna restruned to UI , java object gonna to retiurn to UI layer
		BeanUtils.copyProperties(updatedUser, returnValue);
		return returnValue;	}

	@DeleteMapping(path = "/{id}",
			produces = {org.springframework.http.MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE}
			)
	public OperationStatusModel deletetUser(@PathVariable String id) {
		OperationStatusModel returnValue = new OperationStatusModel();
		userService.deleteUser(id);
		returnValue.setOperationName(RequestOperationName.DELETE.name());
		returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
		return returnValue;
	}
}
