package ruby.bamboo.asm;

import java.util.List;

import net.minecraft.launchwrapper.IClassTransformer;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FrameNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

public class EnumEnchantmentTypeTransformer implements IClassTransformer,
        Opcodes {

    private final String targetClassName = "net.minecraft.enchantment.EnumEnchantmentType";
    private final String targetMethodName = "canEnchantItem";
    private final String targetMethodDesc = "(Lnet/minecraft/item/Item;)Z";
    private final String eventClass = "ruby/bamboo/event/enchant/CanEnchantItemEvent";
    private final String eventClasssDesc = "(Lnet/minecraft/enchantment/EnumEnchantmentType;Lnet/minecraft/item/Item;)V";

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
            overrideList.add(new LabelNode());
            overrideList.add(new TypeInsnNode(187, eventClass));
            overrideList.add(new InsnNode(89));
            overrideList.add(new VarInsnNode(25, 0));
            overrideList.add(new VarInsnNode(25, 1));
            overrideList.add(new MethodInsnNode(183, eventClass, "<init>", eventClasssDesc));
            overrideList.add(new VarInsnNode(58, 2));
            overrideList.add(new LabelNode());
            overrideList.add(new FieldInsnNode(178, "net/minecraftforge/common/MinecraftForge", "EVENT_BUS", "Lcpw/mods/fml/common/eventhandler/EventBus;"));
            overrideList.add(new VarInsnNode(25, 2));
            overrideList.add(new MethodInsnNode(182, "cpw/mods/fml/common/eventhandler/EventBus", "post", "(Lcpw/mods/fml/common/eventhandler/Event;)Z"));
            LabelNode l1 = new LabelNode();
            overrideList.add(new JumpInsnNode(153, l1));
            overrideList.add(new LabelNode());
            overrideList.add(new VarInsnNode(25, 2));
            overrideList.add(new MethodInsnNode(182, eventClass, "getResult", "()Lcpw/mods/fml/common/eventhandler/Event$Result;"));
            overrideList.add(new FieldInsnNode(178, "cpw/mods/fml/common/eventhandler/Event$Result", "ALLOW", "Lcpw/mods/fml/common/eventhandler/Event$Result;"));
            LabelNode l2 = new LabelNode();
            overrideList.add(new JumpInsnNode(166, l2));
            overrideList.add(new InsnNode(4));
            overrideList.add(new InsnNode(172));
            overrideList.add(l2);
            overrideList.add(new FrameNode(14, 1, new Object[] { eventClass }, 0, null));
            overrideList.add(new InsnNode(3));
            overrideList.add(new InsnNode(172));
            overrideList.add(l1);
            overrideList.add(new FrameNode(14, 0, null, 0, null));
            /*overrideList.add(new InsnNode(4));
            overrideList.add(new InsnNode(172));
            overrideList.add(new LabelNode());*/

            mnode.instructions.insert(mnode.instructions.get(1), overrideList);

            ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
            cnode.accept(cw);
            bytes = cw.toByteArray();
        }
        return bytes;
    }

}
