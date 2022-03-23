package br.com.process.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(of = {"id" , "valor"})
@JsonIgnoreProperties(value = {"notaFiscal"})
public class DuplicataDTO {

	private Integer id;

	private Integer parcela;

	private BigDecimal valor;

	private Date dataVencimento;


}
