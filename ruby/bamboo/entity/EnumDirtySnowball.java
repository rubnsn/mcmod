package ruby.bamboo.entity;

public enum EnumDirtySnowball
{
    stone(0, 1),
    ice(1, 2),
    iron(2, 3),
    gold(3, 8),
    diamond(4, 6),
    compress(5, 1),
    ender(6, 0),
    confusion(7, 0),
    poison(8, 0),
    heal(9, 0),
    ;
    EnumDirtySnowball(int id, int dmg)
    {
        this.id = id;
        this.dmg = (byte)dmg;
    }
    private int id;
    private byte dmg;
    public int getId()
    {
        return id;
    }
    public byte getDmg()
    {
        return dmg;
    }
}
