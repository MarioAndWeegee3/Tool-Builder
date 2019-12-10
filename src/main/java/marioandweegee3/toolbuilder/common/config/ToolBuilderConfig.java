package marioandweegee3.toolbuilder.common.config;

public final class ToolBuilderConfig {

    public static final ToolBuilderConfig DEFAULTS = new ToolBuilderConfig();

    private ToolBuilderConfig(){

    }

    public final boolean canCraftWithSticks = true;
    public final double holyDamage = 2;
    public final int holyLuckTime = 600;
    public final int holyLuckLevel = 1;
    public final int poisonTime = 200;
    public final int extraXp = 4;
    public final double lightDamageMult = 0.85;
    public final int flamingTime = 5;
    public final double flammableTimeMult = 1.4;
    public final double durableDurabilityMult = 1.25;
    public final boolean bouncyDamage = false;
    public final boolean limitBounceHeight = true;
    public final double maxBounceSpeed = 4.5;
    public final double bounceSpeedMult = 1.2;
    public final double sneakBounceSpeedMult = 0.5;
    public final double magneticRange = 2.3;
    public final boolean addSteelRecipe = true;
    public final String[] shearLootTables = new String[]{
        "blocks/cobweb", 
        "blocks/dead_bush", 
        "blocks/fern", 
        "blocks/large_fern", 
        "blocks/grass", 
        "blocks/tall_grass",  
        "blocks/vines", 
        "blocks/seagrass", 
        "blocks/tall_seagrass",
        "blocks/oak_leaves",
        "blocks/birch_leaves",
        "blocks/spruce_leaves",
        "blocks/jungle_leaves",
        "blocks/dark_oak_leaves",
        "blocks/acacia_leaves"
        };
    public final boolean removeVanillaToolRecipes = false;
    public final boolean addNetherCobaltLootTable = true;
}