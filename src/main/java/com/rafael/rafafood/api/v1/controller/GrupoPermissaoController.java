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
import com.rafael.rafafood.api.v1.assembler.PermissaoModelAssembler;
import com.rafael.rafafood.api.v1.model.PermissaoModel;
import com.rafael.rafafood.api.v1.openapi.controller.GrupoPermissaoControllerOpenApi;
import com.rafael.rafafood.core.security.FoodSecurity;
import com.rafael.rafafood.core.security.CheckSecurity;
import com.rafael.rafafood.domain.model.Grupo;
import com.rafael.rafafood.domain.service.CadastroGrupoService;

@RestController
@RequestMapping(path = "/v1/grupos/{grupoId}/permissoes",
		produces = MediaType.APPLICATION_JSON_VALUE)
public class GrupoPermissaoController implements GrupoPermissaoControllerOpenApi {

	@Autowired
	private CadastroGrupoService cadastroGrupo;
	
	@Autowired
	private PermissaoModelAssembler permissaoModelAssembler;
	
	@Autowired
	private FoodLinks foodLinks;
	
	@Autowired
	private FoodSecurity foodSecurity;
	
	@CheckSecurity.UsuariosGruposPermissoes.PodeConsultar
	@Override
	@GetMapping
	public CollectionModel<PermissaoModel> listar(@PathVariable Long grupoId) {
		Grupo grupo = cadastroGrupo.buscarOuFalhar(grupoId);
		
		CollectionModel<PermissaoModel> permissoesModel 
			= permissaoModelAssembler.toCollectionModel(grupo.getPermissoes())
				.removeLinks();
		
		permissoesModel.add(foodLinks.linkToGrupoPermissoes(grupoId));
		
		if (foodSecurity.podeEditarUsuariosGruposPermissoes()) {
			permissoesModel.add(foodLinks.linkToGrupoPermissaoAssociacao(grupoId, "associar"));
		
			permissoesModel.getContent().forEach(permissaoModel -> {
				permissaoModel.add(foodLinks.linkToGrupoPermissaoDesassociacao(
						grupoId, permissaoModel.getId(), "desassociar"));
			});
		}
		
		return permissoesModel;
	}
	
	@CheckSecurity.UsuariosGruposPermissoes.PodeEditar
	@Override
	@DeleteMapping("/{permissaoId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<Void> desassociar(@PathVariable Long grupoId, @PathVariable Long permissaoId) {
		cadastroGrupo.desassociarPermissao(grupoId, permissaoId);
		
		return ResponseEntity.noContent().build();
	}
	
	@CheckSecurity.UsuariosGruposPermissoes.PodeEditar
	@Override
	@PutMapping("/{permissaoId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<Void> associar(@PathVariable Long grupoId, @PathVariable Long permissaoId) {
		cadastroGrupo.associarPermissao(grupoId, permissaoId);
		
		return ResponseEntity.noContent().build();
	}

}
