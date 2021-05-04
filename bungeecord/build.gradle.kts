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
    "compileOnly"("net.md-5:bungeecord-api:1.16-R0.2-SNAPSHOT")
    "implementation"("org.slf4j:slf4j-jdk14:1.7.30")
    "implementation"("net.kyori:adventure-platform-bungeecord:${rootProject.ext["adventurePlatformVersion"]}")
}
