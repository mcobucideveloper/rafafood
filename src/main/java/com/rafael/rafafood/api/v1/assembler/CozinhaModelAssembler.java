package com.rafael.rafafood.api.v1.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.rafael.rafafood.api.v1.FoodLinks;
import com.rafael.rafafood.api.v1.controller.CozinhaController;
import com.rafael.rafafood.api.v1.model.CozinhaModel;
import com.rafael.rafafood.core.security.FoodSecurity;
import com.rafael.rafafood.domain.model.Cozinha;

@Component
public class CozinhaModelAssembler 
		extends RepresentationModelAssemblerSupport<Cozinha, CozinhaModel> {

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private FoodLinks foodLinks;
	
	@Autowired
	private FoodSecurity foodSecurity;
	
	public CozinhaModelAssembler() {
		super(CozinhaController.class, CozinhaModel.class);
	}
	
	@Override
	public CozinhaModel toModel(Cozinha cozinha) {
		CozinhaModel cozinhaModel = createModelWithId(cozinha.getId(), cozinha);
		modelMapper.map(cozinha, cozinhaModel);
		
		if (foodSecurity.podeConsultarCozinhas()) {
			cozinhaModel.add(foodLinks.linkToCozinhas("cozinhas"));
		}
		
		return cozinhaModel;
	}
	
}
