package br.com.desafio.deposito.servico.impl;

import br.com.desafio.deposito.model.Bebida;
import br.com.desafio.deposito.repository.BebidaRepository;
import br.com.desafio.deposito.servico.BebidaServico;
import br.com.desafio.deposito.model.util.EBebidaTipo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BebidaServicoImpl implements BebidaServico {

    private final BebidaRepository bebidaRepository;

    @Override
    @Transactional
    public Bebida salvar(Bebida bebida) {
        return bebidaRepository.save(bebida);
    }

    @Override
    @Transactional
    public Bebida alterar(Bebida bebida) {
        var newBebida = buscarPorId(bebida.getId());

        newBebida.setNome(bebida.getNome());
        newBebida.setBebidaTipo(EBebidaTipo.ALCOOLICA);
        newBebida.setMarca(bebida.getMarca());
        newBebida.setSituacao(bebida.getSituacao());

        return bebidaRepository.save(newBebida);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Bebida> buscarTodos() {
        return bebidaRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Bebida buscarPorId(Integer id) {
        return bebidaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Bebida não encontrada!"));
    }
}
