package com.appsdeveloperblog.app.ws.service;

import java.util.List;

import com.appsdeveloperblog.app.ws.shared.dto.AddressDto;

public interface AddressService {

	public List<AddressDto> getAddresses(String userId);

	public AddressDto getAddress(String addressId);
}
