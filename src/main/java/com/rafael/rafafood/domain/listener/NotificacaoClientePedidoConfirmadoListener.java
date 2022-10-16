package com.rafael.rafafood.domain.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import com.rafael.rafafood.domain.event.PedidoConfirmadoEvent;
import com.rafael.rafafood.domain.model.Pedido;
import com.rafael.rafafood.domain.service.EnvioEmailService;
import com.rafael.rafafood.domain.service.EnvioEmailService.Mensagem;

@Component
public class NotificacaoClientePedidoConfirmadoListener {

	@Autowired
	private EnvioEmailService envioEmail;
	
	@TransactionalEventListener
	public void aoConfirmarPedido(PedidoConfirmadoEvent event) {
		Pedido pedido = event.getPedido();
		
		var mensagem = Mensagem.builder()
				.assunto(pedido.getRestaurante().getNome() + " - Pedido confirmado")
				.corpo("pedido-confirmado.html")
				.variavel("pedido", pedido)
				.destinatario(pedido.getCliente().getEmail())
				.build();

		envioEmail.enviar(mensagem);
	}
	
}
