package com.rafael.rafafood.api.v1.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.rafael.rafafood.api.v1.FoodLinks;
import com.rafael.rafafood.api.v1.controller.RestauranteController;
import com.rafael.rafafood.api.v1.model.RestauranteBasicoModel;
import com.rafael.rafafood.core.security.FoodSecurity;
import com.rafael.rafafood.domain.model.Restaurante;

@Component
public class RestauranteBasicoModelAssembler 
		extends RepresentationModelAssemblerSupport<Restaurante, RestauranteBasicoModel> {

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private FoodLinks foodLinks;
	
	@Autowired
	private FoodSecurity foodSecurity;
	
	public RestauranteBasicoModelAssembler() {
		super(RestauranteController.class, RestauranteBasicoModel.class);
	}
	
	@Override
	public RestauranteBasicoModel toModel(Restaurante restaurante) {
		RestauranteBasicoModel restauranteModel = createModelWithId(
				restaurante.getId(), restaurante);
		
		modelMapper.map(restaurante, restauranteModel);
		
		if (foodSecurity.podeConsultarRestaurantes()) {
			restauranteModel.add(foodLinks.linkToRestaurantes("restaurantes"));
		}
		
		if (foodSecurity.podeConsultarCozinhas()) {
			restauranteModel.getCozinha().add(
					foodLinks.linkToCozinha(restaurante.getCozinha().getId()));
		}
		
		return restauranteModel;
	}
	
	@Override
	public CollectionModel<RestauranteBasicoModel> toCollectionModel(Iterable<? extends Restaurante> entities) {
		CollectionModel<RestauranteBasicoModel> collectionModel = super.toCollectionModel(entities);
		
		if (foodSecurity.podeConsultarRestaurantes()) {
			collectionModel.add(foodLinks.linkToRestaurantes());
		}
				
		return collectionModel;
	}
	
}
