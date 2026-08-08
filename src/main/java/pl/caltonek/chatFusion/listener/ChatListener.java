package pl.caltonek.chatFusion.listener;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import pl.caltonek.chatFusion.config.ConfigManager;
import pl.caltonek.chatFusion.util.ColorUtil;

import java.util.Objects;

public class ChatListener implements Listener {

    private final @NotNull ConfigManager configManager;

    public ChatListener(final @NotNull ConfigManager configManager) {
        this.configManager = Objects.requireNonNull(configManager, "configManager cannot be null");
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onChat(final @NotNull AsyncChatEvent event) {
        final Player player = event.getPlayer();

        final String rawMessage = PlainTextComponentSerializer.plainText().serialize(event.message());

        final String cleanMessage = configManager.getCleanMessage(rawMessage);

        final boolean allowLegacy     = player.hasPermission("chatfusion.chat.color.legacy") && configManager.isAllowLegacyColors();
        final boolean allowHex        = player.hasPermission("chatfusion.chat.color.rgb.hex") && configManager.isAllowHexColors();
        final boolean allowGradient   = player.hasPermission("chatfusion.chat.color.rgb.gradient") && configManager.isAllowGradients();
        final boolean allowRainbow    = player.hasPermission("chatfusion.chat.color.rgb.rainbow") && configManager.isAllowRainbow();
        final boolean allowTransition = player.hasPermission("chatfusion.chat.color.rgb.transition") && configManager.isAllowTransition();

        final boolean allowBold          = player.hasPermission("chatfusion.chat.format.bold") && configManager.isAllowBold();
        final boolean allowItalic        = player.hasPermission("chatfusion.chat.format.italic") && configManager.isAllowItalic();
        final boolean allowUnderline     = player.hasPermission("chatfusion.chat.format.underline") && configManager.isAllowUnderline();
        final boolean allowStrikethrough = player.hasPermission("chatfusion.chat.format.strikethrough") && configManager.isAllowStrikethrough();
        final boolean allowObfuscated    = player.hasPermission("chatfusion.chat.format.obfuscated") && configManager.isAllowObfuscated();
        final boolean allowReset         = player.hasPermission("chatfusion.chat.format.reset") && configManager.isAllowReset();

        final boolean allowClick        = player.hasPermission("chatfusion.chat.advanced.click") && configManager.isAllowClickEvents();
        final boolean allowHover        = player.hasPermission("chatfusion.chat.advanced.hover") && configManager.isAllowHoverEvents();
        final boolean allowKeybind      = player.hasPermission("chatfusion.chat.advanced.keybind") && configManager.isAllowKeybinds();
        final boolean allowTranslatable = player.hasPermission("chatfusion.chat.advanced.translatable") && configManager.isAllowTranslatable();
        final boolean allowFont         = player.hasPermission("chatfusion.chat.advanced.font") && configManager.isAllowFont();

        final boolean hasAnyPermission = allowLegacy || allowHex || allowGradient || allowRainbow || allowTransition
                || allowBold || allowItalic || allowUnderline || allowStrikethrough || allowObfuscated || allowReset
                || allowClick || allowHover || allowKeybind || allowTranslatable || allowFont;

        if (!hasAnyPermission) {
            if ("RAW".equalsIgnoreCase(configManager.getNoPermissionMode())) {
                event.message(Component.text(rawMessage));
            } else {
                event.message(Component.text(cleanMessage));
            }
            return;
        }

        String processed = rawMessage;

        if (!allowClick)        processed = processed.replaceAll("(?i)</?click(:[^>]*)?>", "");
        if (!allowHover)        processed = processed.replaceAll("(?i)</?hover(:[^>]*)?>", "");
        if (!allowKeybind)      processed = processed.replaceAll("(?i)</?key(:[^>]*)?>", "").replaceAll("(?i)</?keybind(:[^>]*)?>", "");
        if (!allowTranslatable) processed = processed.replaceAll("(?i)</?lang(:[^>]*)?>", "").replaceAll("(?i)</?tr(:[^>]*)?>", "").replaceAll("(?i)</?lang_key(:[^>]*)?>", "");
        if (!allowFont)         processed = processed.replaceAll("(?i)</?font(:[^>]*)?>", "");

        if (!allowRainbow)      processed = processed.replaceAll("(?i)</?rainbow(:[^>]*)?>", "").replaceAll("(?i)\\{#?rainbow\\}", "").replaceAll("(?i)\\{/#?rainbow\\}", "");
        if (!allowTransition)   processed = processed.replaceAll("(?i)</?transition(:[^>]*)?>", "");
        if (!allowGradient)     processed = processed.replaceAll("(?i)&#[0-9a-fA-F]{6}>(.*?)&#[0-9a-fA-F]{6}<", "$1").replaceAll("(?i)\\{#[0-9a-fA-F]{6}\\}>(.*?)\\{#[0-9a-fA-F]{6}<\\}", "$1").replaceAll("(?i)</?gradient(:[^>]*)?>", "");
        if (!allowHex)          processed = processed.replaceAll("(?i)&#[0-9a-fA-F]{6}", "").replaceAll("(?i)\\{#[0-9a-fA-F]{6}\\}", "");
        if (!allowLegacy)       processed = processed.replaceAll("(?i)[&§][0-9a-fA-F]", "");

        if (!allowBold)          processed = processed.replaceAll("(?i)[&§]l", "");
        if (!allowItalic)        processed = processed.replaceAll("(?i)[&§]o", "");
        if (!allowUnderline)     processed = processed.replaceAll("(?i)[&§]n", "");
        if (!allowStrikethrough) processed = processed.replaceAll("(?i)[&§]m", "");
        if (!allowObfuscated)    processed = processed.replaceAll("(?i)[&§]k", "");
        if (!allowReset)         processed = processed.replaceAll("(?i)[&§]r", "");

        final Component coloredMessage = ColorUtil.color(processed);
        event.message(coloredMessage);
    }
}