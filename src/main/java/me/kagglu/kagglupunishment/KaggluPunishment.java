package me.kagglu.kagglupunishment;

import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;

public class KaggluPunishment extends JavaPlugin {
    private static KaggluPunishment instance;
    private PluginManager pluginManager;
    private FileConfiguration config = this.getConfig();
    private JSONArray warnsJson;
    private JSONArray uuidJson;
    private File warnsJsonFile;
    private File uuidJsonFile;

    @Override
    public void onEnable() {
        config.addDefault("fix", 0);
        config.options().copyDefaults(true);
        saveConfig();

        instance = this;
        pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new Listener(), this);
        warnsJsonFile = new File(this.getDataFolder().toString() + "/warns.json");
        uuidJsonFile = new File(this.getDataFolder().toString() + "/uuids.json");
        initJSON();

        this.getCommand("warn").setExecutor(new CommandWarn());
        this.getCommand("warnings").setExecutor(new CommandWarnings());
        this.getCommand("delwarn").setExecutor(new CommandDelwarn());
    }

    @Override
    public void onDisable() {
        writeUuidJsonString();
        writeWarnsJSONString();
    }

    public static KaggluPunishment getInstance() {
        return instance;
    }

    private void initJSON() {
        try {
            if (warnsJsonFile.exists()) {
                FileReader reader = new FileReader(warnsJsonFile);
                JSONParser jsonParser = new JSONParser();
                warnsJson = (JSONArray) jsonParser.parse(reader);
            } else {
                warnsJsonFile.createNewFile();
                warnsJson = new JSONArray();
                writeWarnsJSONString();
            }
            if (uuidJsonFile.exists()) {
                FileReader reader = new FileReader(uuidJsonFile);
                JSONParser jsonParser = new JSONParser();
                uuidJson = (JSONArray) jsonParser.parse(reader);
            } else {
                uuidJsonFile.createNewFile();
                uuidJson = new JSONArray();
                writeUuidJsonString();
            }
        } catch (Exception e) {
            Bukkit.getLogger().severe("KaggluPunishment error in JSON init: " + e);
        }
    }

    public void writeWarnsJSONString() {
        try {
            Writer writer = new FileWriter(warnsJsonFile);
            writer.write("[");
            for (int i = 0; i < warnsJson.size(); i++) {
                writer.write(((JSONObject) warnsJson.get(i)).toJSONString());
                if (i != warnsJson.size() - 1) {
                    writer.write(",");
                }
            }
            writer.write("]");
            writer.close();
        } catch (Exception e) {
            Bukkit.getLogger().severe("KaggluPunishment error in JSON write: " + e);
        }
    }

    public void writeUuidJsonString() {
        try {
            Writer writer = new FileWriter(uuidJsonFile);
            writer.write("[");
            for (int i = 0; i < uuidJson.size(); i++) {
                writer.write(((JSONObject) uuidJson.get(i)).toJSONString());
                if (i != uuidJson.size() - 1) {
                    writer.write(",");
                }
            }
            writer.write("]");
            writer.close();
        } catch (Exception e) {
            Bukkit.getLogger().severe("KaggluPunishment error in JSON write: " + e);
        }
    }

    public JSONArray getWarnsJson() {
        return warnsJson;
    }

    public JSONArray getUuidJson() {return uuidJson;}
}
