package marioandweegee3.toolbuilder.common.tools;

import java.util.HashSet;
import java.util.Set;

import marioandweegee3.toolbuilder.ToolBuilder;
import marioandweegee3.toolbuilder.api.effect.Effect;
import marioandweegee3.toolbuilder.api.material.HeadMaterial;
import marioandweegee3.toolbuilder.common.effect.Effects;
import net.minecraft.item.Item;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.ToolMaterials;
import net.minecraft.recipe.Ingredient;
import net.minecraft.tag.ItemTags;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.Lazy;
import net.minecraft.util.registry.Registry;

public enum HeadMaterials implements HeadMaterial{
    WOOD(ToolMaterials.WOOD, "#planks", "#logs", "wood", Effects.GROWING),
    STONE(ToolMaterials.STONE, "cobblestone", "stone", "stone", Effects.RESILIENT),
    IRON(ToolMaterials.IRON, "iron_ingot", "iron_block", "iron", Effects.MAGNETIC),
    DIAMOND(ToolMaterials.DIAMOND, "diamond", "diamond_block", "diamond", Effects.GLIMMERING),
    GOLD(ToolMaterials.GOLD, "gold_ingot", "gold_block", "gold", Effects.ROYAL),
    STEEL(ToolMaterials.DIAMOND.getAttackDamage(), ToolMaterials.IRON.getMiningSpeed(),
            ToolMaterials.IRON.getDurability() + 50, ToolMaterials.IRON.getMiningLevel(),
            ToolMaterials.IRON.getEnchantability(), new Identifier("c:steel_ingot"), true, "#c:steel_block", "steel",
            Effects.MAGNETIC),
    COPPER(ToolMaterials.STONE.getAttackDamage(), ToolMaterials.IRON.getMiningSpeed(),
            ToolMaterials.STONE.getDurability() + 50, ToolMaterials.STONE.getMiningLevel(),
            ToolMaterials.IRON.getEnchantability(), new Identifier("c:copper_ingot"), true, "#c:copper_block", "copper",
            Effects.EXPERIENCE),
    SILVER(ToolMaterials.IRON.getAttackDamage() - 1, ToolMaterials.DIAMOND.getMiningSpeed() + 1,
            ToolMaterials.IRON.getDurability() - 25, ToolMaterials.IRON.getMiningLevel(),
            ToolMaterials.GOLD.getEnchantability() - 5, new Identifier("c:silver_ingot"), true, "#c:silver_block",
            "silver", Effects.HOLY),
    LEAD(ToolMaterials.IRON.getAttackDamage(), ToolMaterials.IRON.getMiningSpeed() + 1,
            ToolMaterials.IRON.getDurability() + 50, ToolMaterials.IRON.getMiningLevel(),
            ToolMaterials.IRON.getEnchantability(), new Identifier("c:lead_ingot"), true, "#c:lead_block", "lead",
            Effects.POISONOUS),
    OBSIDIAN(ToolMaterials.DIAMOND.getAttackDamage() + 1, ToolMaterials.DIAMOND.getMiningSpeed() + 2, 3500,
            ToolMaterials.DIAMOND.getMiningLevel(), ToolMaterials.STONE.getEnchantability(),
            new Identifier("toolbuilder:obsidian_plate"), false, "toolbuilder:dense_obsidian", "obsidian"),
    COBALT(ToolMaterials.DIAMOND.getAttackDamage(), ToolMaterials.DIAMOND.getMiningSpeed() + 6,
            ToolMaterials.IRON.getDurability() + 100, ToolMaterials.DIAMOND.getMiningLevel(),
            ToolMaterials.DIAMOND.getEnchantability() - 3, new Identifier("c:cobalt_ingot"), true, "#c:cobalt_block",
            "cobalt", Effects.LIGHT),
    ALUMINUM(ToolMaterials.IRON.getAttackDamage(), ToolMaterials.IRON.getMiningSpeed() + 1.25f,
            ToolMaterials.IRON.getDurability() - 10, ToolMaterials.IRON.getMiningLevel(),
            ToolMaterials.IRON.getEnchantability(), new Identifier("c:aluminum_ingot"), true, "#c:aluminum_block",
            "aluminum", Effects.LIGHT, Effects.EXTRA_MODS),
    NETHER_BRICK(ToolMaterials.STONE.getAttackDamage(), ToolMaterials.IRON.getMiningSpeed() - 1,
            ToolMaterials.IRON.getDurability() - 40, ToolMaterials.IRON.getMiningLevel(),
            ToolMaterials.STONE.getEnchantability(), new Identifier("nether_brick"), false, "nether_bricks",
            "nether_brick", Effects.FLAMING),
    TUNGSTEN(ToolMaterials.IRON.getAttackDamage(), ToolMaterials.STONE.getMiningSpeed() + 0.75f,
            ToolMaterials.IRON.getDurability() + 85, ToolMaterials.IRON.getMiningLevel(),
            ToolMaterials.IRON.getEnchantability(), new Identifier("c:tungsten_ingot"), true, "#c:tungsten_block",
            "tungsten", Effects.RESILIENT),
    PERIDOT(ToolMaterials.IRON.getAttackDamage(), ToolMaterials.DIAMOND.getMiningSpeed(),
            ToolMaterials.IRON.getDurability() + 30, ToolMaterials.IRON.getMiningLevel(),
            ToolMaterials.IRON.getEnchantability() - 2, new Identifier("c:peridot_gem"), true, "#c:peridot_block",
            "peridot", Effects.MAGICAL, Effects.GLIMMERING),
    SLIME(ToolMaterials.STONE.getAttackDamage(), ToolMaterials.STONE.getMiningSpeed(),
            ToolMaterials.IRON.getDurability() + 45, ToolMaterials.STONE.getMiningLevel(),
            ToolMaterials.GOLD.getEnchantability() - 7, new Identifier("toolbuilder:slime_crystal"), false,
            "toolbuilder:slime_crystal_block", "slime", Effects.BOUNCY),
    PRISMARINE(ToolMaterials.DIAMOND.getAttackDamage() + 1.5f, ToolMaterials.IRON.getMiningSpeed() + 0.5f,
            ToolMaterials.IRON.getDurability() + 65, ToolMaterials.STONE.getMiningLevel(),
            ToolMaterials.IRON.getEnchantability(), new Identifier("prismarine_shard"), false, 
            "prismarine_bricks", "prismarine", Effects.AQUATIC);

    private final String name;
    private float attackDamage, efficiency;
    private int durability, harvestLevel, enchantability;
    private Set<Effect> effects = new HashSet<>(0);
    private Lazy<Ingredient> repairIngredient;
    private String repairString;
    private String blockString;
    private boolean isCotton;

    private HeadMaterials(float attackDamage, float efficiency, int durability, int harvestLevel, int enchantability, Identifier item, boolean isTag, String block, String name, Effect... effects){
        this.attackDamage = attackDamage;
        this.durability = durability;
        this.enchantability = enchantability;
        this.harvestLevel = harvestLevel;
        this.efficiency = efficiency;
        for(Effect effect : effects){
            this.effects.add(effect);
        }
        this.name = name;
        this.repairIngredient = new Lazy<Ingredient>(()->{
            if(isTag){
                return Ingredient.ofItems(getItemsInTag(item));
            } else {
                return Ingredient.ofItems(Registry.ITEM.get(item));
            }
        });
        this.repairString = isTag?"#"+item.toString():item.toString();
        this.blockString = block;
        this.isCotton = item.getNamespace().equals("c") || block.startsWith("#c");
    }

    private HeadMaterials(ToolMaterial base, String repair, String block, String name, Effect... effects){
        HeadMaterial material = HeadMaterial.copy(base, name);
        this.attackDamage = material.getAttackDamage();
        this.durability = material.getDurability();
        this.efficiency = material.getMiningSpeed();
        this.enchantability = material.getEnchantability();
        this.harvestLevel = material.getMiningLevel();
        this.repairIngredient = new Lazy<>(() -> {return material.getRepairIngredient();});
        for(Effect effect : effects){
            this.effects.add(effect);
        }
        this.name = name;
        this.repairString = repair;
        this.blockString = block;
    }

    private Item[] getItemsInTag(Identifier tagId){
        Tag<Item> tag = ItemTags.getContainer().get(tagId);
        if(tag == null) return new Item[0];
        
        return tag.values().toArray(new Item[0]);
    }

    public boolean isPoisonous() {
        return effects.contains(Effects.POISONOUS);
    }

    @Override
    public float getMiningSpeed() {
        return efficiency;
    }

    @Override
    public float getAttackDamage() {
        return attackDamage;
    }

    @Override
    public int getDurability() {
        return durability;
    }

    @Override
    public int getMiningLevel() {
        return harvestLevel;
    }

    @Override
    public int getEnchantability() {
        return enchantability;
    }
    
    @Override
    public Ingredient getRepairIngredient() {
        return repairIngredient.get();
    }
    
    @Override
    public Effect[] getEffects() {
        return effects.toArray(new Effect[0]);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getRepairString() {
        return repairString;
    }

    @Override
    public String getBlockString() {
        return blockString;
    }

    @Override
    public boolean isCotton() {
        return isCotton;
    }

    @Override
    public String getMod() {
        return ToolBuilder.modID;
    }
    
}