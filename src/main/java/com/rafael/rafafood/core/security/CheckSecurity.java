package com.rafael.rafafood.core.security;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;

public @interface CheckSecurity {

	public @interface Cozinhas {
		
		@PreAuthorize("hasAuthority('SCOPE_WRITE') and hasAuthority('EDITAR_COZINHAS')")
		@Retention(RUNTIME)
		@Target(METHOD)
		public @interface PodeEditar { }

		@PreAuthorize("@rafaSecurity.podeConsultarCozinhas()")
		@Retention(RUNTIME)
		@Target(METHOD)
		public @interface PodeConsultar { }
		
	}
	
	public @interface Restaurantes {
		
		@PreAuthorize("@rafaSecurity.podeGerenciarCadastroRestaurantes()")
		@Retention(RUNTIME)
		@Target(METHOD)
		public @interface PodeGerenciarCadastro { }

		@PreAuthorize("@rafaSecurity.podeGerenciarFuncionamentoRestaurantes(#restauranteId)")
		@Retention(RUNTIME)
		@Target(METHOD)
		public @interface PodeGerenciarFuncionamento { }
		
		@PreAuthorize("@rafaSecurity.podeConsultarRestaurantes()")
		@Retention(RUNTIME)
		@Target(METHOD)
		public @interface PodeConsultar { }
		
	}
	
	public @interface Pedidos {
		
		@PreAuthorize("hasAuthority('SCOPE_READ') and isAuthenticated()")
		@PostAuthorize("hasAuthority('CONSULTAR_PEDIDOS') or "
				+ "@rafaSecurity.usuarioAutenticadoIgual(returnObject.cliente.id) or "
				+ "@rafaSecurity.gerenciaRestaurante(returnObject.restaurante.id)")
		@Retention(RUNTIME)
		@Target(METHOD)
		public @interface PodeBuscar { }
		
		@PreAuthorize("@rafaSecurity.podePesquisarPedidos(#filtro.clienteId, #filtro.restauranteId)")
		@Retention(RUNTIME)
		@Target(METHOD)
		public @interface PodePesquisar { }
		
		@PreAuthorize("hasAuthority('SCOPE_WRITE') and isAuthenticated()")
		@Retention(RUNTIME)
		@Target(METHOD)
		public @interface PodeCriar { }
		
		@PreAuthorize("@rafaSecurity.podeGerenciarPedidos(#codigoPedido)")
		@Retention(RUNTIME)
		@Target(METHOD)
		public @interface PodeGerenciarPedidos { }
		
	}
	
	public @interface FormasPagamento {
		
		@PreAuthorize("hasAuthority('SCOPE_WRITE') and hasAuthority('EDITAR_FORMAS_PAGAMENTO')")
		@Retention(RUNTIME)
		@Target(METHOD)
		public @interface PodeEditar { }

		@PreAuthorize("@rafaSecurity.podeConsultarFormasPagamento()")
		@Retention(RUNTIME)
		@Target(METHOD)
		public @interface PodeConsultar { }
		
	}
	
	public @interface Cidades {
		
		@PreAuthorize("hasAuthority('SCOPE_WRITE') and hasAuthority('EDITAR_CIDADES')")
		@Retention(RUNTIME)
		@Target(METHOD)
		public @interface PodeEditar { }

		@PreAuthorize("@rafaSecurity.podeConsultarCidades()")
		@Retention(RUNTIME)
		@Target(METHOD)
		public @interface PodeConsultar { }
		
	}
	
	public @interface Estados {
		
		@PreAuthorize("hasAuthority('SCOPE_WRITE') and hasAuthority('EDITAR_ESTADOS')")
		@Retention(RUNTIME)
		@Target(METHOD)
		public @interface PodeEditar { }

		@PreAuthorize("@rafaSecurity.podeConsultarEstados()")
		@Retention(RUNTIME)
		@Target(METHOD)
		public @interface PodeConsultar { }
		
	}
	
	public @interface UsuariosGruposPermissoes {
		
		@PreAuthorize("hasAuthority('SCOPE_WRITE') and "
				+ "@rafaSecurity.usuarioAutenticadoIgual(#usuarioId)")
		@Retention(RUNTIME)
		@Target(METHOD)
		public @interface PodeAlterarPropriaSenha { }
		
		@PreAuthorize("hasAuthority('SCOPE_WRITE') and (hasAuthority('EDITAR_USUARIOS_GRUPOS_PERMISSOES') or "
				+ "@rafaSecurity.usuarioAutenticadoIgual(#usuarioId))")
		@Retention(RUNTIME)
		@Target(METHOD)
		public @interface PodeAlterarUsuario { }

		@PreAuthorize("@rafaSecurity.podeEditarUsuariosGruposPermissoes()")
		@Retention(RUNTIME)
		@Target(METHOD)
		public @interface PodeEditar { }
		

		@PreAuthorize("@rafaSecurity.podeConsultarUsuariosGruposPermissoes()")
		@Retention(RUNTIME)
		@Target(METHOD)
		public @interface PodeConsultar { }
		
	}
	
	public @interface Estatisticas {
		
		@PreAuthorize("@rafaSecurity.podeConsultarEstatisticas()")
		@Retention(RUNTIME)
		@Target(METHOD)
		public @interface PodeConsultar { }
		
	}
	
}
