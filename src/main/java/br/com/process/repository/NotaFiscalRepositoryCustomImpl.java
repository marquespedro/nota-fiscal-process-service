package br.com.process.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import br.com.process.model.NotaFiscal;

@Repository
public class NotaFiscalRepositoryCustomImpl implements NotaFiscalRepositoryCustom {

	@PersistenceContext
	private EntityManager manager;

	@Autowired
	@Lazy
	private NotaFiscalRepository notaFiscalRepository;

	@Override
	public NotaFiscal salvar(NotaFiscal nota) {
		
		notaFiscalRepository.save(nota);

		salvarDuplicatas(nota);
		
		return nota;
	}

	private void salvarDuplicatas(NotaFiscal nota) {
		if (CollectionUtils.isEmpty(nota.getDuplicatas())) {
			return;
		}
		nota.getDuplicatas().stream().forEach(d -> {
			d.setNotaFiscal(nota);
			manager.persist(d);
		});
	}

}
