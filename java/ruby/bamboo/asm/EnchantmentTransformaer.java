package ruby.bamboo.asm;

import net.minecraft.launchwrapper.IClassTransformer;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class EnchantmentTransformaer implements IClassTransformer, Opcodes {
    private final String targetClassName = "net.minecraft.enchantment.EnchantmentHelper";
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
        MethodNode mnode = null;

        for (MethodNode m : cnode.methods) {
            if (m.name.equals(targetMethodName) && m.desc.equals(targetMethodDesc)) {
                mnode = m;
                break;
            }
        }
        if (mnode != null) {
            int insertIndex;
            int storeIndex = 0;
            for (insertIndex = 0; insertIndex < mnode.instructions.size(); insertIndex++) {
                if (mnode.instructions.get(insertIndex) instanceof MethodInsnNode) {
                    if (((MethodInsnNode) mnode.instructions.get(insertIndex)).name.equals("getItemEnchantability")) {
                        break;
                    }
                }
            }
            mnode.instructions.remove(mnode.instructions.get(insertIndex));
            InsnList overrideList = new InsnList();
            overrideList.add(new MethodInsnNode(INVOKESTATIC, "ruby/bamboo/event/enchant/ItemEnchantabilityEvent", "getItemEnchantability", "(Lnet/minecraft/item/Item;)I"));
            mnode.instructions.insert(mnode.instructions.get(insertIndex - 1), overrideList);

            ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
            cnode.accept(cw);
            bytes = cw.toByteArray();
        }
        return bytes;
    }

}
