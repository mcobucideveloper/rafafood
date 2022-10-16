package com.rafael.rafafood.domain.service;

import java.util.List;

import com.rafael.rafafood.domain.filter.VendaDiariaFilter;
import com.rafael.rafafood.domain.model.dto.VendaDiaria;

public interface VendaQueryService {

	List<VendaDiaria> consultarVendasDiarias(VendaDiariaFilter filtro, String timeOffset);
	
}
