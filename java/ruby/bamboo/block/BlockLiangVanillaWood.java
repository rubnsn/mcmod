package ruby.bamboo.block;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.block.BlockWood;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;

public class BlockLiangVanillaWood extends BlockLiangBase {

    public BlockLiangVanillaWood(float minWidth, float maxWidth, float minHeight, float maxHeight) {
        super(minWidth, maxWidth, minHeight, maxHeight);
    }

    @Override
    public void registerBlockIcons(IIconRegister p_149651_1_) {
        List<IIcon> i = new ArrayList<IIcon>();
        try {
            Field f = BlockWood.class.getDeclaredField("field_150095_b");
            f.setAccessible(true);
            i.addAll(Arrays.asList((IIcon[]) f.get(Blocks.planks)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        icons = i.toArray(new IIcon[0]);
    }

}
