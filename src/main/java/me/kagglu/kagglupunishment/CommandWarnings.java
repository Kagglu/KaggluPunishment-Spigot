package me.kagglu.kagglupunishment;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;

public class CommandWarnings implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("kagglupunishment.warnings") || args.length == 0) {
            Player p = (Player) sender;
            JSONArray json = KaggluPunishment.getInstance().getWarnsJson();
            String uuid = p.getUniqueId().toString();
            for (int i = 0; i < json.size(); i++) {
                if (uuid.equals(((JSONObject) json.get(i)).get("uuid"))) {
                    JSONArray warns = (JSONArray) ((JSONObject) json.get(i)).get("warns");
                    if (warns.size() == 1) {
                        sender.sendMessage("§4§lYou have 1 warning:");
                    } else {
                        sender.sendMessage("§4§lYou have " + warns.size() + " warnings:");
                    }
                    for (int j = 0; j < warns.size(); j++) {
                        String dateString = (String)((JSONObject)(warns.get(j))).get("timestamp");
                        LocalDate date = LocalDate.of(Integer.parseInt(dateString.substring(0, 4)), Integer.parseInt(dateString.substring(5, 7)), Integer.parseInt(dateString.substring(8, 10)));
                        String season = "";
                        if (date.compareTo(LocalDate.of(2023, 6, 26)) < 0) {
                            season = "§l§3S4§r§c";
                        } else {
                            season = "§l§2S5§r§c";
                        }
                        sender.sendMessage("§c" + (j + 1) + ": " + ((JSONObject)(warns.get(j))).get("timestamp") + " (" + season + ") by " +  ((JSONObject)(warns.get(j))).get("warner") + ": " + ((JSONObject)(warns.get(j))).get("reason"));
                    }
                    return true;
                }
            }
            sender.sendMessage("§2§lYou have no warnings!");
        } else {
            Player p = KaggluPunishment.getInstance().getServer().getPlayer(args[0]);
            String uuid = null;
            if (p == null) {
                JSONArray uuids = KaggluPunishment.getInstance().getUuidJson();
                for (int i = 0; i < uuids.size(); i++) {
                    if (args[0].equalsIgnoreCase(((JSONObject) uuids.get(i)).get("username").toString())) {
                        uuid = ((JSONObject) uuids.get(i)).get("uuid").toString();
                    }
                }
                if (uuid == null) {
                    sender.sendMessage("§4§lPlayer not found!");
                    return true;
                }
            } else {
                uuid = p.getUniqueId().toString();
            }
            JSONArray json = KaggluPunishment.getInstance().getWarnsJson();
            for (int i = 0; i < json.size(); i++) {
                if (uuid.equals(((JSONObject) json.get(i)).get("uuid"))) {
                    JSONArray warns = (JSONArray) ((JSONObject) json.get(i)).get("warns");
                    if (warns.size() == 1) {
                        sender.sendMessage("§4§l" + args[0] + " has 1 warning:");
                    } else {
                        sender.sendMessage("§4§l" + args[0] + " has " + warns.size() + " warnings:");
                    }
                    for (int j = 0; j < warns.size(); j++) {
                        String dateString = (String)((JSONObject)(warns.get(j))).get("timestamp");
                        LocalDate date = LocalDate.of(Integer.parseInt(dateString.substring(0, 4)), Integer.parseInt(dateString.substring(5, 7)), Integer.parseInt(dateString.substring(8, 10)));
                        String season = "";
                        if (date.compareTo(LocalDate.of(2023, 6, 26)) < 0) {
                            season = "§l§3S4§r§c";
                        } else {
                            season = "§l§2S5§r§c";
                        }
                        sender.sendMessage("§c" + (j + 1) + ": " + ((JSONObject)(warns.get(j))).get("timestamp") + " (" + season + ") by " +  ((JSONObject)(warns.get(j))).get("warner") + ": " + ((JSONObject)(warns.get(j))).get("reason"));
                    }
                    return true;
                }
            }
            sender.sendMessage("§2§l" + args[0] + " has no warnings");
        }
        return true;
    }
}
