-- ➢ Consulta das seções disponíveis para venda de determinado tipo de bebida (calcular via algoritmo). Quais seções tem álcool e não álcool?
select s.id, s.nome, b.bebida_tipo as bebida_tipo,
 (
    (
        SELECT COALESCE(sum(mt.volume), 0)
        FROM movimentacao mt
            join bebida b1 on b1.id = mt._bebida
        WHERE s.id = mt._secao and mt._bebida = b1.id and mt.tipo_transacao = 'ENTRADA'
            and b1.bebida_tipo = b.bebida_tipo
    )
    -
    (
        SELECT COALESCE(sum(mt.volume), 0)
        FROM movimentacao mt
            join bebida b1 on b1.id = mt._bebida
        WHERE s.id = mt._secao and mt._bebida = b1.id and mt.tipo_transacao = 'SAIDA'
            and b1.bebida_tipo = b.bebida_tipo
    )
 ) as volume_atual
from movimentacao m
join secao s on s.id = m._secao
join bebida b on b.id = m._bebida
group by s.id, s.nome, b.bebida_tipo
order by s.id, s.nome, b.bebida_tipo


-- ➢ Histórico de entradas e saídas de bebidas

-- ➢ Histórico de entradas e saídas por tipo de bebida e seção.