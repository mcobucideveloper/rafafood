package com.rafael.rafafood.api.v1.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rafael.rafafood.api.v1.model.input.EstadoInput;
import com.rafael.rafafood.domain.model.Estado;

@Component
public class EstadoInputDisassembler {

	@Autowired
	private ModelMapper modelMapper;
	
	public Estado toDomainObject(EstadoInput estadoInput) {
		return modelMapper.map(estadoInput, Estado.class);
	}
	
	public void copyToDomainObject(EstadoInput estadoInput, Estado estado) {
		modelMapper.map(estadoInput, estado);
	}
	
}
