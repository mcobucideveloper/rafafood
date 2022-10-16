package com.rafael.rafafood.domain.repository;

import com.rafael.rafafood.domain.model.FotoProduto;

public interface ProdutoRepositoryQueries {

	FotoProduto save(FotoProduto foto);
	
	void delete(FotoProduto foto);
	
}
