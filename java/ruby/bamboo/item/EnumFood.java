package ruby.bamboo.item;

import net.minecraft.util.IIcon;

public enum EnumFood {
    MUGIMESI(0, 3, 36, 1, 0),
    GYUMESI(1, 10, 16, 1, 0),
    BUTAMESI(2, 12, 48, 1, 0),
    KINOKOMESI(3, 6, 32, 1, 0),
    BUTAKUSI(4, 10, 16, 0, 0),
    GYUKUSI(5, 8, 8, 0, 0),
    TAKEMESI(6, 5, 32, 1, 0),
    TAMAKAKE(7, 5, 24, 1, 0.5F),
    OYAKO(8, 12, 64, 1, 0.5F),
    TEKKA(9, 7, 36, 2, 2),
    TORIKUSI(10, 9, 12, 0, 0),
    UMEONI(11, 4, 18, 1, 0.5F),
    SAKEONI(12, 7, 24, 2, 1.5F),
    TUNAONI(13, 8, 24, 2, 1.5F),
    KINOONI(14, 5, 24, 1, 0.5F),
    TAKEONI(15, 6, 24, 1, 0.5F),
    WAKAMEONI(16, 6, 24, 1, 1F),
    DANANKO(17, 7, 16, 0, 0),
    DANKINAKO(18, 7, 16, 0, 0),
    DANMITARASHI(19, 7, 16, 0, 0),
    DANSANSYOKU(20, 7, 16, 0, 0),
    DANZUNDA(21, 7, 16, 0, 0),
    MOCHI(22, 6, 72, -2, 0),
    COOKEDMOCHI(23, 6, 36, 0, 0),
    OHAANKO(24, 7, 36, 0, 0),
    OHAKINAKO(25, 7, 36, 0, 0),
    OHAZUNDA(26, 7, 36, 0, 0),
    NATTO(27, 2, 16, 0, 0),
    NATTOMESHI(28, 5, 16, 1, 0),
    TAMANATTOMESHI(29, 7, 16, 1, 0.5F),
    SAKURAMOCHI(30, 7, 36, 0, 0),
    TAMAGYUMESHI(31, 11, 24, 1, 0),
    KATSUDON(32, 11, 24, 1, 0),
    SEKIHAN(33, 5, 36, 0, 0),
    ONISEKIHAN(34, 7, 28, 1, 0),
    TOFU(35, 1, 10, 1, 1),
    AGEDASHI(36, 3, 16, 1, 1),
    MEN(37, 1, 32, 0, 0),
    udon(38, 10, 64, 2, 2),
    soba(39, 12, 64, 2, 2),
    ramen(40, 18, 128, 3, 3);

    private EnumFood(int dmgid, int heal, int duration, int water, float waterS) {
        this.dmgid = dmgid;
        this.heal = heal;
        this.duration = duration;
        this.water = water;
        this.waterS = waterS;
    }

    public final int dmgid;
    public final int heal;
    public final int duration;
    public final int water;
    public final float waterS;
    private IIcon tex;

    public void setTex(IIcon tex) {
        this.tex = tex;
    }

    public IIcon getTex() {
        return tex;
    }
}
