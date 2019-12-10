package marioandweegee3.toolbuilder.api.material;

import marioandweegee3.toolbuilder.api.effect.Effect;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;

public interface HeadMaterial extends ToolMaterial{
    public static HeadMaterial copy(ToolMaterial base, String name) {
        return new HeadMaterial() {

            @Override
            public int getDurability() {
                return base.getDurability();
            }

            @Override
            public float getMiningSpeed() {
                return base.getMiningSpeed();
            }

            @Override
            public float getAttackDamage() {
                return base.getAttackDamage();
            }

            @Override
            public int getMiningLevel() {
                return base.getMiningLevel();
            }

            @Override
            public int getEnchantability() {
                return base.getEnchantability();
            }

            @Override
            public Ingredient getRepairIngredient() {
                return base.getRepairIngredient();
			}

            @Override
            public String getName() {
                return name;
            }

            @Override
            public Effect[] getEffects() {
                return new Effect[0];
            }

            @Override
            public String getRepairString() {
                return "";
            }

            @Override
            public String getBlockString() {
                return "";
            }

        };
    }

    public Effect[] getEffects();
    public String getName();
    public String getRepairString();
    public String getBlockString();
}