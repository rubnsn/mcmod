package ruby.bamboo.asm;

import net.minecraft.launchwrapper.IClassTransformer;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import cpw.mods.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;

public class ItemHoeTransformaer implements IClassTransformer, Opcodes {
    private final String targetClassName = "net.minecraft.item.ItemHoe";

    private String methodName = "getItemEnchantability";

    //srg func_77619_b ()I

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if (transformedName.equals("net.minecraft.item.Item") && !name.equals(transformedName)) {
            methodName = getMethodName(basicClass);//"func_77619_b";
        }
        if (transformedName.equals(targetClassName)) {
            return addForgeEvent(basicClass);
        }

        return basicClass;
    }

    private byte[] addForgeEvent(byte[] bytes) {
        ClassNode cnode = new ClassNode();
        ClassReader reader = new ClassReader(bytes);
        reader.accept(cnode, 0);

        MethodNode mnode = new MethodNode(ASM4, ACC_PUBLIC, methodName, "()I", null, new String[0]);
        mnode.visitVarInsn(ALOAD, 0);
        mnode.visitMethodInsn(INVOKESTATIC, "ruby/bamboo/event/enchant/ItemEnchantabilityEvent", "getItemEnchantability", "(L" + cnode.superName + ";)I");
        mnode.visitInsn(IRETURN);

        cnode.methods.add(mnode);
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
        cnode.accept(cw);
        bytes = cw.toByteArray();
        return bytes;
    }

    private String getMethodName(byte[] bytes) {
        ClassNode cnode = new ClassNode();
        ClassReader reader = new ClassReader(bytes);
        reader.accept(cnode, 0);
        for (MethodNode m : cnode.methods) {
            if (FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(cnode.name, m.name, m.desc).equals("func_77619_b")) {
                return m.name;
            }
        }
        return "func_77619_b";
    }

}
