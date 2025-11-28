package com.spring.bar.springBar.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.NoSuchElementException;

/**
 * Classe global para tratamento de exceções (Advice)
 * Transforma exceções do Service em respostas HTTP padronizadas.
 */
@ControllerAdvice
public class RestExceptionHandler {

    // 1. Erro 404: Quando uma entidade (Mesa, Conta, Produto) não é encontrada.
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleNotFoundException(NoSuchElementException ex) {
        return new ResponseEntity<>("Erro 404: O recurso solicitado não foi encontrado. Detalhe: " + ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    // 2. Erro 400: Para validações de dados inválidos (e.g., quantidade <= 0, valor de pagamento negativo).
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return new ResponseEntity<>("Erro 400 - Dados Inválidos: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // 3. Erro 409: Para conflitos de estado (e.g., tentar abrir mesa que já está ABERTA, fechar conta com saldo).
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleIllegalStateException(IllegalStateException ex) {
        return new ResponseEntity<>("Erro 409 - Conflito: O estado atual não permite esta operação. Detalhe: " + ex.getMessage(), HttpStatus.CONFLICT);
    }

    // 4. Erro 500: Captura qualquer outra RuntimeException não mapeada (erro interno genérico).
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleGenericRuntimeException(RuntimeException ex) {
        // Logar a exceção completa aqui é essencial para debug
        return new ResponseEntity<>("Erro 500 - Erro Interno do Sistema: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}