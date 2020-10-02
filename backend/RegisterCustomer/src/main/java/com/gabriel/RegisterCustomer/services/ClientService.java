package com.gabriel.RegisterCustomer.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gabriel.RegisterCustomer.dto.ClientDTO;
import com.gabriel.RegisterCustomer.entities.Client;
import com.gabriel.RegisterCustomer.repositories.ClientRepository;

@Service
public class ClientService {
	
	@Autowired
	private ClientRepository repository;
	
	@Transactional(readOnly = true)
	public Page<ClientDTO> findAllPaged (PageRequest pageRequest){
		Page<Client> list = repository.findAll(pageRequest);
		
		return list.map(x -> new ClientDTO(x));
	}
	
	@Transactional(readOnly = true)
	public ClientDTO findById (Long id) {
		Optional<Client> obj = repository.findById(id);
		Client entity = obj.orElseThrow(() -> new RuntimeException("Entity not found"));
		return new ClientDTO(entity);
	}
	
	@Transactional
	public ClientDTO insert (ClientDTO dto) {
		Client client = new Client();
		copyDtoToEntity(dto, client);
		client = repository.save(client);
		return new ClientDTO(client);
	}
	
	@Transactional
	public ClientDTO update(Long id, ClientDTO dto) {
		try {
		Client entity = repository.getOne(id);
		copyDtoToEntity(dto, entity);
		entity = repository.save(entity);
		return new ClientDTO(entity);
		}
		catch(EntityNotFoundException e) {
			throw new RuntimeException("Id not found: " + id);
		}
	}
	
	public void delete(Long id) {
		try {
		repository.deleteById(id);
		}
		catch(EmptyResultDataAccessException e) {
			throw new RuntimeException("Id not found: " + id);
		}
		
	}
	
	
	
	
	private void copyDtoToEntity(ClientDTO dto, Client entity) {
		entity.setName(dto.getName());
		entity.setCpf(dto.getCpf());
		entity.setIncome(dto.getIncome());
		entity.setBirthDate(dto.getBirthDate());
		entity.setChildren(dto.getChildren());
		
		}

}
