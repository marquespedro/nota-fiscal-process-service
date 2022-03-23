package br.com.process.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import br.com.process.dto.NotaFiscalDTO;
import br.com.process.dto.NotaFiscalDTO.DuplicataXml;
import br.com.process.dto.NotaFiscalDTO.ElementXml;
import br.com.process.exception.ConverterXmlNotaException;
import br.com.process.model.Duplicata;
import br.com.process.model.NotaFiscal;
import br.com.process.model.StatusProcessamento;
import br.com.process.repository.NotaFiscalRepositoryCustom;

@Service
public class NotaFiscalService {

	@Autowired
	private DiretorioService diretorioService;
	
	@Autowired
	private NotaFiscalRepositoryCustom repository;
	
	public NotaFiscal convertXmlParaNotaFiscal(String nomeArquivo) throws IOException {

		NotaFiscalDTO notaFiscalXml = converterXmlParaNotaFiscalXml(nomeArquivo);

		return montarNotaFiscal(notaFiscalXml, nomeArquivo);
	}
	
	@Transactional
	public NotaFiscal salvar(NotaFiscal notaFiscal) {
		return repository.salvar(notaFiscal);
	} 
	


	public NotaFiscal atualizarStatus(NotaFiscal nota, StatusProcessamento status) {
		if (Objects.nonNull(nota)) {
			nota.setStatus(status);
			salvar(nota);
		}
		return nota;
	}
	
	private NotaFiscal montarNotaFiscal(NotaFiscalDTO notaFiscalXml, String nomeArquivo) {

		ElementXml elementXml = notaFiscalXml.getElement();

		if(Objects.isNull(elementXml)) {
			throw new ConverterXmlNotaException("Falha ao converter xml");
		}
		
		NotaFiscal nota = NotaFiscal.builder().numero(elementXml.getChave())
				.status(StatusProcessamento.AGUARDANDO_PROCESSAMENTO).dhRegistro(elementXml.getDataHoraRegistro())
				.nomeEmitente(elementXml.getNomeEmitente()).nomeDestinatario(elementXml.getNomeDestinatario())
				.valorNota(elementXml.getValor()).duplicatas(montarDuplicatas(elementXml.getDuplicatas()))
				.nomeArquivo(nomeArquivo).build();

		return nota;
	}
	
	private List<Duplicata> montarDuplicatas(List<DuplicataXml> duplicatasXml) {

		Stream<Duplicata> duplicatas = duplicatasXml.stream().map(d -> {

			return Duplicata.builder().dataVencimento(d.getDataVencimento()).valor(d.getValorParcela())
					.parcela(d.getNumeroParcela()).build();
		});

		return duplicatas.collect(Collectors.toList());
	}
	
	private NotaFiscalDTO converterXmlParaNotaFiscalXml(String nomeArquivo) throws IOException {
		
		String caminhoEntrada = diretorioService.obterCaminhoEntrada() + File.separator + nomeArquivo;
		
		Path path = Paths.get(caminhoEntrada);
				
		XmlMapper mapper = new XmlMapper();
		
		byte [] conteudo = Files.readAllBytes(path);

		NotaFiscalDTO notaFiscalXml =  mapper.readValue(new String(conteudo), NotaFiscalDTO.class);;
		
		return notaFiscalXml;
	}
	


	

}
