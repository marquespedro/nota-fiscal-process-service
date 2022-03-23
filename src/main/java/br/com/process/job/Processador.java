package br.com.process.job;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import br.com.process.exception.ConverterXmlNotaException;
import br.com.process.model.NotaFiscal;
import br.com.process.model.StatusProcessamento;
import br.com.process.service.DiretorioService;
import br.com.process.service.NotaFiscalService;
import lombok.extern.java.Log;

@Service
@Log
public class Processador {

	@Autowired
	private DiretorioService diretorioService;
	
	@Autowired
	private NotaFiscalService notaFiscalService;
	
	/**
	 * a cada 120 segundos
	 */
	@Scheduled(cron="0 0/2 * 1/1 * ?")
	public void job() {
		File diretorio = new File(diretorioService.obterCaminhoEntrada());
		String[] arquivos = diretorio.list();	
		Arrays.asList(arquivos).parallelStream().forEach(processar());
	}

	private Consumer<String> processar() {
		return nomeArquivo -> {
			
			NotaFiscal nota = null;

			String diretorioErro = diretorioService.obterCaminhoErro() + File.separator + nomeArquivo;
			String diretorioSaida = diretorioService.obterCaminhoSaida() + File.separator + nomeArquivo;
			
			try  {
				
				nota = notaFiscalService.convertXmlParaNotaFiscal(nomeArquivo);
				nota.setStatus(StatusProcessamento.EM_PROCESSAMENTO);
				diretorioService.moverParaDiretorio(diretorioSaida, nomeArquivo);
				nota.setStatus(StatusProcessamento.PROCESSADA);
				diretorioService.deletarArquivoDiretorioEntrada(nomeArquivo);
				notaFiscalService.salvar(nota);
			
			} catch (IOException | ConverterXmlNotaException e) {
				
				diretorioService.moverParaDiretorio(diretorioErro, nomeArquivo);
				diretorioService.deletarArquivoDiretorioEntrada(nomeArquivo);
				
				log.warning(String.format("Erro ao processar arquivo: %s  causa: %s  mensagem: %s", nomeArquivo, e.getCause(), e.getMessage()));
			}
		};
	}
	




}
