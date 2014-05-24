package ruby.bamboo.asm;

import net.minecraft.launchwrapper.IClassTransformer;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class EnchantmentTransformaer implements IClassTransformer, Opcodes {
    private final String targetClassName = "net.minecraft.item.ItemHoe";
    private final String targetMethodName = "calcItemStackEnchantability";
    private final String targetMethodDesc = "(Ljava/util/Random;IILnet/minecraft/item/ItemStack;)I";

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if (transformedName.equals(targetClassName)) {
            return addForgeEvent(basicClass);
        }

        return basicClass;
    }

    private byte[] addForgeEvent(byte[] bytes) {
        ClassNode cnode = new ClassNode();
        ClassReader reader = new ClassReader(bytes);
        reader.accept(cnode, 0);
        MethodNode mnode = new MethodNode(ASM4, ACC_PUBLIC, "getItemEnchantability", "()I", null, new String[0]);

        InsnList overrideList = new InsnList();
        overrideList.add(new VarInsnNode(ALOAD, 0));
        overrideList.add(new MethodInsnNode(INVOKESTATIC, "ruby/bamboo/event/enchant/ItemEnchantabilityEvent", "getItemEnchantability", "(Lnet/minecraft/item/Item;)I"));
        overrideList.add(new InsnNode(IRETURN));
        mnode.instructions = overrideList;

        cnode.methods.add(mnode);
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
        cnode.accept(cw);
        bytes = cw.toByteArray();
        return bytes;
    }

}
