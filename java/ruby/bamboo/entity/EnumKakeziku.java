package ruby.bamboo.entity;

public enum EnumKakeziku {
    Tatu("Tatu", 0, "Tatu", 16, 32, 0, 0), Curi("Wa", 1, "Wa", 16, 32, 16, 0), Moools("Moools", 2, "Moools", 16, 32, 32, 0), KWa("KWa", 3, "KWa", 16, 32, 48, 0), Byakko("Byakko", 4, "Byakko", 16, 32, 64, 0), Genbu("Genbu", 5, "Genbu", 16, 32, 80, 0), Suzaku("Suzaku", 6, "Suzaku", 16, 32, 96, 0), Maru("Maru", 7, "Maru", 16, 32, 112, 0), Ika("Ika", 8, "Ika", 16, 32, 128, 0), Zon("Zon", 9, "Zon", 16, 32, 144, 0), Suke("Suke", 10, "Suke", 16, 32, 160, 0), Usi("Usi", 11, "Usi", 16, 32, 176, 0), Matsu("Mathu", 12, "Matsh", 16, 48, 0, 32), Ume("Ume", 13, "Ume", 16, 48, 16, 32), Sakura("Sakura", 14, "Sakura", 16, 48, 32, 32), Huzi("Huzi", 15, "Huzi", 16, 48, 48, 32), Ayame("Ayame", 16, "Ayame", 16, 48, 64, 32), Botan("Botan", 17, "Botan", 16, 48, 80, 32), Hagi("Hagi", 18, "Hagi", 16, 48, 96, 32), Susuki("Susuki", 19, "Susuki", 16, 48, 112, 32), Kiku("Kiku", 20, "Kiku", 16, 48, 128, 32), Momizi("Momizi", 21, "Momizi", 16, 48, 144, 32), Yanagi("Yanagi", 22, "Yanagi", 16, 48, 160, 32), Kiri("Kiri", 23, "Kiri", 16, 48, 176, 32);

    private EnumKakeziku(String s, int i, String s1, int j, int k, int l, int i1) {
        title = s1;
        sizeX = j;
        sizeY = k;
        offsetX = l;
        offsetY = i1;
    }

    public static final int maxArtTitleLength = "Moools".length();
    public final String title;
    public final int sizeX;
    public final int sizeY;
    public final int offsetX;
    public final int offsetY;
}
