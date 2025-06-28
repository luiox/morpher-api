package com.github.luiox.morpher.model.io;

import com.github.luiox.morpher.model.IResourceContainer;
import org.jetbrains.annotations.NotNull;

/**
 * 资源导出器接口。
 * <p>
 * 实现该接口可自定义将IResourceContainer中的资源导出到外部介质的逻辑。
 */
public interface IResourceExporter {
    /**
     * 将资源容器中的资源导出。
     *
     * @param container 资源容器
     * @throws Exception 导出过程中可能抛出的异常
     */
    void exportResource(@NotNull IResourceContainer container) throws Exception;
}