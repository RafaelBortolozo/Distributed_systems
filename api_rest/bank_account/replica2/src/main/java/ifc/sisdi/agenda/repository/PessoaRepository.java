package ifc.sisdi.agenda.repository;

import ifc.sisdi.agenda.model.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PessoaRepository extends JpaRepository<Pessoa, Integer> {

}
