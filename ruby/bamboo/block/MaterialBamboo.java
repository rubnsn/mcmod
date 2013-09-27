package ruby.bamboo.block;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

public class MaterialBamboo extends Material
{
    public static MaterialBamboo instance = new MaterialBamboo();
    public MaterialBamboo()
    {
        super(MapColor.foliageColor);
        setNoPushMobility();
    }
    @Override
    public boolean isSolid()
    {
        return false;
    }

    @Override
    public boolean getCanBlockGrass()
    {
        return false;
    }

    @Override
    public boolean blocksMovement()
    {
        return true;
    }
}
