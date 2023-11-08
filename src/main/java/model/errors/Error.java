package model.errors;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Error {
    private final int numError;
    private final String message;
    private final LocalDateTime date;

    public Error(int numError, String message) {
        this.numError = numError;
        this.message = message;
        this.date = LocalDateTime.now();
    }
}
