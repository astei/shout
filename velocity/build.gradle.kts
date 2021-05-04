import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

tasks.withType<ShadowJar> {
    // Like Velocity, Sponge 8 already includes a lot of the dependencies we need, except MiniMessage and Configurate 3
    // (though it has the needed dependencies of Configurate 3 at least).

    exclude("org/checkerframework/**")
    exclude("com/google/common/**")
    exclude("com/google/gson/**")

    // Exclude Kyori dependencies, sans MiniMessage
    exclude("net/kyori/examination/**")
    exclude("net/kyori/adventure/audience/**")
    exclude("net/kyori/adventure/bossbar/**")
    exclude("net/kyori/adventure/inventory/**")
    exclude("net/kyori/adventure/key/**")
    exclude("net/kyori/adventure/nbt/**")
    exclude("net/kyori/adventure/sound/**")
    exclude("net/kyori/adventure/text/*")
    exclude("net/kyori/adventure/text/event/**")
    exclude("net/kyori/adventure/text/renderer/**")
    exclude("net/kyori/adventure/text/format/**")
    exclude("net/kyori/adventure/text/serializer/**")
    exclude("net/kyori/adventure/title/**")
    exclude("net/kyori/adventure/util/**")

    // Exclude Configurate and related dependencies (Gson is excluded below)
    exclude("org/yaml/snakeyaml/**")

    // Excludes for Guice, Guava, and Gson
    exclude("org/aopalliance/**" )
    exclude("javax/annotation/**")
    exclude("com/google/**")
    exclude("javax/inject/**")

    // Exclude SLF4J
    exclude("org/slf4j/**")
}

dependencies {
    "implementation"(project(":shout-common"))
    "compileOnly"("com.velocitypowered:velocity-api:1.1.5")
    "annotationProcessor"("com.velocitypowered:velocity-api:1.1.5")
}
