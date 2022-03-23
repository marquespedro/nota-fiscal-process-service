package br.com.process.model;

import lombok.Getter;

@Getter
public enum StatusProcessamento {

	AGUARDANDO_PROCESSAMENTO(1),
	EM_PROCESSAMENTO(2),
	PROCESSADA(3),
	PROCESSADA_COM_ERRO(4);
	
	private Integer codigo;
	
	private StatusProcessamento(Integer codigo) {
		this.codigo = codigo;
	}
	
	
}
