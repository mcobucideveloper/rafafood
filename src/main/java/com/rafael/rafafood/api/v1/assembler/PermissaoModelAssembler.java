package com.rafael.rafafood.api.v1.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.rafael.rafafood.api.v1.FoodLinks;
import com.rafael.rafafood.api.v1.model.PermissaoModel;
import com.rafael.rafafood.core.security.FoodSecurity;
import com.rafael.rafafood.domain.model.Permissao;

@Component
public class PermissaoModelAssembler 
		implements RepresentationModelAssembler<Permissao, PermissaoModel> {

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private FoodLinks foodLinks;
	
	@Autowired
	private FoodSecurity foodSecurity;

	@Override
	public PermissaoModel toModel(Permissao permissao) {
		PermissaoModel permissaoModel = modelMapper.map(permissao, PermissaoModel.class);
		return permissaoModel;
	}
	
	@Override
	public CollectionModel<PermissaoModel> toCollectionModel(Iterable<? extends Permissao> entities) {
		CollectionModel<PermissaoModel> collectionModel 
			= RepresentationModelAssembler.super.toCollectionModel(entities);

		if (foodSecurity.podeConsultarUsuariosGruposPermissoes()) {
			collectionModel.add(foodLinks.linkToPermissoes());
		}
		
		return collectionModel;
	}
	
}
