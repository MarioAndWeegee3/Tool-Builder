package marioandweegee3.toolbuilder.common.tools;

import io.github.cottonmc.cotton.registry.CommonItems;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.ToolMaterials;
import net.minecraft.recipe.Ingredient;

public enum HeadMaterials implements ToolMaterial{
    WOOD(ToolMaterials.WOOD, "wood"),
    STONE(ToolMaterials.STONE, "stone"),
    IRON(ToolMaterials.IRON, "iron"),
    DIAMOND(ToolMaterials.DIAMOND, "diamond"),
    GOLD(ToolMaterials.GOLD, "gold"),
    STEEL(ToolMaterials.DIAMOND.getAttackDamage(), ToolMaterials.IRON.getMiningSpeed(), ToolMaterials.IRON.getDurability()+50, 
        ToolMaterials.DIAMOND.getMiningLevel(), ToolMaterials.IRON.getEnchantability(), CommonItems.getItem("steel_ingot"), "steel"),
    COPPER(ToolMaterials.STONE.getAttackDamage(), ToolMaterials.IRON.getMiningSpeed(), ToolMaterials.STONE.getDurability()+30, 
        ToolMaterials.STONE.getMiningLevel(), ToolMaterials.IRON.getEnchantability(), CommonItems.getItem("copper_ingot"), "copper");

    public final String name;
    private float attackDamage, efficiency;
    private int durability, harvestLevel, enchantability;
    private Item repairMaterial;
    private Ingredient repairIngredient;

    private HeadMaterials(float attackDamage, float efficiency, int durability, int harvestLevel, int enchantability, Item repairMaterial, String name){
        this.attackDamage = attackDamage;
        this.durability = durability;
        this.enchantability = enchantability;
        this.harvestLevel = harvestLevel;
        this.repairMaterial = repairMaterial;
        this.efficiency = efficiency;
        this.name = name;
    }

    private HeadMaterials(ToolMaterial material, String name){
        this.attackDamage = material.getAttackDamage();
        this.durability = material.getDurability();
        this.efficiency = material.getMiningSpeed();
        this.enchantability = material.getEnchantability();
        this.harvestLevel = material.getMiningLevel();
        this.repairIngredient = material.getRepairIngredient();
        this.name = name;
    }

    public Item getRepairItem(){
        switch(name){
            case "wood": return Items.OAK_PLANKS;
            case "stone": return Blocks.COBBLESTONE.asItem();
            case "iron": return Items.IRON_INGOT;
            case "gold": return Items.GOLD_INGOT;
            case "diamond": return Items.DIAMOND;
            case "steel": return CommonItems.getItem("steel_ingot");
            case "copper": return CommonItems.getItem("copper_ingot");
            default: return Items.IRON_INGOT;
        }
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
        return (repairMaterial == null) ? repairIngredient : Ingredient.ofItems(repairMaterial);
    }
}