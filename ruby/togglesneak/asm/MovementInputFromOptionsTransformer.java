package ruby.togglesneak.asm;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import cpw.mods.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
import cpw.mods.fml.relauncher.FMLLaunchHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.util.MovementInputFromOptions;

public class MovementInputFromOptionsTransformer implements IClassTransformer,Opcodes
{
    private static final String TARGET_CLASS_NAME = "net.minecraft.util.MovementInputFromOptions";

    //name:実機だと難読化後名 arg1常に簡易名
    @Override
    public byte[] transform(String name, String arg1, byte[] bytes)
    {
        if (arg1.equals(TARGET_CLASS_NAME))
        {
           return patch(name, bytes);
        }

        return bytes;
    }

    private byte[] patch(String name, byte[] bytes)
    {
        String targetPath = name.replace('.', '/');
        ClassNode cnode = new ClassNode();
        ClassReader reader = new ClassReader(bytes);
        reader.accept(cnode, 0);
        String gameSettingsClassPath = "net/minecraft/client/settings/GameSettings";
        String toggleSneakPath = ToggleSneak.class.getName().replace('.', '/');
        MethodNode mnode = null;
        FieldNode fnode=null;
        // メソッドサーチ
        for (MethodNode curMnode : (List<MethodNode>) cnode.methods)
        {
            if (curMnode.desc.equals("()V"))
            {
                mnode = curMnode;
                break;
            }
        }
        System.out.println(this.unmap(gameSettingsClassPath));
        //実機難読化
        if (name.indexOf('.') == -1)
        {
            gameSettingsClassPath = this.unmap(gameSettingsClassPath);
        }
        
        //フィールドサーチ
        for (FieldNode curFnode : (List<FieldNode>) cnode.fields)
        {
            if (curFnode.desc.equals("L" + gameSettingsClassPath + ";"))
            {
            	fnode = curFnode;
            }
        }

        if (mnode != null)
        {
            // リターン挿入
            mnode.instructions.insert(mnode.instructions.get(1), new InsnNode(
                                          RETURN));
            // フック追加
            InsnList overrideList = new InsnList();
            overrideList.add(new VarInsnNode(ALOAD, 0));
            overrideList.add(new VarInsnNode(ALOAD, 0));
            overrideList.add(new FieldInsnNode(GETFIELD, targetPath,fnode.name,fnode.desc));
            overrideList.add(new MethodInsnNode(INVOKESTATIC, toggleSneakPath, "hook", "(L" + targetPath + ";L" + gameSettingsClassPath + ";)V"));
            mnode.instructions.insert(mnode.instructions.get(1), overrideList);
            // クラスライター
            ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES
                                             | ClassWriter.COMPUTE_MAXS);
            cnode.accept(cw);
            bytes = cw.toByteArray();
        }

        return bytes;
    }

    private String unmap(String typeName)
    {
        return FMLDeobfuscatingRemapper.INSTANCE.unmap(typeName);
    }


}
