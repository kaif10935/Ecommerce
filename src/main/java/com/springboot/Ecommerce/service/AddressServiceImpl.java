package com.springboot.Ecommerce.service;

import com.springboot.Ecommerce.exceptions.ResourceNotFoundException;
import com.springboot.Ecommerce.model.Address;
import com.springboot.Ecommerce.model.User;
import com.springboot.Ecommerce.payload.AddressDTO;
import com.springboot.Ecommerce.repository.AddressRepository;
import com.springboot.Ecommerce.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public AddressDTO createAddress(AddressDTO addressDTO, User user) {
        Address address = modelMapper.map(addressDTO, Address.class);
        List<Address> addressList = user.getAddresses();
        addressList.add(address);
        user.setAddresses(addressList);
        address.setUser(user);
        Address savedAddress = addressRepository.save(address);
        return modelMapper.map(savedAddress,AddressDTO.class);
    }

    @Override
    public List<AddressDTO> getAddresses() {
        List<Address> addresses = addressRepository.findAll();
        return addresses.stream().map(address -> modelMapper.map(address,AddressDTO.class)).toList();
    }

    @Override
    public AddressDTO getAddress(Long addressId) {
        Optional<Address> address = addressRepository.findById(addressId);
        if(address.isEmpty()) throw new ResourceNotFoundException("Address","AddressId",addressId);
        return modelMapper.map(address,AddressDTO.class);
    }

    @Override
    public List<AddressDTO> getUserAddress(User user) {
        List<Address> userAddresses = user.getAddresses();
        return userAddresses.stream().map(address -> modelMapper.map(address,AddressDTO.class)).toList();
    }

    @Override
    public AddressDTO updateAddress(Long addressId, AddressDTO addressDTO) {
        Address address = addressRepository.findById(addressId).orElseThrow(() -> new ResourceNotFoundException("Address","AddressId",addressId));
        address.setCity(addressDTO.getCity());
        address.setState(addressDTO.getState());
        address.setPincode(addressDTO.getPincode());
        address.setStreet(addressDTO.getStreet());
        address.setCountry(addressDTO.getCountry());
        address.setBuildingName(addressDTO.getBuildingName());
        Address savedAddress = addressRepository.save(address);
        User user = address.getUser();
        user.getAddresses().removeIf(address1 -> address1.getAddressId().equals(addressId));
        user.getAddresses().add(savedAddress);
        userRepository.save(user);
        return modelMapper.map(savedAddress,AddressDTO.class);
    }

    @Override
    public String deleteAddress(Long addressId) {
        Address addressFromDB = addressRepository.findById(addressId).orElseThrow(() -> new ResourceNotFoundException("Address","addressId",addressId));
        User user = addressFromDB.getUser();
        user.getAddresses().removeIf(address ->  address.getAddressId().equals(addressId));
        userRepository.save(user);
        addressRepository.delete(addressFromDB);
        return "address deleted successfully with addressId: " + addressId;
    }

}
