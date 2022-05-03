-- ➢ Consulta do volume total no estoque por cada tipo de bebida.
select b.bebida_tipo as bebida_tipo,
 (
    (
        select COALESCE(sum(mt.volume), 0)
        from movimentacao mt
            join bebida b1 on b1.id = mt._bebida
        where mt._bebida = b1.id and mt.tipo_movimentacao = 'ENTRADA'
            and b1.bebida_tipo = b.bebida_tipo
    )
    -
    (
        select COALESCE(sum(mt.volume), 0)
        from movimentacao mt
            join bebida b1 on b1.id = mt._bebida
        where mt._bebida = b1.id and mt.tipo_movimentacao = 'SAIDA'
            and b1.bebida_tipo = b.bebida_tipo
    )
 ) as volume_atual
from movimentacao m
join bebida b on b.id = m._bebida
group by b.bebida_tipo
order by b.bebida_tipo