import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    `java-library`
    id("com.github.johnrengelman.shadow") version "6.0.0" apply false
}

group = "me.steinborn.shout"
version = "0.1.0-SNAPSHOT"

extra.apply {
    set("adventureVersion", "4.7.0")
    set("adventure", "net.kyori:adventure-api:${extra["adventureVersion"]}")
    set("adventureGsonSerializer", "net.kyori:adventure-text-serializer-gson:${extra["adventureVersion"]}")
    set("adventureLegacySerializer", "net.kyori:adventure-text-serializer-legacy:${extra["adventureVersion"]}")
    set("adventureMiniMessage", "net.kyori:adventure-text-minimessage:3.0.0-SNAPSHOT")
    set("adventurePlatformVersion", "4.0.0-SNAPSHOT")
    set("configurateVersion", "3.7.1")
    set("configurate", "org.spongepowered:configurate-core:${extra["configurateVersion"]}")
    set("configurateYaml", "org.spongepowered:configurate-yaml:${extra["configurateVersion"]}")
    set("configurateGson", "org.spongepowered:configurate-gson:${extra["configurateVersion"]}")
    set("guice", "com.google.inject:guice:4.2.3:no_aop")
    set("slf4jVersion", "1.7.30")
    set("slf4j", "org.slf4j:slf4j-api:${extra["slf4jVersion"]}")
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "com.github.johnrengelman.shadow")

    tasks.withType<ShadowJar> {
        archiveClassifier.set("")
    }

    java {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    repositories {
        mavenCentral()
        maven("https://oss.sonatype.org/content/groups/public/") {
            name = "sonatype-snapshots"
        }
        maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") {
            name = "spigot-repo"
        }
        maven("https://repo.spongepowered.org/maven/") {
            name = "sponge-repo"
        }
        maven("https://nexus.velocitypowered.com/repository/maven-public/") {
            name = "velocity-repo"
        }
    }
}