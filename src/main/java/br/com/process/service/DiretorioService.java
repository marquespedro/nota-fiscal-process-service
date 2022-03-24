package br.com.process.service;

import java.io.File;
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

		String path = obterPathDiretorioInput(nomeArquivo);

		File file = new File(path);

		if (file.exists()) {
			file.delete();
		}
	}

	public void moverParaDiretorio(String diretorioMover, String nomeArquivo) {

		String arquivoEntrada = obterPathDiretorioInput(nomeArquivo);
		Path path = Paths.get(arquivoEntrada);
			
		try (FileOutputStream out = new FileOutputStream(diretorioMover)) {
			byte[] conteudo = Files.readAllBytes(path);
			out.write(conteudo);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public String obterPathDiretorioInput() {
		return obterPathDiretorioUsuario() + File.separator + this.propInputArquivos;
	}
	
	public String obterPathDiretorioInput(String nomeArquivo) {
		return obterPathDiretorioInput() + File.separator + nomeArquivo;
	}

	public String obterPathDiretorioOutput(String nomeArquivo) {
		return obterPathDiretorioUsuario() + File.separator + this.propOutputArquivos + File.separator + nomeArquivo;
	}

	public String obterPathDiretorioErro(String nomeArquivo) {
		return obterPathDiretorioUsuario() + File.separator + this.propErroArquivos + File.separator + nomeArquivo;
	}

	private String obterPathDiretorioUsuario() {
		return System.getProperty("user.home");
	}
}
