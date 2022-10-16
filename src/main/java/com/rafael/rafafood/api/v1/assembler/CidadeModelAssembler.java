package com.rafael.rafafood.api.v1.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.rafael.rafafood.api.v1.FoodLinks;
import com.rafael.rafafood.api.v1.controller.CidadeController;
import com.rafael.rafafood.api.v1.model.CidadeModel;
import com.rafael.rafafood.core.security.FoodSecurity;
import com.rafael.rafafood.domain.model.Cidade;

@Component
public class CidadeModelAssembler 
		extends RepresentationModelAssemblerSupport<Cidade, CidadeModel> {

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private FoodLinks foodLinks;
	
	@Autowired
	private FoodSecurity foodSecuritys;
	
	public CidadeModelAssembler() {
		super(CidadeController.class, CidadeModel.class);
	}
	
	@Override
	public CidadeModel toModel(Cidade cidade) {
		CidadeModel cidadeModel = createModelWithId(cidade.getId(), cidade);
		
		modelMapper.map(cidade, cidadeModel);
		
		if (foodSecuritys.podeConsultarCidades()) {
			cidadeModel.add(foodLinks.linkToCidades("cidades"));
		}
		
		if (foodSecuritys.podeConsultarEstados()) {
			cidadeModel.getEstado().add(foodLinks.linkToEstado(cidadeModel.getEstado().getId()));
		}
		
		return cidadeModel;
	}
	
	@Override
	public CollectionModel<CidadeModel> toCollectionModel(Iterable<? extends Cidade> entities) {
		CollectionModel<CidadeModel> collectionModel = super.toCollectionModel(entities);
		
		if (foodSecuritys.podeConsultarCidades()) {
			collectionModel.add(foodLinks.linkToCidades());
		}
		
		return collectionModel;
	}
	
}
