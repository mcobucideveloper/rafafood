package com.rafael.rafafood.api.v1.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.rafael.rafafood.api.v1.FoodLinks;
import com.rafael.rafafood.api.v1.assembler.ProdutoInputDisassembler;
import com.rafael.rafafood.api.v1.assembler.ProdutoModelAssembler;
import com.rafael.rafafood.api.v1.model.ProdutoModel;
import com.rafael.rafafood.api.v1.model.input.ProdutoInput;
import com.rafael.rafafood.api.v1.openapi.controller.RestauranteProdutoControllerOpenApi;
import com.rafael.rafafood.core.security.CheckSecurity;
import com.rafael.rafafood.domain.model.Produto;
import com.rafael.rafafood.domain.model.Restaurante;
import com.rafael.rafafood.domain.repository.ProdutoRepository;
import com.rafael.rafafood.domain.service.CadastroProdutoService;
import com.rafael.rafafood.domain.service.CadastroRestauranteService;

@RestController
@RequestMapping(path = "/v1/restaurantes/{restauranteId}/produtos", 
	produces = MediaType.APPLICATION_JSON_VALUE)
public class RestauranteProdutoController implements RestauranteProdutoControllerOpenApi {

	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private CadastroProdutoService cadastroProduto;
	
	@Autowired
	private CadastroRestauranteService cadastroRestaurante;
	
	@Autowired
	private ProdutoModelAssembler produtoModelAssembler;
	
	@Autowired
	private ProdutoInputDisassembler produtoInputDisassembler;
	
	@Autowired
	private FoodLinks foodLinks;
	
	@CheckSecurity.Restaurantes.PodeConsultar
	@Override
	@GetMapping
	public CollectionModel<ProdutoModel> listar(@PathVariable Long restauranteId,
			@RequestParam(required = false, defaultValue = "false") Boolean incluirInativos) {
		Restaurante restaurante = cadastroRestaurante.buscarOuFalhar(restauranteId);
		
		List<Produto> todosProdutos = null;
		
		if (incluirInativos) {
			todosProdutos = produtoRepository.findTodosByRestaurante(restaurante);
		} else {
			todosProdutos = produtoRepository.findAtivosByRestaurante(restaurante);
		}
		
		return produtoModelAssembler.toCollectionModel(todosProdutos)
				.add(foodLinks.linkToProdutos(restauranteId));
	}
	
	@CheckSecurity.Restaurantes.PodeConsultar
	@Override
	@GetMapping("/{produtoId}")
	public ProdutoModel buscar(@PathVariable Long restauranteId, @PathVariable Long produtoId) {
		Produto produto = cadastroProduto.buscarOuFalhar(restauranteId, produtoId);
		
		return produtoModelAssembler.toModel(produto);
	}
	
	@CheckSecurity.Restaurantes.PodeGerenciarFuncionamento
	@Override
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ProdutoModel adicionar(@PathVariable Long restauranteId,
			@RequestBody @Valid ProdutoInput produtoInput) {
		Restaurante restaurante = cadastroRestaurante.buscarOuFalhar(restauranteId);
		
		Produto produto = produtoInputDisassembler.toDomainObject(produtoInput);
		produto.setRestaurante(restaurante);
		
		produto = cadastroProduto.salvar(produto);
		
		return produtoModelAssembler.toModel(produto);
	}
	
	@CheckSecurity.Restaurantes.PodeGerenciarFuncionamento
	@Override
	@PutMapping("/{produtoId}")
	public ProdutoModel atualizar(@PathVariable Long restauranteId, @PathVariable Long produtoId,
			@RequestBody @Valid ProdutoInput produtoInput) {
		Produto produtoAtual = cadastroProduto.buscarOuFalhar(restauranteId, produtoId);
		
		produtoInputDisassembler.copyToDomainObject(produtoInput, produtoAtual);
		
		produtoAtual = cadastroProduto.salvar(produtoAtual);
		
		return produtoModelAssembler.toModel(produtoAtual);
	}
	
}
