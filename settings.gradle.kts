rootProject.name = "shout-parent"

listOf("common", "bukkit", "bungeecord", "sponge7", "sponge8", "velocity").forEach { module ->
    include(module);
    findProject(":${module}")?.name = "shout-${module}";
};