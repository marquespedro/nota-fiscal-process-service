package br.com.process.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DiretorioService {

	@Value("${upload-service.path.input}")
	private String propInputArquivos;

	@Value("${upload-service.path.output}")
	private String propOutputArquivos;

	@Value("${upload-service.path.error}")
	private String propErroArquivos;

	public void deletarArquivoDiretorioEntrada(String nomeArquivo) {

		String caminhoArquivo = obterCaminhoEntrada() + File.separator + nomeArquivo;

		File file = new File(caminhoArquivo);

		if (file.exists()) {
			file.delete();
		}
	}

	public void moverParaDiretorio(String diretorioMover, String nomeArquivo) {

		String arquivoEntrada = obterCaminhoEntrada() + File.separator + nomeArquivo;
		Path path = Paths.get(arquivoEntrada);

		try (FileOutputStream out = new FileOutputStream(diretorioMover)) {
			byte[] conteudo = Files.readAllBytes(path);
			out.write(conteudo);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public String obterCaminhoEntrada() {
		return obterCaminhoUsuario() + File.separator + this.propInputArquivos;
	}

	public String obterCaminhoSaida() {
		return obterCaminhoUsuario() + File.separator + this.propOutputArquivos;
	}

	public String obterCaminhoErro() {
		return obterCaminhoUsuario() + File.separator + this.propErroArquivos;
	}

	private String obterCaminhoUsuario() {
		return System.getProperty("user.home");
	}
}
