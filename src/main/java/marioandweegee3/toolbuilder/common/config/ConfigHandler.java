package marioandweegee3.toolbuilder.common.config;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import marioandweegee3.ml3api.config.Config;
import marioandweegee3.ml3api.config.ConfigBuilder;
import marioandweegee3.ml3api.config.ConfigManager;
import marioandweegee3.toolbuilder.ToolBuilder;
import net.minecraft.util.Identifier;

import static marioandweegee3.toolbuilder.common.config.ToolBuilderConfig.DEFAULTS;

public class ConfigHandler{
    public static final Identifier configID = ToolBuilder.makeID("config-v1.0");

    public static ConfigHandler INSTANCE;

    private Config config;

    private ConfigHandler(){
        config = new ConfigBuilder()
            .add("effects", new ConfigBuilder()
                .add("poisonTime", DEFAULTS.poisonTime)
                .add("holy", new ConfigBuilder()
                    .add("damage", DEFAULTS.holyDamage)
                    .add("luckTime", DEFAULTS.holyLuckTime)
                    .add("luckLevel", DEFAULTS.holyLuckLevel)
                    .build()
                )
                .add("extraXp", DEFAULTS.extraXp)
                .add("lightFallDamageMult", DEFAULTS.lightDamageMult)
                .add("flamingTime", DEFAULTS.flamingTime)
                .add("flammableTimeMult", DEFAULTS.flammableTimeMult)
                .add("durableMultiplier", DEFAULTS.durableDurabilityMult)
                .add("bouncy", new ConfigBuilder()
                    .add("damageArmor", DEFAULTS.bouncyDamage)
                    .add("limitHeight", DEFAULTS.limitBounceHeight)
                    .add("speedLimit", DEFAULTS.maxBounceSpeed)
                    .add("speedMultiplier", DEFAULTS.bounceSpeedMult)
                    .add("sneakingSpeedMult", DEFAULTS.sneakBounceSpeedMult)
                    .build()    
                )
                .add("magneticRange", DEFAULTS.magneticRange)
                .build()
            )
            .add("recipes", new ConfigBuilder()
                .add("craftWithSticks", DEFAULTS.canCraftWithSticks)
                .add("addSteelRecipe", DEFAULTS.addSteelRecipe)
                .add("removeVanillaTools", DEFAULTS.removeVanillaToolRecipes)
                .build()
            )
            .add("materials", new ConfigBuilder()
                .add("heads", DEFAULTS.enabledHeadMaterials)
                .add("handles", DEFAULTS.enabledHandleMaterials)
                .build()
            )
            .add("shearLootTables", DEFAULTS.shearLootTables)
            .add("addNetherCobaltTable", DEFAULTS.addNetherCobaltLootTable)
            .add("useNewModels", DEFAULTS.useNewModels)
        .build();

        ConfigManager.INSTANCE.set(configID, config);

        refresh();
    }

    private void refresh(){
        ConfigManager.INSTANCE.refresh(configID);
        config = ConfigManager.INSTANCE.getConfig(configID);
    }

    public static void init(){
        INSTANCE = new ConfigHandler();
    }

    public Config getConfig(){
        refresh();
        return config;
    }

    public void setConfig(Config config){
        this.config = config;
    }

    public Integer getPoisonTime(){
        refresh();
        return config.getSubConfig("effects").getInt("poisonTime");
    }

    public Double getHolyDamage(){
        refresh();
        return config.getSubConfig("effects").getSubConfig("holy").get("damage", Double.class);
    }

    public Integer getHolyLuckTime(){
        refresh();
        return config.getSubConfig("effects").getSubConfig("holy").getInt("luckTime");
    }

    public Integer getHolyLuckLevel(){
        refresh();
        return config.getSubConfig("effects").getSubConfig("holy").getInt("luckLevel");
    }

    public Integer getExtraXp(){
        refresh();
        return config.getSubConfig("effects").getInt("extraXp");
    }

    public Double getLightFallDamageMultiplier(){
        refresh();
        return config.getSubConfig("effects").get("lightFallDamageMult", Double.class);
    }

    public Integer getFlamingTime(){
        refresh();
        return config.getSubConfig("effects").getInt("flamingTime");
    }

    public Double getFlammableTimeMultiplier(){
        refresh();
        return config.getSubConfig("effects").get("flammableTimeMult", Double.class);
    }

    public Double getDurableMultiplier(){
        refresh();
        return config.getSubConfig("effects").get("durableMultiplier", Double.class);
    }

    public Boolean bouncyDamagesArmor(){
        refresh();
        return config.getSubConfig("effects").getSubConfig("bouncy").get("damageArmor", Boolean.class);
    }

    public Boolean limitBounceHeight(){
        refresh();
        return config.getSubConfig("effects").getSubConfig("bouncy").get("limitHeight", Boolean.class);
    }

    public Double getMaxBounceVelocity(){
        refresh();
        return config.getSubConfig("effects").getSubConfig("bouncy").get("speedLimit", Double.class);
    }

    public Double getBounceSpeedMultiplier(){
        refresh();
        return config.getSubConfig("effects").getSubConfig("bouncy").get("speedMultiplier", Double.class);
    }

    public Double getSneakBounceSpeedMultiplier(){
        refresh();
        return config.getSubConfig("effects").getSubConfig("bouncy").get("sneakingSpeedMult", Double.class);
    }

    public Double getMagneticRange(){
        refresh();
        return config.getSubConfig("effects").get("magneticRange", Double.class);
    }

    public Boolean canCraftWithSticks(){
        refresh();
        return config.getSubConfig("recipes").get("craftWithSticks", Boolean.class);
    }

    public Boolean shouldAddSteelRecipe(){
        refresh();
        return config.getSubConfig("recipes").get("addSteelRecipe", Boolean.class);
    }

    public List<Identifier> shearLootTables(){
        refresh();
        Set<Identifier> ids = new HashSet<>();
        for(Object o : config.get("shearLootTables", ArrayList.class)){
            if(o instanceof String){
                ids.add(new Identifier((String)o));
            }
        }
        return new ArrayList<>(ids);
    }

    public Boolean shouldRemoveVanillaToolRecipes(){
        refresh();
        return config.getSubConfig("recipes").get("removeVanillaTools", Boolean.class);
    }

    public Boolean shouldAddNetherCobaltLootTable(){
        refresh();
        return config.get("addNetherCobaltTable", Boolean.class);
    }

    public Boolean shouldUseNewModels(){
        refresh();
        return config.get("useNewModels", Boolean.class);
    }

    public List<String> enabledHeadMaterials(){
        refresh();
        List<String> materials = new ArrayList<>();
        for(Object o : config.getSubConfig("materials").get("heads", ArrayList.class)){
            if(o instanceof String) {
                materials.add((String) o);
            }
        }
        return materials;
    }

    public List<String> enabledHandleMaterials(){
        refresh();
        List<String> materials = new ArrayList<>();
        for(Object o : config.getSubConfig("materials").get("handles", ArrayList.class)){
            if(o instanceof String) {
                materials.add((String) o);
            }
        }
        return materials;
    }
}