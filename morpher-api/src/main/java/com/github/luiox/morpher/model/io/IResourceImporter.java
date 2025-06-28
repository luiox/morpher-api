package com.github.luiox.morpher.model.io;

import com.github.luiox.morpher.model.IResourceContainer;
import org.jetbrains.annotations.NotNull;

/**
 * 资源导入器接口。
 * <p>
 * 实现该接口可自定义将外部资源导入到IResourceContainer中的逻辑。
 */
public interface IResourceImporter {
    /**
     * 将资源导入到指定的资源容器中。
     *
     * @param container 资源容器
     * @throws Exception 导入过程中可能抛出的异常
     */
    void importResource(@NotNull IResourceContainer container) throws Exception;
}
