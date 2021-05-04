import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

tasks.withType<ShadowJar> {
    // Bukkit provides SnakeYAML, Guava, and Gson already. Paper adds SLF4J on top of this.

    exclude("org/checkerframework/**")
    exclude("com/google/common/**")
    exclude("com/google/gson/**")

    // Exclude already-provided Configurate dependencies
    exclude("org/yaml/snakeyaml/**")

    // Excludes for Guava and Gson
    exclude("javax/annotation/**")
    exclude("com/google/gson/**")
    exclude("com/google/common/**")
}

dependencies {
    "implementation"(project(":shout-common"))
    // Declare against Spigot 1.8.8 as a reasonable "lowest-common denominator" version. We are (mostly) disinterested
    // in all the APIs that are post-1.13.
    "compileOnly"("org.spigotmc:spigot-api:1.8.8-R0.1-SNAPSHOT")
    "implementation"("org.slf4j:slf4j-jdk14:1.7.30")
    "implementation"("net.kyori:adventure-platform-bukkit:${rootProject.ext["adventurePlatformVersion"]}")
}
