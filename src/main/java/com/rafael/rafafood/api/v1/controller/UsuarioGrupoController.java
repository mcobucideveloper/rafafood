package com.rafael.rafafood.api.v1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.rafael.rafafood.api.v1.FoodLinks;
import com.rafael.rafafood.api.v1.assembler.GrupoModelAssembler;
import com.rafael.rafafood.api.v1.model.GrupoModel;
import com.rafael.rafafood.api.v1.openapi.controller.UsuarioGrupoControllerOpenApi;
import com.rafael.rafafood.core.security.FoodSecurity;
import com.rafael.rafafood.core.security.CheckSecurity;
import com.rafael.rafafood.domain.model.Usuario;
import com.rafael.rafafood.domain.service.CadastroUsuarioService;

@RestController
@RequestMapping(path = "/v1/usuarios/{usuarioId}/grupos", 
	produces = MediaType.APPLICATION_JSON_VALUE)
public class UsuarioGrupoController implements UsuarioGrupoControllerOpenApi {

	@Autowired
	private CadastroUsuarioService cadastroUsuario;
	
	@Autowired
	private GrupoModelAssembler grupoModelAssembler;
	
	@Autowired
	private FoodLinks foodLinks;
	
	@Autowired
	private FoodSecurity foodSecurity;
	
	@CheckSecurity.UsuariosGruposPermissoes.PodeConsultar
	@Override
	@GetMapping
	public CollectionModel<GrupoModel> listar(@PathVariable Long usuarioId) {
		Usuario usuario = cadastroUsuario.buscarOuFalhar(usuarioId);
		
		CollectionModel<GrupoModel> gruposModel = grupoModelAssembler.toCollectionModel(usuario.getGrupos())
				.removeLinks();
		
		if (foodSecurity.podeEditarUsuariosGruposPermissoes()) {
			gruposModel.add(foodLinks.linkToUsuarioGrupoAssociacao(usuarioId, "associar"));
			
			gruposModel.getContent().forEach(grupoModel -> {
				grupoModel.add(foodLinks.linkToUsuarioGrupoDesassociacao(
						usuarioId, grupoModel.getId(), "desassociar"));
			});
		}
		
		return gruposModel;
	}
	
	@CheckSecurity.UsuariosGruposPermissoes.PodeEditar
	@Override
	@DeleteMapping("/{grupoId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<Void> desassociar(@PathVariable Long usuarioId, @PathVariable Long grupoId) {
		cadastroUsuario.desassociarGrupo(usuarioId, grupoId);
		
		return ResponseEntity.noContent().build();
	}
	
	@CheckSecurity.UsuariosGruposPermissoes.PodeEditar
	@Override
	@PutMapping("/{grupoId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<Void> associar(@PathVariable Long usuarioId, @PathVariable Long grupoId) {
		cadastroUsuario.associarGrupo(usuarioId, grupoId);
		
		return ResponseEntity.noContent().build();
	}

}
