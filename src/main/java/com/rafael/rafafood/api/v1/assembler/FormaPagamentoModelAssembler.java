package com.rafael.rafafood.api.v1.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.rafael.rafafood.api.v1.FoodLinks;
import com.rafael.rafafood.api.v1.controller.FormaPagamentoController;
import com.rafael.rafafood.api.v1.model.FormaPagamentoModel;
import com.rafael.rafafood.core.security.FoodSecurity;
import com.rafael.rafafood.domain.model.FormaPagamento;

@Component
public class FormaPagamentoModelAssembler 
		extends RepresentationModelAssemblerSupport<FormaPagamento, FormaPagamentoModel> {

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private FoodLinks foodLinks;
	
	@Autowired
	private FoodSecurity foodSecurity;
	
	public FormaPagamentoModelAssembler() {
		super(FormaPagamentoController.class, FormaPagamentoModel.class);
	}
	
	@Override
	public FormaPagamentoModel toModel(FormaPagamento formaPagamento) {
		FormaPagamentoModel formaPagamentoModel = 
				createModelWithId(formaPagamento.getId(), formaPagamento);
		
		modelMapper.map(formaPagamento, formaPagamentoModel);
		
		if (foodSecurity.podeConsultarFormasPagamento()) {
			formaPagamentoModel.add(foodLinks.linkToFormasPagamento("formasPagamento"));
		}
		
		return formaPagamentoModel;
	}
	
	@Override
	public CollectionModel<FormaPagamentoModel> toCollectionModel(Iterable<? extends FormaPagamento> entities) {
		CollectionModel<FormaPagamentoModel> collectionModel = super.toCollectionModel(entities);
		
		if (foodSecurity.podeConsultarFormasPagamento()) {
			collectionModel.add(foodLinks.linkToFormasPagamento());
		}
			
		return collectionModel;
	}
	
}
