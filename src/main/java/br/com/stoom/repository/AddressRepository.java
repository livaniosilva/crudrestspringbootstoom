package br.com.stoom.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.stoom.entity.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {}