SELECT secao1_.id                                              AS id_secao,
       secao1_.nome                                            AS nome_secao,
       bebida2_.bebida_tipo                                    AS tipo_bebida,
       To_char(movimentac0_.data_movimentacao, 'YYYY-MM-DD')   AS dt,
       (SELECT COALESCE(Sum(movimentac3_.volume), 0)
        FROM   movimentacao movimentac3_
               INNER JOIN bebida bebida4_
                       ON movimentac3_._bebida = bebida4_.id
               INNER JOIN secao secao5_
                       ON movimentac3_._secao = secao5_.id
        WHERE  secao5_.id = secao1_.id
               AND bebida4_.bebida_tipo = bebida2_.bebida_tipo
               AND movimentac3_.tipo_movimentacao = 'ENTRADA') AS entrada,
       (SELECT COALESCE(Sum(movimentac6_.volume), 0)
        FROM   movimentacao movimentac6_
               INNER JOIN bebida bebida7_
                       ON movimentac6_._bebida = bebida7_.id
               INNER JOIN secao secao8_
                       ON movimentac6_._secao = secao8_.id
        WHERE  secao8_.id = secao1_.id
               AND bebida7_.bebida_tipo = bebida2_.bebida_tipo
               AND movimentac6_.tipo_movimentacao = 'SAIDA')   AS saida,
       ( (SELECT COALESCE(Sum(movimentac3_.volume), 0)
          FROM   movimentacao movimentac3_
                 INNER JOIN bebida bebida4_
                         ON movimentac3_._bebida = bebida4_.id
                 INNER JOIN secao secao5_
                         ON movimentac3_._secao = secao5_.id
          WHERE  secao5_.id = secao1_.id
                 AND bebida4_.bebida_tipo = bebida2_.bebida_tipo
                 AND movimentac3_.tipo_movimentacao = 'ENTRADA') -
         (SELECT COALESCE(Sum(movimentac6_.volume), 0)
          FROM
           movimentacao movimentac6_
           INNER JOIN bebida bebida7_
                   ON movimentac6_._bebida = bebida7_.id
           INNER JOIN secao secao8_
                   ON movimentac6_._secao = secao8_.id
                                                                    WHERE
           secao8_.id = secao1_.id
           AND bebida7_.bebida_tipo = bebida2_.bebida_tipo
           AND movimentac6_.tipo_movimentacao = 'SAIDA') )     AS saldo
FROM   movimentacao movimentac0_
       INNER JOIN secao secao1_
               ON movimentac0_._secao = secao1_.id
       INNER JOIN bebida bebida2_
               ON movimentac0_._bebida = bebida2_.id
WHERE  secao1_.id = 1
GROUP  BY secao1_.id,
          secao1_.nome,
          bebida2_.bebida_tipo,
          dt