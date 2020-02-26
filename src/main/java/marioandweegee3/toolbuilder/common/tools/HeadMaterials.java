package marioandweegee3.toolbuilder.common.tools;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import marioandweegee3.toolbuilder.ToolBuilder;
import marioandweegee3.toolbuilder.api.effect.Effect;
import marioandweegee3.toolbuilder.api.effect.EffectInstance;
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
    WOOD(ToolMaterials.WOOD, "#planks", "#logs", "wood", "Wooden", Effects.GROWING),
    STONE(ToolMaterials.STONE, "cobblestone", "stone", "stone", null, Effects.RESILIENT),
    IRON(ToolMaterials.IRON, "iron_ingot", "iron_block", "iron", null, Effects.MAGNETIC),
    DIAMOND(ToolMaterials.DIAMOND, "diamond", "diamond_block", "diamond", null, Effects.GLIMMERING),
    GOLD(ToolMaterials.GOLD, "gold_ingot", "gold_block", "gold", "Golden", Effects.ROYAL),
    STEEL(ToolMaterials.DIAMOND.getAttackDamage(), ToolMaterials.IRON.getMiningSpeed(),
            ToolMaterials.IRON.getDurability() + 50, ToolMaterials.IRON.getMiningLevel(),
            ToolMaterials.IRON.getEnchantability(), new Identifier("c:steel_ingot"), true, "#c:steel_block", "steel",
            null, Effects.MAGNETIC),
    COPPER(ToolMaterials.STONE.getAttackDamage(), ToolMaterials.IRON.getMiningSpeed(),
            ToolMaterials.STONE.getDurability() + 50, ToolMaterials.STONE.getMiningLevel(),
            ToolMaterials.IRON.getEnchantability(), new Identifier("c:copper_ingot"), true, "#c:copper_block", "copper",
            null, Effects.EXPERIENCE),
    SILVER(ToolMaterials.IRON.getAttackDamage() - 1, ToolMaterials.DIAMOND.getMiningSpeed() + 1,
            ToolMaterials.IRON.getDurability() - 25, ToolMaterials.IRON.getMiningLevel(),
            ToolMaterials.GOLD.getEnchantability() - 5, new Identifier("c:silver_ingot"), true, "#c:silver_block",
            "silver", null, Effects.HOLY),
    LEAD(ToolMaterials.IRON.getAttackDamage(), ToolMaterials.IRON.getMiningSpeed() + 1,
            ToolMaterials.IRON.getDurability() + 50, ToolMaterials.IRON.getMiningLevel(),
            ToolMaterials.IRON.getEnchantability(), new Identifier("c:lead_ingot"), true, "#c:lead_block", "lead",
            null, Effects.POISONOUS),
    OBSIDIAN(ToolMaterials.DIAMOND.getAttackDamage() + 1, ToolMaterials.DIAMOND.getMiningSpeed() + 2, 3500,
            ToolMaterials.DIAMOND.getMiningLevel(), ToolMaterials.STONE.getEnchantability(),
            new Identifier("toolbuilder:obsidian_plate"), false, "toolbuilder:dense_obsidian", "obsidian", null),
    COBALT(ToolMaterials.DIAMOND.getAttackDamage(), ToolMaterials.DIAMOND.getMiningSpeed() + 6,
            ToolMaterials.IRON.getDurability() + 100, ToolMaterials.DIAMOND.getMiningLevel(),
            ToolMaterials.DIAMOND.getEnchantability() - 3, new Identifier("c:cobalt_ingot"), true, "#c:cobalt_block",
            "cobalt", null, Effects.LIGHT),
    ALUMINUM(ToolMaterials.IRON.getAttackDamage(), ToolMaterials.IRON.getMiningSpeed() + 1.25f,
            ToolMaterials.IRON.getDurability() - 10, ToolMaterials.IRON.getMiningLevel(),
            ToolMaterials.IRON.getEnchantability(), new Identifier("c:aluminum_ingot"), true, "#c:aluminum_block",
            "aluminum", null, Effects.LIGHT, Effects.EXTRA_MODS),
    NETHER_BRICK(ToolMaterials.STONE.getAttackDamage(), ToolMaterials.IRON.getMiningSpeed() - 1,
            ToolMaterials.IRON.getDurability() - 40, ToolMaterials.IRON.getMiningLevel(),
            ToolMaterials.STONE.getEnchantability(), new Identifier("nether_brick"), false, "nether_bricks",
            "nether_brick", "Nether Brick", Effects.FLAMING),
    TUNGSTEN(ToolMaterials.IRON.getAttackDamage(), ToolMaterials.STONE.getMiningSpeed() + 0.75f,
            ToolMaterials.IRON.getDurability() + 85, ToolMaterials.IRON.getMiningLevel(),
            ToolMaterials.IRON.getEnchantability(), new Identifier("c:tungsten_ingot"), true, "#c:tungsten_block",
            "tungsten", null, Effects.RESILIENT),
    PERIDOT(ToolMaterials.IRON.getAttackDamage(), ToolMaterials.DIAMOND.getMiningSpeed(),
            ToolMaterials.IRON.getDurability() + 30, ToolMaterials.IRON.getMiningLevel(),
            ToolMaterials.IRON.getEnchantability() - 2, new Identifier("c:peridot_gem"), true, "#c:peridot_block",
            "peridot", null, Effects.MAGICAL, Effects.GLIMMERING),
    SLIME(ToolMaterials.STONE.getAttackDamage(), ToolMaterials.STONE.getMiningSpeed(),
            ToolMaterials.IRON.getDurability() + 45, ToolMaterials.STONE.getMiningLevel(),
            ToolMaterials.GOLD.getEnchantability() - 7, new Identifier("toolbuilder:slime_crystal"), false,
            "toolbuilder:slime_crystal_block", "slime", null, Effects.BOUNCY),
    PRISMARINE(ToolMaterials.DIAMOND.getAttackDamage() + 1.5f, ToolMaterials.IRON.getMiningSpeed() + 0.5f,
            ToolMaterials.IRON.getDurability() + 65, ToolMaterials.STONE.getMiningLevel(),
            ToolMaterials.IRON.getEnchantability(), new Identifier("prismarine_shard"), false, 
            "prismarine_bricks", "prismarine", null, Effects.AQUATIC);

    private final String name;
    private float attackDamage, efficiency;
    private int durability, harvestLevel, enchantability;
    private Set<Effect> effects = new HashSet<>(0);
    private Lazy<Ingredient> repairIngredient;
    private String repairString;
    private String blockString;
    private boolean isCotton;
    private String usTranslation;

    HeadMaterials(float attackDamage, float efficiency, int durability, int harvestLevel, int enchantability, Identifier item, boolean isTag, String block, String name, String usTranslation, Effect... effects){
        this.attackDamage = attackDamage;
        this.durability = durability;
        this.enchantability = enchantability;
        this.harvestLevel = harvestLevel;
        this.efficiency = efficiency;
        Collections.addAll(this.effects, effects);
        this.name = name;
        this.repairIngredient = new Lazy<>(() -> {
            if (isTag) {
                return Ingredient.ofItems(getItemsInTag(item));
            } else {
                return Ingredient.ofItems(Registry.ITEM.get(item));
            }
        });
        this.repairString = isTag?"#"+item.toString():item.toString();
        this.blockString = block;
        this.isCotton = item.getNamespace().equals("c") || block.startsWith("#c");

        if(usTranslation == null){
            this.usTranslation = StringUtils.capitalize(name);
        } else {
            this.usTranslation = usTranslation;
        }
    }

    HeadMaterials(ToolMaterial base, String repair, String block, String name, String usTranslation, Effect... effects){
        HeadMaterial material = HeadMaterial.copy(base, name);
        this.attackDamage = material.getAttackDamage();
        this.durability = material.getDurability();
        this.efficiency = material.getMiningSpeed();
        this.enchantability = material.getEnchantability();
        this.harvestLevel = material.getMiningLevel();
        this.repairIngredient = new Lazy<>(material::getRepairIngredient);
        this.effects.addAll(Arrays.asList(effects));
        this.name = name;
        this.repairString = repair;
        this.blockString = block;

        if(usTranslation == null){
            this.usTranslation = StringUtils.capitalize(name);
        } else {
            this.usTranslation = usTranslation;
        }
    }

    private Item[] getItemsInTag(Identifier tagId){
        Tag<Item> tag = ItemTags.getContainer().get(tagId);
        if(tag == null) return new Item[0];
        
        return tag.values().toArray(new Item[0]);
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
    public Set<EffectInstance> getEffects() {
        return EffectInstance.fromEffects(effects);
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
    public String getMod() {
        return ToolBuilder.modID;
    }
    
    @Override
    public String getUSTranslation() {
        return usTranslation;
    }
}