-- Tipos de bebidas por secao
select s.id, b.bebida_tipo as bebida_tipo,

 (SELECT COALESCE(sum(mt.volume), 0)
        FROM movimentacao mt join bebida b1 on b1.id = mt._bebida
        WHERE
        s.id = mt._secao and
        mt._bebida = b1.id and
        b1.bebida_tipo = b.bebida_tipo and
        mt.tipo_movimentacao = 'ENTRADA') as entrada,

 (SELECT COALESCE(sum(mt.volume), 0)
        FROM movimentacao mt
            join bebida b1 on b1.id = mt._bebida
        WHERE s.id = mt._secao and mt._bebida = b1.id and mt.tipo_movimentacao = 'SAIDA'
            and b1.bebida_tipo = b.bebida_tipo ) as saida

from movimentacao m
join secao s on s.id = m._secao
join bebida b on b.id = m._bebida
where s.id = 1
group by s.id, b.bebida_tipo
order by s.id, b.bebida_tipo