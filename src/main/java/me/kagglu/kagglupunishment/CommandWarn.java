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
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

public class CommandWarn implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage("§4§lInvalid syntax! Use: /warn (username) [reason]");
            return true;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            sb.append(args[i]).append(' ');
        }
        String reason = sb.toString();
        String username = args[0];
        String uuid = null;
        Player p = KaggluPunishment.getInstance().getServer().getPlayer(username);
        if (p == null) {
            JSONArray uuids = KaggluPunishment.getInstance().getUuidJson();
            for (int i = 0; i < uuids.size(); i++) {
                if (username.equalsIgnoreCase(((JSONObject) uuids.get(i)).get("username").toString())) {
                    uuid = ((JSONObject) uuids.get(i)).get("uuid").toString();
                }
            }
            if (uuid == null) {
                sender.sendMessage("§4§lPlayer not found!");
                return true;
            }
        } else {
            uuid = p.getUniqueId().toString();
            p.sendMessage("§4§lYou were just warned for: " + reason);
        }

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        LocalDateTime now = LocalDateTime.now();

        JSONArray json = KaggluPunishment.getInstance().getWarnsJson();
        JSONObject user = null;
        for (int i = 0; i < json.size(); i++) {
            if (uuid.equals(((JSONObject) json.get(i)).get("uuid"))) {
                user = (JSONObject) json.get(i);
                JSONArray warns = (JSONArray) user.get("warns");
                JSONObject warn = new JSONObject();
                warn.put("warner", sender.getName());
                warn.put("timestamp", dtf.format(now));
                warn.put("reason", reason);
                warns.add(warn);
                user.put("warns", warns);
                break;
            }
        }
        if (user == null) {
            user = new JSONObject();
            user.put("uuid", uuid);
            JSONArray warns = new JSONArray();
            JSONObject warn = new JSONObject();
            warn.put("warner", sender.getName());
            warn.put("timestamp", dtf.format(now));
            warn.put("reason", reason);
            warns.add(warn);
            user.put("warns", warns);
            json.add(user);
        }
        KaggluPunishment.getInstance().writeWarnsJSONString();
        //sender.sendMessage("§2§l" + username + " warned for: " + reason);
        Bukkit.broadcast("§c" + username + " has been warned for: " + reason, "kagglupunishment.warn");
        return true;
    }
}
