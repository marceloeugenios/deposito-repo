package br.com.desafio.deposito.repository;

import br.com.desafio.deposito.model.Bebida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BebidaRepository extends JpaRepository<Bebida, Integer> {

    @Override
    @Query("SELECT b FROM Bebida b JOIN FETCH b.marca WHERE b.id = ?1")
    Optional<Bebida> findById(Integer id);

    @Override
    @Query("SELECT b FROM Bebida b JOIN FETCH b.marca")
    List<Bebida> findAll();
}
