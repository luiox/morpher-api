package com.github.luiox.morpher.jar;

import com.github.luiox.morpher.util.LogUtil;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;

public class JarUtil {
    public static final int JarMagicNumber = 0xCAFEBABE;
    public static final String ManifestFileName = "META-INF/MANIFEST.MF";

    /**
     * 检查一个文件是不是class文件
     *
     * @param fileName 文件的名称
     * @return 是否是class文件
     */
    public static boolean isClassFile(@NotNull String fileName) {
        return fileName.endsWith(".class");
    }

    /**
     * 检查一个文件的输入流是不是JarFile
     *
     * @param inputStream 文件的输入流
     * @return 是否是JarFile
     */
    public static boolean isClassFile(@NotNull InputStream inputStream) {
        try {
            // 读取前四个字节
            int firstByte = inputStream.read();
            int secondByte = inputStream.read();
            int thirdByte = inputStream.read();
            int fourthByte = inputStream.read();

            // 将四个字节组合成一个整数
            int magicNumber = (firstByte << 24) | (secondByte << 16) | (thirdByte << 8) | fourthByte;

            // 比较读取的魔数与Jar文件的魔数
            return magicNumber == JarMagicNumber;
        } catch (IOException e) {
            LogUtil.printStackTrace(e);
            return false;
        } finally {
            try {
                // 重置输入流到初始位置，以便后续操作
                if (inputStream.markSupported()) {
                    inputStream.reset();
                }
            } catch (IOException e) {
                LogUtil.printStackTrace(e);
            }
        }
    }

    /**
     * 检查通过前四个字节判定一个字节数组是不是class文件
     *
     * @param bytes 字节数组
     * @return 是否是class文件
     */
    public static boolean isClassFile(byte @NotNull [] bytes) {
        if (bytes.length < 4) {
            return false;
        }
        int magicNumber = (bytes[0] << 24) | (bytes[1] << 16) | (bytes[2] << 8) | bytes[3];
        return magicNumber == JarMagicNumber;
    }

}
