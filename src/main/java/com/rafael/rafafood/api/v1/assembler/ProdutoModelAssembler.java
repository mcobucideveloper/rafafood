package com.rafael.rafafood.api.v1.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.rafael.rafafood.api.v1.FoodLinks;
import com.rafael.rafafood.api.v1.controller.RestauranteProdutoController;
import com.rafael.rafafood.api.v1.model.ProdutoModel;
import com.rafael.rafafood.core.security.FoodSecurity;
import com.rafael.rafafood.domain.model.Produto;

@Component
public class ProdutoModelAssembler 
		extends RepresentationModelAssemblerSupport<Produto, ProdutoModel> {

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private FoodLinks foodLinks;
	
	@Autowired
	private FoodSecurity foodSecuritys;
	
	public ProdutoModelAssembler() {
		super(RestauranteProdutoController.class, ProdutoModel.class);
	}
	
	@Override
	public ProdutoModel toModel(Produto produto) {
		ProdutoModel produtoModel = createModelWithId(
				produto.getId(), produto, produto.getRestaurante().getId());
		
		modelMapper.map(produto, produtoModel);
		
		// Quem pode consultar restaurantes, também pode consultar os produtos e fotos
		if (foodSecuritys.podeConsultarRestaurantes()) {
			produtoModel.add(foodLinks.linkToProdutos(produto.getRestaurante().getId(), "produtos"));

			produtoModel.add(foodLinks.linkToFotoProduto(
					produto.getRestaurante().getId(), produto.getId(), "foto"));
		}
		
		return produtoModel;
	}
	
}
