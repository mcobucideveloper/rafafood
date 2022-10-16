package com.rafael.rafafood.api.v1.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.rafael.rafafood.api.v1.FoodLinks;
import com.rafael.rafafood.api.v1.controller.PedidoController;
import com.rafael.rafafood.api.v1.model.PedidoModel;
import com.rafael.rafafood.core.security.FoodSecurity;
import com.rafael.rafafood.domain.model.Pedido;

@Component
public class PedidoModelAssembler 
		extends RepresentationModelAssemblerSupport<Pedido, PedidoModel> {

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private FoodLinks foodLinks;
	
	@Autowired
	private FoodSecurity foodSecurity;

	public PedidoModelAssembler() {
		super(PedidoController.class, PedidoModel.class);
	}
	
	@Override
	public PedidoModel toModel(Pedido pedido) {
		PedidoModel pedidoModel = createModelWithId(pedido.getCodigo(), pedido);
		modelMapper.map(pedido, pedidoModel);
		
		// Não usei o método foodSecurity.podePesquisarPedidos(clienteId, restauranteId) aqui,
		// porque na geração do link, não temos o id do cliente e do restaurante, 
		// então precisamos saber apenas se a requisição está autenticada e tem o escopo de leitura
		if (foodSecurity.podePesquisarPedidos()) {
			pedidoModel.add(foodLinks.linkToPedidos("pedidos"));
		}
		
		if (foodSecurity.podeGerenciarPedidos(pedido.getCodigo())) {
			if (pedido.podeSerConfirmado()) {
				pedidoModel.add(foodLinks.linkToConfirmacaoPedido(pedido.getCodigo(), "confirmar"));
			}
			
			if (pedido.podeSerCancelado()) {
				pedidoModel.add(foodLinks.linkToCancelamentoPedido(pedido.getCodigo(), "cancelar"));
			}
			
			if (pedido.podeSerEntregue()) {
				pedidoModel.add(foodLinks.linkToEntregaPedido(pedido.getCodigo(), "entregar"));
			}
		}
		
		if (foodSecurity.podeConsultarRestaurantes()) {
			pedidoModel.getRestaurante().add(
					foodLinks.linkToRestaurante(pedido.getRestaurante().getId()));
		}
		
		if (foodSecurity.podeConsultarUsuariosGruposPermissoes()) {
			pedidoModel.getCliente().add(
					foodLinks.linkToUsuario(pedido.getCliente().getId()));
		}
		
		if (foodSecurity.podeConsultarFormasPagamento()) {
			pedidoModel.getFormaPagamento().add(
					foodLinks.linkToFormaPagamento(pedido.getFormaPagamento().getId()));
		}
		
		if (foodSecurity.podeConsultarCidades()) {
			pedidoModel.getEnderecoEntrega().getCidade().add(
					foodLinks.linkToCidade(pedido.getEnderecoEntrega().getCidade().getId()));
		}
		
		// Quem pode consultar restaurantes, também pode consultar os produtos dos restaurantes
		if (foodSecurity.podeConsultarRestaurantes()) {
			pedidoModel.getItens().forEach(item -> {
				item.add(foodLinks.linkToProduto(
						pedidoModel.getRestaurante().getId(), item.getProdutoId(), "produto"));
			});
		}
		
		return pedidoModel;
	}

}
