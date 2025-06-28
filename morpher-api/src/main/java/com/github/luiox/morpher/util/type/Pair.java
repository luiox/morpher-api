package com.github.luiox.morpher.util.type;

import java.util.Objects;

/**
 * 通用的二元组（Pair）类型，用于存储两个相关联的对象。
 *
 * @param <K> 第一个元素的类型
 * @param <V> 第二个元素的类型
 */
public class Pair<K, V> {
    /**
     * 第一个元素
     */
    private K first;
    /**
     * 第二个元素
     */
    private V second;

    /**
     * 构造一个二元组。
     *
     * @param first  第一个元素
     * @param second 第二个元素
     */
    public Pair(K first, V second) {
        this.first = first;
        this.second = second;
    }

    /**
     * 获取第一个元素。
     *
     * @return 第一个元素
     */
    public K getFirst() {
        return first;
    }

    /**
     * 设置第一个元素。
     *
     * @param first 第一个元素
     */
    public void setFirst(K first) {
        this.first = first;
    }

    /**
     * 获取第二个元素。
     *
     * @return 第二个元素
     */
    public V getSecond() {
        return second;
    }

    /**
     * 设置第二个元素。
     *
     * @param second 第二个元素
     */
    public void setSecond(V second) {
        this.second = second;
    }

    /**
     * 静态工厂方法，创建一个二元组。
     *
     * @param first  第一个元素
     * @param second 第二个元素
     * @param <K>    第一个元素类型
     * @param <V>    第二个元素类型
     * @return Pair实例
     */
    public static <K, V> Pair<K, V> of(K first, V second) {
        return new Pair<>(first, second);
    }

    /**
     * 计算哈希值。
     *
     * @return 哈希值
     */
    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }

    /**
     * 判断两个Pair是否相等。
     *
     * @param obj 比较对象
     * @return 是否相等
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Pair<?, ?> other = (Pair<?, ?>) obj;
        return Objects.equals(this.first, other.first) &&
                Objects.equals(this.second, other.second);
    }

    /**
     * 返回Pair的字符串表示。
     *
     * @return 字符串
     */
    @Override
    public String toString() {
        return "Pair{" +
                "first=" + first +
                ", second=" + second +
                '}';
    }
}
