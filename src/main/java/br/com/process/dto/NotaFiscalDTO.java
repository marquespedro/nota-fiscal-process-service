package br.com.process.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import br.com.process.model.StatusProcessamento;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString(of = {"id", "numero" , "nomeArquivo"})
@AllArgsConstructor
@NoArgsConstructor
public class NotaFiscalDTO {
	
	private Integer id;

	private String numero;
	
	private String nomeArquivo;

	private LocalDateTime dhRegistro;

	private String nomeEmitente;

	private String nomeDestinatario;

	private BigDecimal valorNota;

	private StatusProcessamento status;

	private List<DuplicataDTO> duplicatas;
}
