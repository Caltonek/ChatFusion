package pl.caltonek.chatFusion;

import org.bukkit.plugin.java.JavaPlugin;
import pl.caltonek.chatFusion.config.ConfigManager;
import pl.caltonek.chatFusion.listener.ChatListener;

public final class ChatFusion extends JavaPlugin {

    @Override
    public void onEnable() {
        ConfigManager configManager = new ConfigManager(this);
        configManager.loadConfigs();

        getServer().getPluginManager().registerEvents(new ChatListener(configManager), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
