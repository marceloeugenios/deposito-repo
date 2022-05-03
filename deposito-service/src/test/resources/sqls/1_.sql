-- Bebidas armazenadas por seção com suas respectivas queries. Volume?
select s.id as sec_id, s.nome as sec_name, b.id as beb_id, b.nome as beb_name,
 (
    (SELECT COALESCE(sum(mt.volume), 0) FROM movimentacao mt WHERE mt._secao = s.id and mt._bebida = b.id and mt.tipo_movimentacao = 'ENTRADA')
    -
    (SELECT COALESCE(sum(mt.volume), 0) FROM movimentacao mt WHERE mt._secao = s.id and mt._bebida = b.id and mt.tipo_movimentacao = 'SAIDA')
 ) as volume_atual
from movimentacao m
join secao s on s.id = m._secao
join bebida b on b.id = m._bebida
group by s.id, s.nome, b.id, b.nome
order by s.id, s.nome, b.id, b.nome;

