package net.myriantics.myrror.core;

public final class MyrrorPack {
    private final String[] namespaces;

    private MyrrorPack(String[] namespaces) {
        this.namespaces = namespaces;
    }

    public static MyrrorPack create(String... namespaces) {
        return new MyrrorPack(namespaces);
    }

    public void addProvider() {

    }
}
