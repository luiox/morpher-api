package com.github.luiox.morpher.jar;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.BiConsumer;

public interface IJarReader {
    /**
     * 读取jar，输入流由调用者负责关闭
     *
     * @param consumer 每个条目会调用一次<条目路径，条目的输入流>的回调
     * @throws IOException 如果出现问题会抛出异常
     */
    void read(BiConsumer<String, InputStream> consumer) throws IOException;
}
