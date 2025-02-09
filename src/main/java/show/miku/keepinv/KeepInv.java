package show.miku.keepinv;

import org.bukkit.plugin.java.JavaPlugin;
import show.miku.keepinv.command.keepInvCommand;
import show.miku.keepinv.config.Data;
import show.miku.keepinv.listener.DeathListener;

public final class KeepInv extends JavaPlugin {
    private Data data;

    @Override
    public void onEnable() {
        this.data = new Data(this);
        getServer().getPluginManager().registerEvents(new DeathListener(data), this);
        getCommand("keepinv").setExecutor(new keepInvCommand(this));
    }

    public Data getData() {
        return data;
    }

    @Override
    public void onDisable() {
        data.saveDataSync();
    }
}
