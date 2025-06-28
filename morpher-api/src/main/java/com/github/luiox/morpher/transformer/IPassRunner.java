package com.github.luiox.morpher.transformer;

/**
 * Pass运行器接口。
 * <p>
 * 实现该接口可自定义Pass的整体执行流程。
 */
public interface IPassRunner {
    /**
     * 执行Pass转换流程。
     * @param context Pass上下文
     */
    void transform(IPassContext context);
}
