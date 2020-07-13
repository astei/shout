package me.steinborn.shout.config;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.ValueType;

import java.util.regex.Pattern;

public class ComponentConfigurationUtil {
    // This regex attempts (for certain definitions of "attempts") to detect the following cases, so we know the string
    // provided is really in a legacy format:
    //
    // - Kyori-style RGB coloring (&#rrggbb)
    // - Legacy Mojang color codes (&c)
    // - The BungeeCord RGB format, which deserves to be buried underneath the sands of time (&x&r&r&g&g&b&b).
    //   Technically, this format should be covered by the Mojang case, but whatever.
    //
    // We recommend just specifying the format upfront, it will cause less problems in the end.
    private static final Pattern CURSED_LEGACY_CHAT_COLOR_PATTERN = Pattern.compile("&([0-9a-fk-r]|#[a-f0-9]{6}|x(&[a-f0-9]){6})");

    private ComponentConfigurationUtil() {
        throw new AssertionError();
    }

    /**
     * Attempts to deserialize a component from a configuration node. If the node does not exist or is {@code null},
     * it is treated as an empty component.
     *
     * @param in the node containing the component to deserialize
     * @return the deserialized component
     */
    public static Component deserialize(ConfigurationNode in) {
        if (in.getValueType() == ValueType.SCALAR) {
            return guessFormatAndDeserialize(in.getString(""));
        } else if (in.getValueType() == ValueType.MAP) {
            String type = in.getNode("type").getString("");
            String message = in.getNode("message").getString("");

            switch (type) {
                case "json":
                    return GsonComponentSerializer.gson().deserialize(message);
                case "legacy":
                    return LegacyComponentSerializer.legacy('&').deserialize(message);
                case "minimessage":
                    return MiniMessage.get().parse(message);
                case "markdown":
                    return MiniMessage.markdown().parse(message);
                default:
                    // We need to guess the message type
                    return guessFormatAndDeserialize(message);
            }
        } else {
            throw new IllegalArgumentException("Unknown message configuration node type " + in.getValueType());
        }
    }

    /**
     * Attempts to deserialize a component. In order, JSON, legacy, and MiniMessage formats are attempted. JSON uses
     * a simple heuristic check, legacy messages are checked using a regex, and MiniMessage is the fallback.
     *
     * @param in the component to deserialize
     * @return the deserialized component
     */
    public static Component guessFormatAndDeserialize(String in) {
        if (in.startsWith("{") || in.startsWith("[")) {
            // This is a good heuristic as any to attempt JSON deserialization.
            try {
                return GsonComponentSerializer.gson().deserialize(in);
            } catch (Exception ignored) {
                // Ignore the exception. It might be something else after all.
            }
        }

        if (CURSED_LEGACY_CHAT_COLOR_PATTERN.matcher(in).find()) {
            return LegacyComponentSerializer.legacy('&').deserialize(in);
        }

        // Fallback to MiniMessage
        return MiniMessage.get().parse(in);
    }
}
