package me.steinborn.shout.platform;

public class PlatformVersion {
    private final String name;
    private final String version;

    public PlatformVersion(String name, String version) {
        this.name = name;
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    @Override
    public String toString() {
        return "PlatformVersion{" +
                "name='" + name + '\'' +
                ", version='" + version + '\'' +
                '}';
    }
}
