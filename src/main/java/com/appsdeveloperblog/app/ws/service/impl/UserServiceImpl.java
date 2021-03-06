package com.appsdeveloperblog.app.ws.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.appsdeveloperblog.app.ws.exception.UserServiceException;
import com.appsdeveloperblog.app.ws.io.entity.UserEntity;
import com.appsdeveloperblog.app.ws.io.repositories.UserRepository;
import com.appsdeveloperblog.app.ws.service.UserService;
import com.appsdeveloperblog.app.ws.shared.Utils;
import com.appsdeveloperblog.app.ws.shared.dto.AddressDto;
import com.appsdeveloperblog.app.ws.shared.dto.UserDto;
import com.appsdeveloperblog.app.ws.ui.model.response.ErrorMessages;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	Utils utils;
	
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;
	@Override
	public UserDto createUser(UserDto user) {
		
		UserEntity storeUserEmail = userRepository.findByEmail(user.getEmail());
		if(storeUserEmail !=null) throw new RuntimeException("Duplicate email");
		
		for(int i=0;i<user.getAddresses().size();i++) {
			AddressDto address = user.getAddresses().get(i);
			address.setUserDetails(user);
			address.setAddressId(utils.generateAddressId(30));
		}
		
		ModelMapper mapper = new ModelMapper();
		UserEntity userEntity = mapper.map(user, UserEntity.class);
		
		String PublicUserId = utils.generateUserId(30);
		// hard coding the non nullable fields which are not part of json
		userEntity.setUserId(PublicUserId);
		userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		
		UserEntity storedUserDetails = userRepository.save(userEntity);
		
		UserDto returnValue = mapper.map(storedUserDetails, UserDto.class);
		return returnValue;
	}
	
	@Override
	
	public UserDto getUser(String email) {
		UserEntity userEntity = userRepository.findByEmail(email);
		if(userEntity == null)throw new UsernameNotFoundException(email);
		UserDto returnValue = new UserDto();
		BeanUtils.copyProperties(userEntity, returnValue);
		return returnValue;
	}
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		UserEntity userEntity = userRepository.findByEmail(email);
		if(userEntity == null)throw new UsernameNotFoundException(email);
		//return user results
		return new User(userEntity.getEmail(),userEntity.getEncryptedPassword(),new ArrayList<>());
	}

	@Override
	public UserDto getUserById(String userId) {
		UserDto returnValue = new UserDto();
		//userRepository return userentity 
		UserEntity entity = userRepository.findByUserId(userId);
		if(entity == null)throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessages());
		BeanUtils.copyProperties(entity, returnValue);
		return returnValue;
	}

	@Override
	public UserDto updatedUser(String userId,UserDto user) {
		UserDto returnValue = new UserDto();
		//userRepository return userentity 
		UserEntity userEntity = userRepository.findByUserId(userId);
		//can through this exception or custom exception , i will thorw custom one commenting spring exception
		//if(userEntity == null)throw new UsernameNotFoundException(email);
		if(userEntity == null)throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessages());
		userEntity.setFirstName(user.getFirstName());
		userEntity.setLastName(user.getLastName());
		
		UserEntity updatedUser = userRepository.save(userEntity);
		BeanUtils.copyProperties(updatedUser, returnValue);
		
		return returnValue;
	}

	@Override
	public void deleteUser(String userId) {
		UserDto returnValue = new UserDto();
		//userRepository return userentity 
		UserEntity userEntity = userRepository.findByUserId(userId);
		if(userEntity == null)throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessages());
		userRepository.delete(userEntity);
		
	}

	@Override
	public List<UserDto> getUsers(int pagesize, int pageoffset) {
		List<UserDto> returnValue = new ArrayList<>();
		
		//since page is startting as 0, we want it to start it as 1
		if(pagesize>0) pagesize = pagesize-1;
		Pageable pageableRequest = PageRequest.of(pagesize, pageoffset);
		Page<UserEntity> usersPages = userRepository.findAll(pageableRequest);
		List<UserEntity> users = usersPages.getContent();
		
		for(UserEntity user:users)
		{
			UserDto userModel = new UserDto();
			BeanUtils.copyProperties(user,userModel);
			returnValue.add(userModel);
		}
		return returnValue;
	}

}
