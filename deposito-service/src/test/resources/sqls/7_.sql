SELECT
           bebida2_.id   AS col_2_0_,
           bebida2_.nome AS col_2_0_,
           (
                      SELECT     CAST(COALESCE(Sum(movimentac3_.volume), 0) AS FLOAT)
                      FROM       movimentacao movimentac3_
                      INNER JOIN bebida bebida4_
                      ON         movimentac3_._bebida = bebida4_.id
                      INNER JOIN secao secao5_
                      ON         movimentac3_._secao = secao5_.id
                      WHERE      secao5_.id = 1
                      AND        bebida4_.id = bebida2_.id
                      AND        movimentac3_.tipo_movimentacao = 'ENTRADA') AS col_4_0_,
           (
                      SELECT     COALESCE(Sum(movimentac6_.volume), 0)
                      FROM       movimentacao movimentac6_
                      INNER JOIN bebida bebida7_
                      ON         movimentac6_._bebida = bebida7_.id
                      INNER JOIN secao secao8_
                      ON         movimentac6_._secao = secao8_.id
                      WHERE      secao8_.id = 1
                      AND        bebida7_.id = bebida2_.id
                      AND        movimentac6_.tipo_movimentacao = 'SAIDA') AS col_5_0_
FROM       movimentacao movimentac0_
INNER JOIN secao secao1_
ON         movimentac0_._secao = secao1_.id
INNER JOIN bebida bebida2_
ON         movimentac0_._bebida = bebida2_.id
WHERE      secao1_.id = 1
AND        bebida2_.id = 4
GROUP BY
           bebida2_.id,
           bebida2_.nome,