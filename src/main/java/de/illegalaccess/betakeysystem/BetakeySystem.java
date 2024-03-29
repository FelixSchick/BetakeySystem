package de.illegalaccess.betakeysystem;

import de.illegalaccess.betakeysystem.commands.ActivateCommand;
import de.illegalaccess.betakeysystem.commands.CreateKeyCommand;
import de.illegalaccess.betakeysystem.commands.DeleteKeyCommand;
import de.illegalaccess.betakeysystem.listener.PlayerLoginListener;
import de.illegalaccess.betakeysystem.utils.BetakeyManager;
import de.illegalaccess.betakeysystem.utils.MySQL;
import lombok.Getter;
import lombok.SneakyThrows;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
/*
    Hier könnte ihre Werbung stehen
*/
public final class BetakeySystem extends Plugin {
    private MySQL mySQL;
    private static BetakeyManager betakeyManager;

    @Getter
    private static BetakeySystem instance;

    @Getter
    private Configuration config;

    {
        try {
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "config.yml"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


 
    @SneakyThrows
    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;

        createFiles();
        mySQL = new MySQL();
        mySQL.connect();
        mySQL.createTabels();


        ProxyServer.getInstance().getPluginManager().registerCommand(this, new CreateKeyCommand());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new DeleteKeyCommand());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new ActivateCommand());
        ProxyServer.getInstance().getPluginManager().registerListener(this, new PlayerLoginListener());


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        mySQL.disconnect();
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
