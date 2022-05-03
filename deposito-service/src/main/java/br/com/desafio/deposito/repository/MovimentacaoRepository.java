package br.com.desafio.deposito.repository;

import br.com.desafio.deposito.dto.response.BebidaTipoExtratoResponseDto;
import br.com.desafio.deposito.dto.response.SecaoExtratoDataResponseDto;
import br.com.desafio.deposito.model.Movimentacao;
import br.com.desafio.deposito.dto.response.BebidaSecaoExtratoResponseDto;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovimentacaoRepository extends JpaRepository<Movimentacao, Long> {

    @Override
    @Query("SELECT m FROM Movimentacao m JOIN FETCH m.bebida b JOIN FETCH m.secao s")
    List<Movimentacao> findAll();

    @Override
    @Query("SELECT m FROM Movimentacao m JOIN FETCH m.bebida b JOIN FETCH m.secao s")
    List<Movimentacao> findAll(Sort sort);

    @Query("SELECT new br.com.desafio.deposito.dto.response.SecaoExtratoDataResponseDto" +
            "(s.id, s.nome, b.bebidaTipo, to_char(m.dataMovimentacao, 'YYYY-MM-DD') as dataMovimentacao, " +
            "( " +
            "( " +
            "(SELECT CAST(COALESCE(sum(mt.volume), 0) as float) " +
            "FROM Movimentacao mt JOIN mt.bebida b1 JOIN mt.secao s1 " +
            "WHERE s1 = s AND b1.bebidaTipo = b.bebidaTipo AND mt.tipoMovimentacao = 'ENTRADA') " +
            "-" +
            "(SELECT CAST(COALESCE(sum(mt.volume), 0) as float) " +
            "FROM Movimentacao mt JOIN mt.bebida b1 JOIN mt.secao s1 " +
            "WHERE s1 = s AND b1.bebidaTipo = b.bebidaTipo AND mt.tipoMovimentacao = 'SAIDA')) " +
            ") " +
            ")" +
            "FROM Movimentacao m JOIN m.secao s JOIN m.bebida b " +
            "WHERE s.id = ?1 " +
            "GROUP BY s.id, s.nome, b.bebidaTipo, dataMovimentacao")
    List<SecaoExtratoDataResponseDto> findByExtrato(Integer idSecao);

    @Query("SELECT new br.com.desafio.deposito.dto.response.BebidaSecaoExtratoResponseDto" +
            "(s.id, s.nome, b.id, b.nome, b.bebidaTipo, " +
            "( " +
            "(SELECT CAST(COALESCE(sum(mt.volume), 0) as float) " +
            "FROM Movimentacao mt JOIN mt.bebida b1 JOIN mt.secao s1 " +
            "WHERE s1 = s AND b1.id = b.id AND mt.tipoMovimentacao = 'ENTRADA')" +
            " - " +
            "(SELECT CAST(COALESCE(sum(mt.volume), 0) as float) " +
            "FROM Movimentacao mt JOIN mt.bebida b1 JOIN mt.secao s1 " +
            "WHERE s1 = s AND b1.id = b.id AND mt.tipoMovimentacao = 'SAIDA') " +
            ") " +
            ") " +
            "FROM Movimentacao m JOIN m.secao s JOIN m.bebida b " +
            "WHERE s.id = ?1 AND b.id = ?2 " +
            "GROUP BY s.id, s.nome, b.id, b.nome, b.bebidaTipo")
    Optional<BebidaSecaoExtratoResponseDto> findByExtrato(Integer idSecao, Integer idBebida);

    @Query("SELECT new br.com.desafio.deposito.dto.response.BebidaSecaoExtratoResponseDto" +
            "(s.id, s.nome, b.id, b.nome, b.bebidaTipo, " +
            " ( " +
            "(SELECT CAST(COALESCE(sum(mt.volume), 0) as float) " +
            "FROM Movimentacao mt JOIN mt.bebida b1 JOIN mt.secao s1 " +
            "WHERE s1 = s AND b1.id = b.id AND " +
            "mt.tipoMovimentacao = 'ENTRADA')" +
            " - " +
            "(SELECT CAST(COALESCE(sum(mt.volume), 0) as float) " +
            "FROM Movimentacao mt JOIN mt.bebida b1 JOIN mt.secao s1 " +
            "WHERE s1 = s AND b1.id = b.id AND mt.tipoMovimentacao = 'SAIDA') " +
            ") " +
            ") " +
            "FROM Movimentacao m JOIN m.secao s JOIN m.bebida b " +
            "GROUP BY s.id, s.nome, b.id, b.nome, b.bebidaTipo")
    List<BebidaSecaoExtratoResponseDto> findByBebidaSecaoExtrato();

    @Query("SELECT new br.com.desafio.deposito.dto.response.BebidaTipoExtratoResponseDto" +
            "(b.bebidaTipo, " +
            "(" +
            "(SELECT CAST(COALESCE(sum(mt.volume), 0) as float) " +
            "FROM Movimentacao mt JOIN mt.bebida b1 " +
            "WHERE " +
            "b1.bebidaTipo = b.bebidaTipo AND mt.tipoMovimentacao = 'ENTRADA') " +
            " - " +
            "(SELECT CAST(COALESCE(sum(mt.volume), 0) as float) " +
            "FROM Movimentacao mt JOIN mt.bebida b1 " +
            "WHERE " +
            "b1.bebidaTipo = b.bebidaTipo AND mt.tipoMovimentacao = 'SAIDA') " +
            ") " +
            ") " +
            "FROM Movimentacao m " +
            "JOIN m.bebida b " +
            "GROUP BY b.bebidaTipo")
    List<BebidaTipoExtratoResponseDto> findByBebidaTipoExtrato();

}
