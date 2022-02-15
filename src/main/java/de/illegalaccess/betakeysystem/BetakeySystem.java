package de.illegalaccess.betakeysystem;

import de.illegalaccess.betakeysystem.commands.ActivateCommand;
import de.illegalaccess.betakeysystem.commands.CreateKeyCommand;
import de.illegalaccess.betakeysystem.commands.DeleteKeyCommand;
import de.illegalaccess.betakeysystem.listener.PlayerLoginListener;
import de.illegalaccess.betakeysystem.utils.BetakeyManager;
import de.illegalaccess.betakeysystem.utils.MySQL;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.UUID;

public final class BetakeySystem extends Plugin {
    //Trigger warning: STATIC ABUSE
    private static MySQL mySQL;
    private static BetakeyManager betakeyManager;

    public static Configuration config;
    {
        try {
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "config.yml"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    @Override
    public void onEnable() {
        // Plugin startup logic
        createFiles();
        mySQL = new MySQL();
        mySQL.createTabels();

        ProxyServer.getInstance().getPluginManager().registerCommand(this, new CreateKeyCommand());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new DeleteKeyCommand());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new ActivateCommand());
        ProxyServer.getInstance().getPluginManager().registerListener(this, new PlayerLoginListener());

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static MySQL getMySQL() {
        return mySQL;
    }

    private void createFiles() {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, new File(getDataFolder(), "config.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }
        File file = new File(getDataFolder(), "config.yml");
        if (!file.exists()) {
            try (InputStream in = getResourceAsStream("config.yml")) {
                Files.copy(in, file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
