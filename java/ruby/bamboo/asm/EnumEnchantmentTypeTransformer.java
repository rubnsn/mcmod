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

import cpw.mods.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;

public class EnumEnchantmentTypeTransformer implements IClassTransformer,
        Opcodes {

    private final String targetClassName = "net.minecraft.enchantment.EnumEnchantmentType";
    private String enumEnchantmentTypeClassName = "net/minecraft/enchantment/EnumEnchantmentType";
    private String itemClassName = "net/minecraft/item/Item";

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {

        if (transformedName.equals(targetClassName)) {
            if (!name.equals(transformedName)) {
                enumEnchantmentTypeClassName = FMLDeobfuscatingRemapper.INSTANCE.unmap(enumEnchantmentTypeClassName);
                itemClassName = FMLDeobfuscatingRemapper.INSTANCE.unmap(itemClassName);
            }
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
            if (curMnode.desc.equals("(L" + itemClassName + ";)Z")) {
                mnode = curMnode;
                break;
            }
        }
        if (mnode != null) {
            InsnList overrideList = new InsnList();
            overrideList.add(new VarInsnNode(ALOAD, 0));
            overrideList.add(new VarInsnNode(ALOAD, 1));
            overrideList.add(new MethodInsnNode(INVOKESTATIC, "ruby/bamboo/event/enchant/CanEnchantItemEvent", "canEnchantItem", "(L" + enumEnchantmentTypeClassName + ";L" + itemClassName + ";)Z"));
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
