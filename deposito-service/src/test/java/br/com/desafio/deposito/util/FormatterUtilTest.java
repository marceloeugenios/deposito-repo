package br.com.desafio.deposito.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class FormatterUtilTest {

    @Test
    void formataDataComSucesso() {
        var data = "2020-01-01";
        var dataFormatada = FormatterUtil.fromStringToLocalDate(data);
        Assertions.assertNotNull(dataFormatada);
    }

    @Test
    void throwsExceptionQuandoDataForInvalida() {
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> FormatterUtil.fromStringToLocalDate(null));
        Assertions.assertEquals("Data null informada não é válida!", exception.getMessage());
    }
}