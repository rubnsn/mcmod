package ruby.bamboo.asm;

import java.util.ListIterator;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FrameNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LineNumberNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

public class ASMUtil {
    //コピペ用に出力する※Jumpラベルは人力
    public static void createNodeCode(MethodNode mnode) {
        System.out.println("------------------start-------------------");
        ListIterator<AbstractInsnNode> ite = mnode.instructions.iterator();
        while (ite.hasNext()) {
            StringBuilder builder = new StringBuilder();
            AbstractInsnNode ab = ite.next();
            if (ab instanceof LineNumberNode) {
                continue;
            }
            builder.append("overrideList.add(");
            if (ab instanceof LabelNode) {
                builder.append("new LabelNode()");
            } else if (ab instanceof TypeInsnNode) {
                builder.append("new TypeInsnNode(" + ab.getOpcode() + ",\"" + ((TypeInsnNode) ab).desc + "\")");
            } else if (ab instanceof MethodInsnNode) {
                builder.append("new MethodInsnNode(" + ab.getOpcode() + ",\"" + ((MethodInsnNode) ab).owner + "\",\"" + ((MethodInsnNode) ab).name + "\",\"" + ((MethodInsnNode) ab).desc + "\")");

            } else if (ab instanceof FieldInsnNode) {
                builder.append("new FieldInsnNode(" + ab.getOpcode() + ",\"" + ((FieldInsnNode) ab).owner + "\",\"" + ((FieldInsnNode) ab).name + "\",\"" + ((FieldInsnNode) ab).desc + "\")");

            } else if (ab instanceof VarInsnNode) {
                builder.append("new VarInsnNode(" + ab.getOpcode() + "," + ((VarInsnNode) ab).var + ")");
            } else if (ab instanceof InsnNode) {
                builder.append("new InsnNode(" + ab.getOpcode() + ")");
            } else if (ab instanceof JumpInsnNode) {
                builder.append("new JumpInsnNode(" + ab.getOpcode() + "," + ((JumpInsnNode) ab).label + ")");
            } else if (ab instanceof FrameNode) {
                builder.append("new FrameNode(" + ab.getType() + ",");
                if (((FrameNode) ab).local != null) {
                    builder.append(((FrameNode) ab).local.size() + ",");
                    builder.append("new Object[]{");
                    for (Object obj : ((FrameNode) ab).local) {
                        builder.append("\"" + obj + "\"");
                        if (((FrameNode) ab).local.size() > 1) {
                            builder.append(",");
                        }
                    }
                    builder.append("}");
                } else {
                    builder.append("0,null");
                }
                builder.append(",");
                if (((FrameNode) ab).stack != null) {
                    builder.append("," + ((FrameNode) ab).stack.size() + ",");
                    builder.append("new Object[]{");
                    for (Object obj : ((FrameNode) ab).stack) {
                        builder.append("\"" + obj + "\"");
                        if (((FrameNode) ab).stack.size() > 1) {
                            builder.append(",");
                        }
                    }
                    builder.append("}");
                } else {
                    builder.append("0,null");
                }
                builder.append(")");
            } else {
                builder.append(ab.toString() + " ");
            }
            builder.append(");");

            if (ab instanceof LabelNode) {
                builder.append("//" + ab.toString());
            }
            System.out.println(builder);
        }
        System.out.println("------------------end-------------------");
    }
}
