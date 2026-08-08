package pl.caltonek.chatFusion.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.caltonek.chatFusion.ChatFusion;
import pl.caltonek.chatFusion.util.ColorUtil;

import java.util.Objects;

public final class ConfigManager {

    private final @NotNull ChatFusion plugin;

    private boolean formattingEnabled;
    private @NotNull String noPermissionMode = "STRIP";
    private boolean allowLegacyColors;
    private boolean allowHexColors;
    private boolean allowGradients;
    private boolean allowBold;
    private boolean allowItalic;
    private boolean allowUnderline;
    private boolean allowStrikethrough;
    private boolean allowObfuscated;
    private boolean allowReset;

    private boolean allowClickEvents;
    private boolean allowHoverEvents;
    private boolean allowKeybinds;
    private boolean allowTranslatable;
    private boolean allowFont;
    private boolean allowRainbow;
    private boolean allowTransition;

    public ConfigManager(final @NotNull ChatFusion plugin) {
        this.plugin = Objects.requireNonNull(plugin, "plugin cannot be null");
    }

    public void loadConfigs() {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();

        final FileConfiguration mainConfig = plugin.getConfig();

        this.formattingEnabled = mainConfig.getBoolean("formatting.enabled", true);
        this.noPermissionMode = mainConfig.getString("formatting.no-permission-mode", "STRIP").toUpperCase();
        this.allowLegacyColors = mainConfig.getBoolean("formatting.allow-legacy-colors", true);
        this.allowHexColors = mainConfig.getBoolean("formatting.allow-hex-colors", true);
        this.allowGradients = mainConfig.getBoolean("formatting.allow-gradients", true);
        this.allowBold = mainConfig.getBoolean("formatting.allow-bold", true);
        this.allowItalic = mainConfig.getBoolean("formatting.allow-italic", true);
        this.allowUnderline = mainConfig.getBoolean("formatting.allow-underline", true);
        this.allowStrikethrough = mainConfig.getBoolean("formatting.allow-strikethrough", true);
        this.allowObfuscated = mainConfig.getBoolean("formatting.allow-obfuscated", true);
        this.allowReset = mainConfig.getBoolean("formatting.allow-reset", true);

        this.allowClickEvents = mainConfig.getBoolean("formatting.allow-click-events", false);
        this.allowHoverEvents = mainConfig.getBoolean("formatting.allow-hover-events", false);
        this.allowKeybinds = mainConfig.getBoolean("formatting.allow-keybinds", false);
        this.allowTranslatable = mainConfig.getBoolean("formatting.allow-translatable", false);
        this.allowFont = mainConfig.getBoolean("formatting.allow-font", false);
        this.allowRainbow = mainConfig.getBoolean("formatting.allow-rainbow", true);
        this.allowTransition = mainConfig.getBoolean("formatting.allow-transition", true);
    }

    public @NotNull String filterFormatting(final @Nullable String input) {
        if (input == null || input.isEmpty()) return "";
        if (!formattingEnabled) {
            return ColorUtil.stripFormatting(input);
        }

        String result = input;

        if (!allowClickEvents) {
            result = result.replaceAll("(?i)</?click(:[^>]*)?>", "");
        }
        if (!allowHoverEvents) {
            result = result.replaceAll("(?i)</?hover(:[^>]*)?>", "");
        }
        if (!allowKeybinds) {
            result = result.replaceAll("(?i)</?key(:[^>]*)?>", "")
                    .replaceAll("(?i)</?keybind(:[^>]*)?>", "");
        }
        if (!allowTranslatable) {
            result = result.replaceAll("(?i)</?lang(:[^>]*)?>", "")
                    .replaceAll("(?i)</?tr(:[^>]*)?>", "")
                    .replaceAll("(?i)</?lang_key(:[^>]*)?>", "");
        }
        if (!allowFont) {
            result = result.replaceAll("(?i)</?font(:[^>]*)?>", "");
        }
        if (!allowRainbow) {
            result = result.replaceAll("(?i)</?rainbow(:[^>]*)?>", "")
                    .replaceAll("(?i)\\{#?rainbow\\}", "")
                    .replaceAll("(?i)\\{/#?rainbow\\}", "");
        }
        if (!allowTransition) {
            result = result.replaceAll("(?i)</?transition(:[^>]*)?>", "");
        }

        if (!allowGradients) {
            result = result.replaceAll("(?i)&#[0-9a-fA-F]{6}>(.*?)&#[0-9a-fA-F]{6}<", "$1")
                    .replaceAll("(?i)\\{#[0-9a-fA-F]{6}\\}>(.*?)\\{#[0-9a-fA-F]{6}<\\}", "$1")
                    .replaceAll("(?i)</?gradient(:[^>]*)?>", "");
        }

        if (!allowHexColors) {
            result = result.replaceAll("(?i)&#[0-9a-fA-F]{6}", "")
                    .replaceAll("(?i)\\{#[0-9a-fA-F]{6}\\}", "");
        }

        if (!allowLegacyColors) {
            result = result.replaceAll("(?i)[&§][0-9a-f]", "");
        }

        if (!allowBold)          result = result.replaceAll("(?i)[&§]l", "");
        if (!allowItalic)        result = result.replaceAll("(?i)[&§]o", "");
        if (!allowUnderline)     result = result.replaceAll("(?i)[&§]n", "");
        if (!allowStrikethrough) result = result.replaceAll("(?i)[&§]m", "");
        if (!allowObfuscated)    result = result.replaceAll("(?i)[&§]k", "");
        if (!allowReset)         result = result.replaceAll("(?i)[&§]r", "");

        return result;
    }

    public @NotNull String getCleanMessage(final @Nullable String input) {
        return ColorUtil.stripFormatting(input);
    }

    public boolean isFormattingEnabled() { return formattingEnabled; }
    public @NotNull String getNoPermissionMode() { return noPermissionMode; }
    public boolean isAllowLegacyColors() { return allowLegacyColors; }
    public boolean isAllowHexColors() { return allowHexColors; }
    public boolean isAllowGradients() { return allowGradients; }
    public boolean isAllowBold() { return allowBold; }
    public boolean isAllowItalic() { return allowItalic; }
    public boolean isAllowUnderline() { return allowUnderline; }
    public boolean isAllowStrikethrough() { return allowStrikethrough; }
    public boolean isAllowObfuscated() { return allowObfuscated; }
    public boolean isAllowReset() { return allowReset; }
    public boolean isAllowClickEvents() { return allowClickEvents; }
    public boolean isAllowHoverEvents() { return allowHoverEvents; }
    public boolean isAllowKeybinds() { return allowKeybinds; }
    public boolean isAllowTranslatable() { return allowTranslatable; }
    public boolean isAllowFont() { return allowFont; }
    public boolean isAllowRainbow() { return allowRainbow; }
    public boolean isAllowTransition() { return allowTransition; }
}