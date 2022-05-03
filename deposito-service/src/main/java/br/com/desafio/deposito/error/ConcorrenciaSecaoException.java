package br.com.desafio.deposito.error;

public class ConcorrenciaSecaoException extends RuntimeException {

  public ConcorrenciaSecaoException(String message) {
    super(message);
  }
}
