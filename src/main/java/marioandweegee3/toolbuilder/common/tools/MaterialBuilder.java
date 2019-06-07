package marioandweegee3.toolbuilder.common.tools;

import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;

public class MaterialBuilder{
    private Material material;

    public static String[] materialNames = new String[]{
        "wood",
        "stone",
        "iron",
        "diamond",
        "gold",
        "steel",
        "copper"
    };

    private MaterialBuilder(HandleMaterials handle, HeadMaterials head, String name) {
        this.material = new Material(handle, head, name);
    }

    private Material getMaterial(){
        return this.material;
    }

    public static Material build(HandleMaterials handle, HeadMaterials head, String name){
        MaterialBuilder builder = new MaterialBuilder(handle, head, name);
        return builder.getMaterial();
    }

    public static class Material implements ToolMaterial{
        public final HandleMaterials handle;
        public final HeadMaterials head;
        private String name;

        public Material(HandleMaterials handle, HeadMaterials head, String name){
            this.handle = handle;
            this.head = head;
            this.name = name;
        }

        @Override
        public Ingredient getRepairIngredient() {
            return this.head.getRepairIngredient();
        }
    
        @Override
        public float getMiningSpeed() {
            return this.head.getMiningSpeed();
        }
    
        @Override
        public int getMiningLevel() {
            return this.head.getMiningLevel();
        }
    
        @Override
        public int getEnchantability() {
            int newEnchant = this.head.getEnchantability();
            newEnchant += this.handle.getEnchantabilityModifier();
            return newEnchant;
        }
    
        @Override
        public int getDurability() {
            int newDurability = this.head.getDurability();
            newDurability += this.handle.getDurabilityModifier();
            newDurability *= this.handle.getExtraDurability();
            return newDurability;
        }
    
        @Override
        public float getAttackDamage() {
            return this.head.getAttackDamage();
        }

        public String getName(){
            return this.name;
        }
    }
}