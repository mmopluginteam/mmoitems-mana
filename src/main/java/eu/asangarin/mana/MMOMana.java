package eu.asangarin.mana;

import eu.asangarin.mana.api.ResourceData;
import eu.asangarin.mana.api.ResourceRegainReason;
import eu.asangarin.mana.api.StatType;
import eu.asangarin.mana.comp.DefaultHook;
import eu.asangarin.mana.comp.PAPIPlaceholders;
import eu.asangarin.mana.manager.PlayerDataManager;
import net.Indyuce.mmoitems.MMOItems;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class MMOMana extends JavaPlugin {
	public static MMOMana plugin;

	public final PlayerDataManager dataManager = new PlayerDataManager();
	public final ManaConfig config = new ManaConfig();

	/**
	 * Data from players is cleared if they
	 * have not logged in for the last 60 minutes
	 */
	private static final long RESOURCE_DATA_TIMEOUT = 1000 * 60 * 60;

	public void onLoad() {
		plugin = this;
		this.saveDefaultConfig();
		config.loadOptions(getConfig());
		for (StatType type : StatType.values())
			type.registerStat();
	}

	public void onEnable() {
		final int configVersion = getConfig().contains("config-version", true) ? getConfig().getInt("config-version") : -1;
		final int defConfigVersion = getConfig().getDefaults().getInt("config-version");
		if (configVersion != defConfigVersion) {
			getLogger().warning("You may be using an outdated config.yml!");
			getLogger().warning("(Your config version: '" + configVersion + "' | Expected config version: '" + defConfigVersion + "')");
		}

		if (Bukkit.getPluginManager().isPluginEnabled("MMOCore")) {
			getLogger().severe("Do not use this addon alongside MMOCore!");
			Bukkit.getPluginManager().disablePlugin(this);
			return;
		}

		MMOItems.plugin.setRPG(new DefaultHook());

		Bukkit.getOnlinePlayers().forEach(this.dataManager::setup);

		if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
			new PAPIPlaceholders().register();
			getLogger().log(Level.INFO, "Hooked onto PlaceholderAPI");
		}

		// Register events
		Bukkit.getPluginManager().registerEvents(new EventListener(), this);

		// Register loop for natural mana and stamina regeneration
		Bukkit.getScheduler().runTaskTimer(this, () -> {
			for (ResourceData data : dataManager.getLoaded())
				if (data.toMythicLib().isOnline()) {
					data.giveMana(data.getStat(StatType.MANA_REGENERATION) * config.refreshRate / 20, ResourceRegainReason.REGENERATION);
					data.giveStamina(data.getStat(StatType.STAMINA_REGENERATION) * config.refreshRate / 20, ResourceRegainReason.REGENERATION);
				}
		}, 100L, config.refreshRate);

		/*
		 * Clear resource data for players who did not log in for the
		 * past 1 hour which should be enough to regen everything.
		 */
		Bukkit.getScheduler().runTaskTimerAsynchronously(this,
				() -> dataManager.getLoaded().removeIf(data -> !data.toMythicLib().isOnline() && System.currentTimeMillis() - data.toMythicLib().getLastLogActivity() > RESOURCE_DATA_TIMEOUT),
				20 * 10, 20 * 60 * 60);
	}
}
