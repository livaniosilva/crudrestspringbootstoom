package br.com.stoom.service;

import java.util.List;

import br.com.stoom.dto.AddressDTO;
import br.com.stoom.entity.Address;

public interface AddressService {
	public Address save(AddressDTO address)throws Exception;
	public List<Address>findAll();
	public Address findById(Long id) throws Exception;
	public boolean update(Long id, AddressDTO address) throws Exception;
	public boolean delete(Long id) throws Exception;

}
