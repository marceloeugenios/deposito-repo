package br.com.desafio.deposito.servico.impl;

import br.com.desafio.deposito.model.Marca;
import br.com.desafio.deposito.repository.MarcaRepository;
import br.com.desafio.deposito.servico.MarcaServico;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MarcaServicoImpl implements MarcaServico {

    private final MarcaRepository marcaRepository;

    @Override
    @Transactional
    public Marca salvar(Marca marca) {
        return marcaRepository.save(marca);
    }

    @Override
    @Transactional
    public Marca alterar(Marca marca) {

        var newMarca = buscarPorId(marca.getId());

        newMarca.setNome(marca.getNome());
        newMarca.setSituacao(marca.getSituacao());

        return marcaRepository.save(newMarca);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Marca> buscarTodos() {
        return marcaRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Marca buscarPorId(Integer id) {
        return marcaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Marca não encontrada!"));
    }
}
