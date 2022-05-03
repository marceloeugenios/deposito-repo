package br.com.desafio.deposito.repository;

import br.com.desafio.deposito.model.Secao;

import java.time.LocalDateTime;
import java.util.Optional;
import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SecaoRepository extends JpaRepository<Secao, Integer> {

  @Modifying
  @Query("UPDATE Secao set ultimaAlteracao = :dataAlteracao WHERE id = :id")
  void atualizarDataAlteracao(@Param("id") Integer id, @Param("dataAlteracao") LocalDateTime dataAlteracao);

  @Lock(LockModeType.PESSIMISTIC_READ)
  @Query("SELECT s FROM Secao s WHERE s.id = :id")
  @QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value = "3000")})
  Optional<Secao> findByIdLock(@Param("id") Integer id);
}
