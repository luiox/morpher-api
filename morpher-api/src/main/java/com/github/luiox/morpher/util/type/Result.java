package com.github.luiox.morpher.util.type;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Result<T, E> {
    private static final Logger logger = LoggerFactory.getLogger(Result.class);
    private final T value;
    private final E error;

    private Result(T value, E error) {
        this.value = value;
        this.error = error;
    }

    public T getValue() {
        return value;
    }

    public E getError() {
        return error;
    }

    public boolean isOk() {
        return value != null;
    }

    public boolean isErr() {
        return error != null;
    }

    public T unwrap() {
        if (isErr()) {
            if (error instanceof String) {
                logger.error("Result unwrap occur error, Error: {}", error);
            }
            throw new RuntimeException("Try to unwrap a error, Error: " + error);
        }
        return value;
    }

    public T expect(String info) {
        if (isErr()) {
            if (error instanceof String) {
                logger.error("Result expect occur error, Error: {} Info: {}", error, info);
            }
            throw new RuntimeException("Try to expect a error, Info: " + info);
        }
        return value;
    }

    public static <T, E> Result<T, E> Ok(T value) {
        return new Result<>(value, null);
    }

    public static <T, E> Result<T, E> Err(E error) {
        return new Result<>(null, error);
    }
}
