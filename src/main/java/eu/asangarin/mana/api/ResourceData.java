package eu.asangarin.mana.api;

import eu.asangarin.mana.MMOMana;
import eu.asangarin.mana.api.event.ResourceRegainEvent;
import io.lumine.mythic.lib.player.MMOPlayerData;
import io.lumine.mythic.lib.api.stat.modifier.StatModifier;
import org.bukkit.Bukkit;

import java.util.UUID;

public class ResourceData {
    private final MMOPlayerData data;

    private double mana;
    private double stamina;

    public ResourceData(MMOPlayerData data) {
        this.data = data;

        data.getStatMap().getInstance("MAX_MANA").addModifier("manaAndStamina", new StatModifier(StatType.MAX_MANA.getBase()));
        data.getStatMap().getInstance("MAX_STAMINA").addModifier("manaAndStamina", new StatModifier(StatType.MAX_STAMINA.getBase()));
        data.getStatMap().getInstance("MANA_REGENERATION").addModifier("manaAndStamina", new StatModifier(StatType.MANA_REGENERATION.getBase()));
        data.getStatMap().getInstance("STAMINA_REGENERATION").addModifier("manaAndStamina", new StatModifier(StatType.STAMINA_REGENERATION.getBase()));

        // Default resource ratios
        this.mana = MMOMana.plugin.config.loginManaRatio / 100.0D * this.getStat(StatType.MAX_MANA);
        this.stamina = MMOMana.plugin.config.loginStaminaRatio / 100.0D * this.getStat(StatType.MAX_STAMINA);
    }

    /**
     * @deprecated Use {@link #toMythicLib()} instead
     */
    @Deprecated
    public MMOPlayerData toMMOLib() {
        return toMythicLib();
    }

    public MMOPlayerData toMythicLib() {
        return data;
    }

    /**
     * @deprecated Only works if the player is online.
     *         Will throw a NPE if he is offline
     */
    @Deprecated
    public UUID getUniqueId() {
        return data.getPlayer().getUniqueId();
    }

    public double getMana() {
        return mana;
    }

    public double getStamina() {
        return stamina;
    }

    public double getStat(StatType type) {
        return data.getStatMap().getStat(type.name());
    }

    public void giveMana(double value) {
        giveMana(value, ResourceRegainReason.PLUGIN);
    }

    public void giveMana(double value, ResourceRegainReason reason) {
        ResourceRegainEvent called = new ResourceRegainEvent(this, value, reason, ResourceType.MANA);
        Bukkit.getPluginManager().callEvent(called);
        if (!called.isCancelled())
            setMana(mana + called.getAmount());
    }

    public void giveStamina(double value) {
        giveStamina(value, ResourceRegainReason.OTHER);
    }

    public void giveStamina(double value, ResourceRegainReason reason) {
        ResourceRegainEvent called = new ResourceRegainEvent(this, value, reason, ResourceType.STAMINA);
        Bukkit.getPluginManager().callEvent(called);
        if (!called.isCancelled())
            setStamina(stamina + called.getAmount());
    }

    public void setMana(double value) {
        mana = Math.max(0, Math.min(getStat(StatType.MAX_MANA), value));
    }

    public void setStamina(double value) {
        stamina = Math.max(0, Math.min(getStat(StatType.MAX_STAMINA), value));
    }

    public String getManaBar() {
        StringBuilder format = new StringBuilder();
        double ratio = MMOMana.plugin.config.manaBarLength * getMana() / getStat(StatType.MAX_MANA);

        for (double j = 1; j < MMOMana.plugin.config.manaBarLength; j++)
            format.append(ratio >= j ? MMOMana.plugin.config.manaBarWholeColor : (ratio >= j - .5
                    ? MMOMana.plugin.config.manaBarHalfColor : MMOMana.plugin.config.manaBarEmptyColor))
                    .append(MMOMana.plugin.config.manaBarChar);

        return format.toString();
    }

    public String getStaminaBar() {
        StringBuilder format = new StringBuilder();
        double ratio = MMOMana.plugin.config.staminaBarLength * getStamina() / getStat(StatType.MAX_STAMINA);

        for (double j = 1; j < MMOMana.plugin.config.staminaBarLength; j++)
            format.append(ratio >= j ? MMOMana.plugin.config.staminaBarWholeColor : (ratio >= j - .5
                    ? MMOMana.plugin.config.staminaBarHalfColor : MMOMana.plugin.config.staminaBarEmptyColor))
                    .append(MMOMana.plugin.config.staminaBarChar);

        return format.toString();
    }
}
