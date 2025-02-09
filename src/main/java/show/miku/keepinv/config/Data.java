package show.miku.keepinv.config;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class Data {
    private Set<UUID> keepInvPlayers = new HashSet<>();
    private final File dataFile;
    private final JavaPlugin plugin;

    public Data(final JavaPlugin plugin) {
        this.plugin = plugin;
        dataFile = new File(plugin.getDataFolder(), "data.yaml");
        loadData();
    }

    public void saveData() {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, this::saveDataSync);
    }

    public void saveDataSync() {
        try {
            YamlConfiguration config = new YamlConfiguration();
            config.set("keepInvPlayers", keepInvPlayers.stream().map(UUID::toString).toList());
            config.save(dataFile);
        } catch (Exception e) {
            plugin.getLogger().warning("保存数据失败: " + e.getMessage());
        }
    }

    public void loadData() {
        try {
            if (!dataFile.exists()) {
                dataFile.getParentFile().mkdirs();
                dataFile.createNewFile();
            }
            YamlConfiguration config = new YamlConfiguration();
            config.load(dataFile);
            if (config.contains("keepInvPlayers")) {
                setKeepInvPlayers(config.getStringList("keepInvPlayers")
                        .stream()
                        .map(UUID::fromString)
                        .collect(Collectors.toSet()));
            }
        } catch (Exception e) {
            plugin.getLogger().warning("加载数据失败: " + e.getMessage());
        }
    }


    public void setKeepInvPlayers(Set<UUID> keepInvPlayers) {
        this.keepInvPlayers = keepInvPlayers;
    }

    public Set<UUID> getKeepInvPlayers() {
        return keepInvPlayers;
    }

    public boolean addPlayer(UUID uuid) {
        return keepInvPlayers.add(uuid);
    }

    public boolean removePlayer(UUID uuid) {
        return keepInvPlayers.remove(uuid);
    }
}
