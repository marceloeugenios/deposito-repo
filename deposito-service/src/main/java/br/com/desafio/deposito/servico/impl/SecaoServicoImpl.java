package br.com.desafio.deposito.servico.impl;

import br.com.desafio.deposito.model.Secao;
import br.com.desafio.deposito.repository.SecaoRepository;
import br.com.desafio.deposito.servico.SecaoServico;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SecaoServicoImpl implements SecaoServico {

  private final SecaoRepository secaoRepository;

  @Override
  @Transactional
  public Secao salvar(Secao secao) {
    return secaoRepository.save(secao);
  }

  @Override
  @Transactional
  public Secao alterar(Secao secao) {
    var newSecao = buscarPorId(secao.getId());

    newSecao.setNome(secao.getNome());
    newSecao.setSituacao(secao.getSituacao());
    return secaoRepository.save(newSecao);
  }

  @Override
  @Transactional(readOnly = true)
  public List<Secao> buscarTodos() {
    return secaoRepository.findAll();
  }

  @Override
  @Transactional(readOnly = true)
  public Secao buscarPorId(Integer id) {
    return secaoRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Seção não encontrada!"));
  }

  @Transactional(readOnly = true)
  public Secao buscarPorIdLock(Integer id) {
    return secaoRepository.findByIdLock(id)
        .orElseThrow(() -> new IllegalArgumentException("Seção não encontrada!"));
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRED)
  public void atualizarDataAlteracao(Secao secao) {
    try {
      secaoRepository.atualizarDataAlteracao(secao.getId(), LocalDateTime.now());
    } catch (Exception e) {
      throw new IllegalArgumentException("Secao pode ja ter sido alterada, por favor tente novamente!");
    }
  }
}
