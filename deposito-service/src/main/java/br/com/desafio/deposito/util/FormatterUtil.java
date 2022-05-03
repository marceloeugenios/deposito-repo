package br.com.desafio.deposito.util;

import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class FormatterUtil {

    public LocalDate fromStringToLocalDate(String data) {
        try {
            var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            return LocalDate.parse(data, formatter);
        } catch (Exception e) {
            throw new IllegalArgumentException(String.format("Data %s informada não é válida!", data));
        }
    }

    public Float formatDecimal(Float value) {
        return BigDecimal.valueOf(value)
                .setScale(2, RoundingMode.HALF_UP)
                .floatValue();
    }
}
