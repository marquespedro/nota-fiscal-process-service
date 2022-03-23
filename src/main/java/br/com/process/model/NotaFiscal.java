package br.com.process.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotaFiscal {

	@Id
	@EqualsAndHashCode.Include
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@ToString.Include
	private Integer id;
	
	private String numero;
	
	@Column(name="dh_registro")
	private Date dhRegistro;
	
	@Column(name="nome_emitente")
	private String nomeEmitente;
	
	@Column(name="nome_destinatario")
	private String nomeDestinatario;
	
	@Column(name="valor_nota")
	private BigDecimal valorNota;
	
	@Column(name="nome_arquivo")
	private String nomeArquivo;
	
	@Enumerated(EnumType.STRING)
	private StatusProcessamento status;
	
	@OneToMany(mappedBy = "notaFiscal")
	private List<Duplicata> duplicatas;
	
}
