package ruby.bamboo.item;

import net.minecraft.util.IIcon;

public enum EnumFood {
    MUGIMESI(0, 3, 36),
    GYUMESI(1, 10, 16),
    BUTAMESI(2, 12, 48),
    KINOKOMESI(3, 6, 32),
    BUTAKUSI(4, 10, 16),
    GYUKUSI(5, 8, 8),
    TAKEMESI(6, 5, 32),
    TAMAKAKE(7, 5, 24),
    OYAKO(8, 12, 64),
    TEKKA(9, 7, 36),
    TORIKUSI(10, 9, 12),
    UMEONI(11, 4, 18),
    SAKEONI(12, 7, 24),
    TUNAONI(13, 8, 24),
    KINOONI(14, 5, 24),
    TAKEONI(15, 6, 24),
    WAKAMEONI(16, 6, 24),
    DANANKO(17, 7, 16),
    DANKINAKO(18, 7, 16),
    DANMITARASHI(19, 7, 16),
    DANSANSYOKU(20, 7, 16),
    DANZUNDA(21, 7, 16),
    MOCHI(22, 6, 72),
    COOKEDMOCHI(23, 6, 36),
    OHAANKO(24, 7, 36),
    OHAKINAKO(25, 7, 36),
    OHAZUNDA(26, 7, 36),
    NATTO(27, 2, 16),
    NATTOMESHI(28, 5, 16),
    TAMANATTOMESHI(29, 7, 16),
    SAKURAMOCHI(30, 7, 36),
    TAMAGYUMESHI(31, 11, 24),
    KATSUDON(32, 11, 24),
    SEKIHAN(33, 5, 36),
    ONISEKIHAN(34, 7, 28), ;

    private EnumFood(int dmgid, int heal, int duration) {
        this.dmgid = dmgid;
        this.heal = heal;
        this.duration = duration;
    }

    private int dmgid;
    private int heal;
    private int duration;
    private IIcon tex;

    public void setTex(IIcon tex) {
        this.tex = tex;
    }

    public int getId() {
        return dmgid;
    }

    public int getHeal() {
        return heal;
    }

    public int getDuration() {
        return duration;
    }

    public IIcon getTex() {
        return tex;
    }
}
