package com.github.luiox.morpher.util.type;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 表示一个操作的结果类型，包含成功（Ok）和失败（Err）两种状态。
 * <p>
 * 该类用于封装方法的返回值和错误信息，类似于Rust中的Result类型。
 * @param <T> 成功时的返回值类型
 * @param <E> 失败时的错误类型
 */
public class Result<T, E> {
    /** 日志记录器 */
    private static final Logger logger = LoggerFactory.getLogger(Result.class);
    /** 成功时的值 */
    private final T value;
    /** 失败时的错误信息 */
    private final E error;

    /**
     * 私有构造方法，外部请使用Ok或Err静态方法创建实例。
     * @param value 成功时的值
     * @param error 失败时的错误信息
     */
    private Result(T value, E error) {
        this.value = value;
        this.error = error;
    }

    /**
     * 获取成功时的值。
     * @return 成功时的值，失败时为null
     */
    public T getValue() {
        return value;
    }

    /**
     * 获取失败时的错误信息。
     * @return 失败时的错误信息，成功时为null
     */
    public E getError() {
        return error;
    }

    /**
     * 判断是否为成功状态。
     * @return 如果为成功状态返回true，否则返回false
     */
    public boolean isOk() {
        return value != null;
    }

    /**
     * 判断是否为失败状态。
     * @return 如果为失败状态返回true，否则返回false
     */
    public boolean isErr() {
        return error != null;
    }

    /**
     * 获取成功时的值，如果为失败状态则抛出异常。
     * @return 成功时的值
     * @throws RuntimeException 如果为失败状态则抛出异常
     */
    public T unwrap() {
        if (isErr()) {
            if (error instanceof String) {
                logger.error("Result unwrap occur error, Error: {}", error);
            }
            throw new RuntimeException("Try to unwrap a error, Error: " + error);
        }
        return value;
    }

    /**
     * 获取成功时的值，如果为失败状态则抛出异常，并输出额外信息。
     * @param info 额外的错误信息
     * @return 成功时的值
     * @throws RuntimeException 如果为失败状态则抛出异常
     */
    public T expect(String info) {
        if (isErr()) {
            if (error instanceof String) {
                logger.error("Result expect occur error, Error: {} Info: {}", error, info);
            }
            throw new RuntimeException("Try to expect a error, Info: " + info);
        }
        return value;
    }

    /**
     * 创建一个成功的Result实例。
     * @param value 成功时的值
     * @return Result实例
     * @param <T> 成功类型
     * @param <E> 错误类型
     */
    public static <T, E> Result<T, E> Ok(T value) {
        return new Result<>(value, null);
    }

    /**
     * 创建一个失败的Result实例。
     * @param error 失败时的错误信息
     * @return Result实例
     * @param <T> 成功类型
     * @param <E> 错误类型
     */
    public static <T, E> Result<T, E> Err(E error) {
        return new Result<>(null, error);
    }
}
