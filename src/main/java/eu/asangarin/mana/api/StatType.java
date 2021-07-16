package eu.asangarin.mana.api;

import eu.asangarin.mana.MMOMana;
import eu.asangarin.mana.stat.ManaRegeneration;
import eu.asangarin.mana.stat.MaxStamina;
import eu.asangarin.mana.stat.StaminaRegeneration;
import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.stat.type.ItemStat;

public enum StatType {

    /**
     * Has no stat class because MMOItems has a max mana stat by default.
     */
    MAX_MANA(null),

    MAX_STAMINA(new MaxStamina()),
    MANA_REGENERATION(new ManaRegeneration()),
    STAMINA_REGENERATION(new StaminaRegeneration());

    private final ItemStat stat;
    private final double base;

    private StatType(ItemStat stat) {
        this.stat = stat;
        this.base = MMOMana.plugin.getConfig().getDouble("default." + this.name().toLowerCase().replace("_", "-"));
    }

    public double getBase() {
        return base;
    }

    public void registerStat() {
        if (stat != null)
            MMOItems.plugin.getStats().register(stat);
    }
}