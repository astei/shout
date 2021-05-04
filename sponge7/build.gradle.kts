import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

tasks.withType<ShadowJar> {
    // Sponge provides Configurate, SLF4J, Guava, Guice, and Gson already.

    exclude("org/checkerframework/**")
    exclude("com/google/common/**")
    exclude("com/google/gson/**")

    // Exclude Configurate and related dependencies (Gson is excluded below)
    exclude("ninja/leaping/configurate/**")
    exclude("org/yaml/snakeyaml/**")

    // Excludes for Guice, Guava, and Gson
    exclude("org/aopalliance/**")
    exclude("javax/annotation/**")
    exclude("com/google/**")
    exclude("javax/inject/**")

    // Exclude SLF4J
    exclude("org/slf4j/**")
}

dependencies {
    "implementation"(project(":shout-common"))
    "compileOnly"("org.spongepowered:spongeapi:7.2.0")
    "implementation"("net.kyori:adventure-platform-spongeapi:${rootProject.ext["adventurePlatformVersion"]}")
}
