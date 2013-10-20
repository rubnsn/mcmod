package ruby.togglesneak.asm;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import cpw.mods.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
import net.minecraft.launchwrapper.IClassTransformer;

public class MovementInputFromOptionsTransformer implements IClassTransformer,
        Opcodes {
    private static final String TARGET_CLASS_NAME = "net.minecraft.util.MovementInputFromOptions";

    //name:実機だと難読化後名 arg1常に簡易名
    @Override
    public byte[] transform(String name, String arg1, byte[] bytes) {
        if (arg1.equals(TARGET_CLASS_NAME)) {
            return patch(name, bytes);
        }

        return bytes;
    }

    private byte[] patch(String name, byte[] bytes) {
        String targetPath = name.replace('.', '/');
        ClassNode cnode = new ClassNode();
        ClassReader reader = new ClassReader(bytes);
        reader.accept(cnode, 0);
        String toggleSneakPath = ToggleSneak.class.getName().replace('.', '/');
        MethodNode mnode = null;
        FieldNode fnode = null;
        // メソッドサーチ
        for (MethodNode curMnode : cnode.methods) {
            if (curMnode.desc.equals("()V")) {
                mnode = curMnode;
                break;
            }
        }
        fnode = cnode.fields.get(0);
        //フィールドサーチ
        //上手いこと行かないからクソファック
        /*for (FieldNode curFnode : cnode.fields) {
            if (FMLDeobfuscatingRemapper.INSTANCE.mapFieldName(name, curFnode.name, curFnode.desc).equals("gameSettings")) {
                fnode = curFnode;
            }
        }*/
        if (mnode != null) {
            // リターン挿入
            mnode.instructions.insert(mnode.instructions.get(1), new InsnNode(RETURN));
            // フック追加
            InsnList overrideList = new InsnList();
            overrideList.add(new VarInsnNode(ALOAD, 0));
            overrideList.add(new VarInsnNode(ALOAD, 0));
            overrideList.add(new FieldInsnNode(GETFIELD, targetPath, fnode.name, fnode.desc));
            overrideList.add(new MethodInsnNode(INVOKESTATIC, toggleSneakPath, "hook", "(L" + targetPath + ";" + fnode.desc + ")V"));
            mnode.instructions.insert(mnode.instructions.get(1), overrideList);
            // クラスライター
            ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
            cnode.accept(cw);
            bytes = cw.toByteArray();
        }

        return bytes;
    }

    private String unmap(String typeName) {
        return FMLDeobfuscatingRemapper.INSTANCE.unmap(typeName);
    }

}
