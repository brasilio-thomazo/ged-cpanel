package br.dev.optimus.ged.cpanel.exception;

import lombok.Getter;

@Getter
public class UniqueException extends IllegalArgumentException {
    private final String field;

    public UniqueException(String message, String field) {
        super(message);
        this.field = field;
    }

}
