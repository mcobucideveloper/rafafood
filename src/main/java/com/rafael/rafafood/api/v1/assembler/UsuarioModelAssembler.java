package com.rafael.rafafood.api.v1.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.rafael.rafafood.api.v1.FoodLinks;
import com.rafael.rafafood.api.v1.controller.UsuarioController;
import com.rafael.rafafood.api.v1.model.UsuarioModel;
import com.rafael.rafafood.core.security.FoodSecurity;
import com.rafael.rafafood.domain.model.Usuario;

@Component
public class UsuarioModelAssembler 
		extends RepresentationModelAssemblerSupport<Usuario, UsuarioModel> {

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private FoodLinks foodLinks;
	
	@Autowired
	private FoodSecurity foodSecurity;
	
	public UsuarioModelAssembler() {
		super(UsuarioController.class, UsuarioModel.class);
	}
	
	@Override
	public UsuarioModel toModel(Usuario usuario) {
		UsuarioModel usuarioModel = createModelWithId(usuario.getId(), usuario);
		modelMapper.map(usuario, usuarioModel);
		
		if (foodSecurity.podeConsultarUsuariosGruposPermissoes()) {
			usuarioModel.add(foodLinks.linkToUsuarios("usuarios"));
			
			usuarioModel.add(foodLinks.linkToGruposUsuario(usuario.getId(), "grupos-usuario"));
		}
		
		return usuarioModel;
	}
	
	@Override
	public CollectionModel<UsuarioModel> toCollectionModel(Iterable<? extends Usuario> entities) {
		CollectionModel<UsuarioModel> collectionModel = super.toCollectionModel(entities);
		
		if (foodSecurity.podeConsultarUsuariosGruposPermissoes()) {
			collectionModel.add(foodLinks.linkToUsuarios());
		}
		
		return collectionModel;
	}
	
}
