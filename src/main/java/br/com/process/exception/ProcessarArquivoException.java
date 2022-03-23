package br.com.process.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ProcessarArquivoException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ProcessarArquivoException(String msg) {
		super(msg);
	}

}
