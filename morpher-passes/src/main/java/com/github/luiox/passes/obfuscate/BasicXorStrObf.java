package com.github.luiox.passes.obfuscate;

import com.github.luiox.morpher.asm.insn.InsnBuilder;
import com.github.luiox.morpher.asm.insn.InsnUtil;
import com.github.luiox.morpher.transformer.ClassPass;
import com.github.luiox.morpher.transformer.IPassContext;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.Random;

public class BasicXorStrObf extends ClassPass {

    static Random random = new Random();

    @Override
    public void run(@NotNull ClassNode classNode, @NotNull IPassContext iPassContext) {
        // 生成一个key
        int xorKey = random.nextInt();


        String encryptMethodName = generateRandomString();
        // 查找所有的字符串常量
        for(var methodNode : classNode.methods){
            InsnBuilder builder = new InsnBuilder();
            for(var insn : methodNode.instructions){
                if(insn instanceof LdcInsnNode ldcInsnNode && ldcInsnNode.cst instanceof String str) {
                    var encryptedStr = encrypt(str, xorKey);
                    builder.ldc(encryptedStr);
                    builder.invokestatic(classNode.name, encryptMethodName, "(Ljava/lang/String;)Ljava/lang/String;");
                    continue;
                }
                builder.addInsnNode(insn);
            }
            methodNode.instructions = builder.getInsnList();
        }
        classNode.methods.add(genXorMethod(encryptMethodName, xorKey));
    }

    public static String generateRandomString() {
        int length = 10;
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }

        return sb.toString();
    }

    private static String encrypt(String string, int key) {
        StringBuilder stringBuilder = new StringBuilder();
        int n = 0;
        while (n < string.length()) {
            stringBuilder.append((char)(string.charAt(n) ^ key));
            ++n;
        }
        return stringBuilder.toString();
    }

    MethodNode genXorMethod(String methodName, int xorKey){
        MethodNode methodNode = new MethodNode();
        methodNode.name = methodName;
        methodNode.desc = "(Ljava/lang/String;)Ljava/lang/String;";
        methodNode.access |= Opcodes.ACC_PRIVATE | Opcodes.ACC_STATIC;
        methodNode.instructions = InsnBuilder.build(list->{
            list.label("A");
            list.neww("java/lang/StringBuilder");
            list.dup();
            list.invokespecial("java/lang/StringBuilder", "<init>", "()V");
            list.astore(1);
            list.label("B");
            list.iconst_0();
            list.istore(2);
            list.label("C");
            list.astore(1);
            list.aload(0);
            list.iload(2);
            list.invokevirtual("java/lang/String", "charAt", "(I)C");
            list.addInsnNode(InsnUtil.getIntInsn(xorKey));
            list.ixor();
            list.i2c();
            list.invokevirtual("java/lang/StringBuilder", "append", "(C)Ljava/lang/StringBuilder;");
            list.pop();
            list.iinc(2, 1);
            list.label("C");
            list.iload(2);
            list.aload(0);
            list.invokevirtual("java/lang/String", "length", "()I");
            list.if_icmplt("D");
            list.gotoo("E");
            list.label("D");
            list.gotoo("B");
            list.label("E");
            list.aload(1);
            list.invokevirtual("java/lang/StringBuilder", "toString", "()Ljava/lang/String;");
            list.areturn();
            list.label("F");
        });
        return methodNode;
    }
}
