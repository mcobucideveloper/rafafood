package com.rafael.rafafood.api.v1.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.rafael.rafafood.api.v1.FoodLinks;
import com.rafael.rafafood.api.v1.controller.PedidoController;
import com.rafael.rafafood.api.v1.model.PedidoResumoModel;
import com.rafael.rafafood.core.security.FoodSecurity;
import com.rafael.rafafood.domain.model.Pedido;

@Component
public class PedidoResumoModelAssembler 
		extends RepresentationModelAssemblerSupport<Pedido, PedidoResumoModel> {

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private FoodLinks foodLinks;
	
	@Autowired
	private FoodSecurity foodSecurity;

	public PedidoResumoModelAssembler() {
		super(PedidoController.class, PedidoResumoModel.class);
	}
	
	@Override
	public PedidoResumoModel toModel(Pedido pedido) {
		PedidoResumoModel pedidoModel = createModelWithId(pedido.getCodigo(), pedido);
		modelMapper.map(pedido, pedidoModel);
		
		if (foodSecurity.podePesquisarPedidos()) {
			pedidoModel.add(foodLinks.linkToPedidos("pedidos"));
		}
		
		if (foodSecurity.podeConsultarRestaurantes()) {
			pedidoModel.getRestaurante().add(
					foodLinks.linkToRestaurante(pedido.getRestaurante().getId()));
		}

		if (foodSecurity.podeConsultarUsuariosGruposPermissoes()) {
			pedidoModel.getCliente().add(foodLinks.linkToUsuario(pedido.getCliente().getId()));
		}
		
		return pedidoModel;
	}

}
