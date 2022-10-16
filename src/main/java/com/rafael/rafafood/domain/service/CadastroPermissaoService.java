package com.rafael.rafafood.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rafael.rafafood.domain.exception.PermissaoNaoEncontradaException;
import com.rafael.rafafood.domain.model.Permissao;
import com.rafael.rafafood.domain.repository.PermissaoRepository;

@Service
public class CadastroPermissaoService {

	@Autowired
	private PermissaoRepository permissaoRepository;
	
	public Permissao buscarOuFalhar(Long permissaoId) {
		return permissaoRepository.findById(permissaoId)
			.orElseThrow(() -> new PermissaoNaoEncontradaException(permissaoId));
	}
	
}
