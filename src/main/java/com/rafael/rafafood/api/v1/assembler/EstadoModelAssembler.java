package com.rafael.rafafood.api.v1.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.rafael.rafafood.api.v1.FoodLinks;
import com.rafael.rafafood.api.v1.controller.EstadoController;
import com.rafael.rafafood.api.v1.model.EstadoModel;
import com.rafael.rafafood.core.security.FoodSecurity;
import com.rafael.rafafood.domain.model.Estado;

@Component
public class EstadoModelAssembler 
		extends RepresentationModelAssemblerSupport<Estado, EstadoModel> {

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private FoodLinks foodLinks;
	
	@Autowired
	private FoodSecurity foodSecurity;
	
	public EstadoModelAssembler() {
		super(EstadoController.class, EstadoModel.class);
	}
	
	@Override
	public EstadoModel toModel(Estado estado) {
		EstadoModel estadoModel = createModelWithId(estado.getId(), estado);
		modelMapper.map(estado, estadoModel);
		
		if (foodSecurity.podeConsultarEstados()) {
			estadoModel.add(foodLinks.linkToEstados("estados"));
		}
		
		return estadoModel;
	}
	
	@Override
	public CollectionModel<EstadoModel> toCollectionModel(Iterable<? extends Estado> entities) {
		CollectionModel<EstadoModel> collectionModel = super.toCollectionModel(entities);
		
		if (foodSecurity.podeConsultarEstados()) {
			collectionModel.add(foodLinks.linkToEstados());
		}
		
		return collectionModel;
	}
	
}
