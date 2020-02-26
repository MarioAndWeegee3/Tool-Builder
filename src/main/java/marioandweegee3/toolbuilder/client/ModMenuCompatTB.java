package marioandweegee3.toolbuilder.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Function;

import io.github.prospector.modmenu.api.ModMenuApi;
import marioandweegee3.ml3api.config.Config;
import marioandweegee3.ml3api.config.ConfigManager;
import marioandweegee3.toolbuilder.ToolBuilder;
import marioandweegee3.toolbuilder.common.config.ConfigHandler;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;

import static marioandweegee3.toolbuilder.common.config.ToolBuilderConfig.DEFAULTS;

public class ModMenuCompatTB implements ModMenuApi{

    @Override
    public String getModId() {
        return ToolBuilder.modID;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Function<Screen, ? extends Screen> getConfigScreenFactory() {
        return screen -> {
            Config config = ConfigHandler.INSTANCE.getConfig();
            ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(screen)
                .setTitle("Tool Builder")
                .setSavingRunnable(()->{
                    ConfigManager.INSTANCE.set(ConfigHandler.configID, config);
                    ConfigManager.INSTANCE.write(ConfigHandler.configID);
            });
            ConfigEntryBuilder entryBuilder = ConfigEntryBuilder.create();

            ConfigCategory general = builder.getOrCreateCategory("General");
            general.addEntry(entryBuilder.startStrList("Shear Loot Tables", config.get("shearLootTables", ArrayList.class)).setDefaultValue(Arrays.asList(DEFAULTS.shearLootTables)).setSaveConsumer(val->config.set("shearLootTables", val)).build());
            general.addEntry(entryBuilder.startBooleanToggle("Add Nether Cobalt Loot Table", config.get("addNetherCobaltTable", Boolean.class)).setDefaultValue(DEFAULTS.addNetherCobaltLootTable).setSaveConsumer(val->config.set("addNetherCobaltTable", val)).build());
            general.addEntry(entryBuilder.startBooleanToggle("Use New Tool Models", config.get("useNewModels", Boolean.class)).setDefaultValue(DEFAULTS.useNewModels).setSaveConsumer(val -> config.set("useNewModels", val)).build());

            ConfigCategory effects = builder.getOrCreateCategory("Effects");
            effects.addEntry(entryBuilder.startIntField("Poisonous Duration", config.getSubConfig("effects").getInt("poisonTime")).setDefaultValue(DEFAULTS.poisonTime).setSaveConsumer(val->config.setSubConfigVal("effects", "poisonTime", val)).build());
            effects.addEntry(entryBuilder.startDoubleField("Holy Damage", config.getSubConfig("effects").getSubConfig("holy").get("damage", Double.class)).setDefaultValue(DEFAULTS.holyDamage).setSaveConsumer(val->config.setSubSubConfigVal("effects", "holy", "damage", val)).build());
            effects.addEntry(entryBuilder.startIntField("Holy Luck Time", config.getSubConfig("effects").getSubConfig("holy").getInt("luckTime")).setDefaultValue(DEFAULTS.holyLuckTime).setSaveConsumer(val->config.setSubSubConfigVal("effects", "holy", "luckTime", val)).build());
            effects.addEntry(entryBuilder.startIntField("Holy Luck Level", config.getSubConfig("effects").getSubConfig("holy").getInt("luckLevel")).setDefaultValue(DEFAULTS.holyLuckLevel).setSaveConsumer(val->config.setSubSubConfigVal("effects", "holy", "luckLevel", val)).build());
            effects.addEntry(entryBuilder.startIntField("Extra Experience", config.getSubConfig("effects").getInt("extraXp")).setDefaultValue(DEFAULTS.extraXp).setSaveConsumer(val->config.setSubConfigVal("effects", "extraXp", val)).build());
            effects.addEntry(entryBuilder.startDoubleField("Light Fall Damage Multiplier", config.getSubConfig("effects").get("lightFallDamageMult", Double.class)).setDefaultValue(DEFAULTS.lightDamageMult).setSaveConsumer(val->config.setSubConfigVal("effects", "lightFallDamageMult", val)).build());
            effects.addEntry(entryBuilder.startIntField("Flaming Time", config.getSubConfig("effects").getInt("flamingTime")).setDefaultValue(DEFAULTS.flamingTime).setSaveConsumer(val->config.setSubConfigVal("effects", "flamingTime", val)).build());
            effects.addEntry(entryBuilder.startDoubleField("Flammable Time Multiplier", config.getSubConfig("effects").get("flammableTimeMult", Double.class)).setDefaultValue(DEFAULTS.flammableTimeMult).setSaveConsumer(val->config.setSubConfigVal("effects", "flammableTimeMult", val)).build());
            effects.addEntry(entryBuilder.startDoubleField("Durable Multiplier", config.getSubConfig("effects").get("durableMultiplier", Double.class)).setDefaultValue(DEFAULTS.durableDurabilityMult).setSaveConsumer(val->config.setSubConfigVal("effects", "durableMultiplier", val)).build());
            effects.addEntry(entryBuilder.startBooleanToggle("Bouncy Damages Armor", config.getSubConfig("effects").getSubConfig("bouncy").get("damageArmor", Boolean.class)).setDefaultValue(DEFAULTS.bouncyDamage).setSaveConsumer(val->config.setSubSubConfigVal("effects", "bouncy", "damageArmor", val)).build());
            effects.addEntry(entryBuilder.startBooleanToggle("Bouncy has Speed Limit", config.getSubConfig("effects").getSubConfig("bouncy").get("limitHeight", Boolean.class)).setDefaultValue(DEFAULTS.limitBounceHeight).setSaveConsumer(val->config.setSubSubConfigVal("effects", "bouncy", "limitHeight", val)).build());
            effects.addEntry(entryBuilder.startDoubleField("Bouncy Speed Limit", config.getSubConfig("effects").getSubConfig("bouncy").get("speedLimit", Double.class)).setDefaultValue(DEFAULTS.maxBounceSpeed).setSaveConsumer(val->config.setSubSubConfigVal("effects", "bouncy", "speedLimit", val)).build());
            effects.addEntry(entryBuilder.startDoubleField("Bouncy Speed Multiplier", config.getSubConfig("effects").getSubConfig("bouncy").get("speedMultiplier", Double.class)).setDefaultValue(DEFAULTS.bounceSpeedMult).setSaveConsumer(val->config.setSubSubConfigVal("effects", "bouncy", "speedMultiplier", val)).build());
            effects.addEntry(entryBuilder.startDoubleField("Bouncy Sneak Speed Multiplier", config.getSubConfig("effects").getSubConfig("bouncy").get("sneakingSpeedMult", Double.class)).setDefaultValue(DEFAULTS.sneakBounceSpeedMult).setSaveConsumer(val->config.setSubSubConfigVal("effects", "bouncy", "sneakingSpeedMult", val)).build());
            effects.addEntry(entryBuilder.startDoubleField("Magnetic Attraction Range", config.getSubConfig("effects").get("magneticRange", Double.class)).setDefaultValue(DEFAULTS.magneticRange).setSaveConsumer(val->config.setSubConfigVal("effects", "magneticRange", val)).build());

            ConfigCategory recipes = builder.getOrCreateCategory("Recipes");
            recipes.addEntry(entryBuilder.startBooleanToggle("Can craft with Sticks", config.getSubConfig("recipes").get("craftWithSticks", Boolean.class)).setDefaultValue(DEFAULTS.canCraftWithSticks).setSaveConsumer(val->config.setSubConfigVal("recipes", "craftWithSticks", val)).build());
            recipes.addEntry(entryBuilder.startBooleanToggle("Add Steel Blasting Recipe", config.getSubConfig("recipes").get("addSteelRecipe", Boolean.class)).setDefaultValue(DEFAULTS.addSteelRecipe).setSaveConsumer(val->config.setSubConfigVal("recipes", "addSteelRecipe", val)).build());
            recipes.addEntry(entryBuilder.startBooleanToggle("Remove Vanilla Tool Recipes", config.getSubConfig("recipes").get("removeVanillaTools", Boolean.class)).setDefaultValue(DEFAULTS.removeVanillaToolRecipes).setSaveConsumer(val->config.setSubConfigVal("recipes", "removeVanillaTools", val)).build());

            ConfigCategory materials = builder.getOrCreateCategory("Materials");
            materials.addEntry(entryBuilder.startStrList("Enabled Head Materials", config.getSubConfig("materials").get("heads", ArrayList.class)).setDefaultValue(Arrays.asList(DEFAULTS.enabledHeadMaterials)).setSaveConsumer(val->config.setSubConfigVal("materials", "heads", val)).build());
            materials.addEntry(entryBuilder.startStrList("Enabled Handle Materials", config.getSubConfig("materials").get("handles", ArrayList.class)).setDefaultValue(Arrays.asList(DEFAULTS.enabledHeadMaterials)).setSaveConsumer(val->config.setSubConfigVal("materials", "handles", val)).build());

            return(builder.build());
        };
    }

}