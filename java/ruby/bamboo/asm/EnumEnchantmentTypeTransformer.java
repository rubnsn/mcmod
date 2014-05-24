package ruby.bamboo.asm;

import java.util.List;

import net.minecraft.launchwrapper.IClassTransformer;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class EnumEnchantmentTypeTransformer implements IClassTransformer,
        Opcodes {

    private final String targetClassName = "net.minecraft.enchantment.EnumEnchantmentType";
    private final String targetMethodName = "canEnchantItem";
    private final String targetMethodDesc = "(Lnet/minecraft/item/Item;)Z";

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
        for (MethodNode curMnode : (List<MethodNode>) cnode.methods) {
            if (curMnode.name.equals(targetMethodName) && curMnode.desc.equals(targetMethodDesc)) {
                mnode = curMnode;
                break;
            }
        }
        if (mnode != null) {
            InsnList overrideList = new InsnList();
            overrideList.add(new VarInsnNode(ALOAD, 0));
            overrideList.add(new VarInsnNode(ALOAD, 1));
            overrideList.add(new MethodInsnNode(INVOKESTATIC, "ruby/bamboo/event/enchant/CanEnchantItemEvent", "canEnchantItem", "(Lnet/minecraft/enchantment/EnumEnchantmentType;Lnet/minecraft/item/Item;)Z"));
            LabelNode l1 = new LabelNode();
            overrideList.add(new JumpInsnNode(IFEQ, l1));
            overrideList.add(new InsnNode(4));
            overrideList.add(new InsnNode(IRETURN));
            overrideList.add(l1);

            mnode.instructions.insert(mnode.instructions.get(1), overrideList);

            ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
            cnode.accept(cw);
            bytes = cw.toByteArray();
        }
        return bytes;
    }

}
