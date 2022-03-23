package br.com.process.job;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.process.model.NotaFiscal;
import br.com.process.model.StatusProcessamento;
import br.com.process.repository.NotaFiscalRepository;
import lombok.extern.java.Log;

@Service
@Log
public class Processador {

	@Value("${upload-service.path.input}")
	private String propInputArquivos;

	@Value("${upload-service.path.output}")
	private String propOutputArquivos;

	@Autowired
	private NotaFiscalRepository repository;

	/**
	 * ira rodar a cada 120 segundos
	 */
	// @Scheduled(cron="0 0/2 * 1/1 * ?")
	@Scheduled(cron = "0 0/1 * 1/1 * ?")
	public void processar() {
		String diretorio = obterCaminhoCompletoDiretorioInput();
		File dir = new File(diretorio);
		String[] arquivos = dir.list();
		Arrays.asList(arquivos).parallelStream().forEach(iniciaProcessamento());
	}

	private Consumer<String> iniciaProcessamento() {
		return nomeArquivo -> {
			NotaFiscal nota = this.repository.findByNomeArquivo(nomeArquivo);
			iniciarStatusProcessamento(nota);
			processarArquivoXml(nota);
		};

	}

	@Transactional
	public void processarArquivoXml(NotaFiscal nota) {
		String nomeArquivo = nota.getNomeArquivo();

		String caminhoCompletoArquivoInput = obterCaminhoCompletoDiretorioInput() + File.separator + nomeArquivo;
		String caminhoCompletoArquivoOutput = obterCaminhoCompletoDiretorioOutput() + File.separator + nomeArquivo;

		Path path = Paths.get(caminhoCompletoArquivoInput);

		try (FileOutputStream out = new FileOutputStream(caminhoCompletoArquivoOutput);) {

			byte[] conteudo = Files.readAllBytes(path);
			out.write(conteudo);
			nota.setStatus(StatusProcessamento.PROCESSADA);
			deletarArquivoDiretorioInput(caminhoCompletoArquivoInput);
			log.info(String.format("Status Nota: %s em PROCESSADA ", nota.getNomeArquivo()));
		} catch (IOException e) {
			log.warning(String.format("Status Nota: %s em ERRO ", nota.getNomeArquivo()));
			nota.setStatus(StatusProcessamento.PROCESSADA_COM_ERRO);
			e.printStackTrace();
		}
		repository.save(nota);
	}

	private void deletarArquivoDiretorioInput(String nomeArquivo) {
		File file = new File(nomeArquivo);
		if(file.exists()) {
			file.delete();
		}
	}

	@Transactional
	public NotaFiscal iniciarStatusProcessamento(NotaFiscal nota) {
		if (Objects.nonNull(nota) && !StatusProcessamento.EM_PROCESSAMENTO.equals(nota.getStatus())) {
			nota.setStatus(StatusProcessamento.EM_PROCESSAMENTO);
			repository.save(nota);
		}
		log.info(String.format("Status Nota: %s em EM_PROCESSAMENTO ", nota.getNomeArquivo()));
		return nota;
	}

	private String obterCaminhoCompletoDiretorioInput() {
		String diretorioDoUsuario = System.getProperty("user.home");
		return diretorioDoUsuario + File.separator + this.propInputArquivos;
	}

	private String obterCaminhoCompletoDiretorioOutput() {
		String diretorioDoUsuario = System.getProperty("user.home");
		return diretorioDoUsuario + File.separator + this.propOutputArquivos;
	}

}
