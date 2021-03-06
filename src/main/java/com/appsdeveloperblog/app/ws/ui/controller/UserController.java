package com.appsdeveloperblog.app.ws.ui.controller;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
//will recive http request

import com.appsdeveloperblog.app.ws.service.AddressService;
import com.appsdeveloperblog.app.ws.service.UserService;
import com.appsdeveloperblog.app.ws.shared.dto.AddressDto;
import com.appsdeveloperblog.app.ws.shared.dto.UserDto;
import com.appsdeveloperblog.app.ws.ui.model.request.RequestOperationName;
import com.appsdeveloperblog.app.ws.ui.model.request.UserDetailsRequestModel;
import com.appsdeveloperblog.app.ws.ui.model.response.AddressRest;
import com.appsdeveloperblog.app.ws.ui.model.response.ErrorMessages;
import com.appsdeveloperblog.app.ws.ui.model.response.OperationStatusModel;
import com.appsdeveloperblog.app.ws.ui.model.response.RequestOperationStatus;
import com.appsdeveloperblog.app.ws.ui.model.response.UserRest;
import java.lang.reflect.*;

@RestController
@RequestMapping("users") // http://localhost:8080/users
public class UserController {

	@Autowired
	UserService userService;

	@Autowired
	AddressService addressesService;

	// byusing @pathvariable id given in path will be availble in @pathvariable
	@GetMapping(path = "/{id}", produces = { org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })

	public UserRest getUser(@PathVariable String id) {
		UserRest returnValue = new UserRest();
		UserDto userDto = userService.getUserById(id);
		BeanUtils.copyProperties(userDto, returnValue);
		return returnValue;
	}

	@PostMapping(consumes = { org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE }, produces = { org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
					MediaType.APPLICATION_XML_VALUE })
	// request body annoatation will convert incoming request json into JAVA OBJECT
	public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) throws Exception {
		UserRest returnValue = new UserRest();
		// throw custom exception if first name not provided
		if (userDetails.getFirstName().isEmpty())
			throw new Exception(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessages());

		if (userDetails.getLastName().isEmpty())
			throw new NullPointerException("the object is null");
		ModelMapper mapper = new ModelMapper();
		// copy userdetails in to user dto as , user dto will used in all layers
		UserDto userDto = mapper.map(userDetails, UserDto.class);
		// create new user ,call service layer which performs bussiness login in service
		UserDto createdUser = userService.createUser(userDto);
		// this is gonna restruned to UI , java object gonna to retiurn to UI layer
		returnValue = mapper.map(createdUser, UserRest.class);
		return returnValue;
	}

	@PutMapping(path = "/{id}", consumes = { org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE }, produces = { org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
					MediaType.APPLICATION_XML_VALUE })
	public UserRest updateUser(@PathVariable String id, @RequestBody UserDetailsRequestModel userDetails)
			throws Exception {
		UserRest returnValue = new UserRest();
		// throw custom exception if first name not provided
		if (userDetails.getFirstName().isEmpty())
			throw new Exception(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessages());
		if (userDetails.getLastName().isEmpty())
			throw new NullPointerException("the object is null");
		/*
		 * UserDto userDto = new UserDto(); // copy userdetails in to user dto as , user
		 * dto will used in all layers BeanUtils.copyProperties(userDetails, userDto);
		 */

		// now we will replace bean utils with model mapper (as model mapper is good
		// with object conatins object and bena utils are not so good

		ModelMapper mapper = new ModelMapper();
		UserDto userDto = mapper.map(userDetails, UserDto.class);

		// create new user ,call service layer which performs bussiness login in service
		UserDto updatedUser = userService.updatedUser(id, userDto);
		// this is gonna restruned to UI , java object gonna to retiurn to UI layer
		BeanUtils.copyProperties(updatedUser, returnValue);
		return returnValue;
	}

	@DeleteMapping(path = "/{id}", produces = { org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	public OperationStatusModel deletetUser(@PathVariable String id) {
		OperationStatusModel returnValue = new OperationStatusModel();
		userService.deleteUser(id);
		returnValue.setOperationName(RequestOperationName.DELETE.name());
		returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
		return returnValue;
	}

	// pageno = pagesize starting from 0
	// get all user pagination, by default page offset = 25 pagesize and pageoffset
	// as querystrin
	@GetMapping(produces = { org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	public List<UserRest> getAllUsers(@RequestParam(value = "pagesize", defaultValue = "0") int pagesize,
			@RequestParam(value = "pageoffset", defaultValue = "25") int pageoffset) {
		List<UserRest> returnValue = new ArrayList<>();
		List<UserDto> users = userService.getUsers(pagesize, pageoffset);

		for (UserDto user : users) {
			UserRest userModel = new UserRest();
			BeanUtils.copyProperties(user, userModel);
			returnValue.add(userModel);
		}
		return returnValue;
	}

	// localhost:80808/users/id/addresses/ - will give list of address
	@GetMapping(path = "/{id}/addresses", produces = { org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })

	public List<AddressRest> getUserAddresses(@PathVariable String id) {
		List<AddressRest> returnValue = new ArrayList<>();
		List<AddressDto> addressDto = addressesService.getAddresses(id);
		if (addressDto != null && !addressDto.isEmpty()) {
			ModelMapper modelMapper = new ModelMapper();
			Type listType = new TypeToken<List<AddressRest>>() {
			}.getType();
			returnValue = modelMapper.map(addressDto, listType);
		}
		return returnValue;
	}

	// localhost:80808/users/id/addresses/addressId - will give list of address
	@GetMapping(path = "/{id}/addresses/{addressId}", produces = {
			org.springframework.http.MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })

	public AddressRest getUserAddress(@PathVariable String addressId) {
		AddressRest returnValue = new AddressRest();
		AddressDto addressDto = addressesService.getAddress(addressId);
		ModelMapper modelMapper = new ModelMapper();
		returnValue = modelMapper.map(addressDto, AddressRest.class);
		return returnValue;
	}
}
