package com.rafael.rafafood.api.v1.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.rafael.rafafood.api.v1.FoodLinks;
import com.rafael.rafafood.api.v1.controller.RestauranteProdutoFotoController;
import com.rafael.rafafood.api.v1.model.FotoProdutoModel;
import com.rafael.rafafood.core.security.FoodSecurity;
import com.rafael.rafafood.domain.model.FotoProduto;

@Component
public class FotoProdutoModelAssembler 
		extends RepresentationModelAssemblerSupport<FotoProduto, FotoProdutoModel> {

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private FoodLinks foodLinks;
	
	@Autowired
	private FoodSecurity foodSecurity;
	
	public FotoProdutoModelAssembler() {
		super(RestauranteProdutoFotoController.class, FotoProdutoModel.class);
	}
	
	@Override
	public FotoProdutoModel toModel(FotoProduto foto) {
		FotoProdutoModel fotoProdutoModel = modelMapper.map(foto, FotoProdutoModel.class);
		
		// Quem pode consultar restaurantes, também pode consultar os produtos e fotos
		if (foodSecurity.podeConsultarRestaurantes()) {
			fotoProdutoModel.add(foodLinks.linkToFotoProduto(
					foto.getRestauranteId(), foto.getProduto().getId()));
			
			fotoProdutoModel.add(foodLinks.linkToProduto(
					foto.getRestauranteId(), foto.getProduto().getId(), "produto"));
		}
		
		return fotoProdutoModel;
	}
	
}
