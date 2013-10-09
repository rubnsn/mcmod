package ruby.bamboo.item;

import net.minecraft.util.Icon;

public enum EnumFood {
    MUGIMESI(0, 3, 36), GYUMESI(1, 10, 16), BUTAMESI(2, 12, 48), KINOKOMESI(3, 6, 32), BUTAKUSI(4, 10, 16), GYUKUSI(5, 8, 8), TAKEMESI(6, 5, 32), TAMAKAKE(7, 5, 24), OYAKO(8, 12, 64), TEKKA(9, 7, 36), TORIKUSI(10, 9, 12);

    private EnumFood(int dmgid, int heal, int duration) {
        this.dmgid = dmgid;
        this.heal = heal;
        this.duration = duration;
    }

    private int dmgid;
    private int heal;
    private int duration;
    private Icon tex;

    public void setTex(Icon tex) {
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

    public Icon getTex() {
        return tex;
    }
}
