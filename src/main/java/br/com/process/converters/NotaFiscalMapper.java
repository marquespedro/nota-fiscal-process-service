package br.com.process.converters;

import org.mapstruct.Mapper;

import br.com.process.dto.NotaFiscalDTO;
import br.com.process.model.NotaFiscal;

@Mapper(componentModel = "spring")
public interface NotaFiscalMapper {

	NotaFiscal converter(NotaFiscalDTO dto);

	NotaFiscalDTO converter(NotaFiscal entity);
}
