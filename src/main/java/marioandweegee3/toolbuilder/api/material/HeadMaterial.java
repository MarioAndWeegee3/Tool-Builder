package marioandweegee3.toolbuilder.api.material;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import marioandweegee3.toolbuilder.api.effect.EffectInstance;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;

public interface HeadMaterial extends ToolMaterial{
    static HeadMaterial copy(ToolMaterial base, String name) {
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
            public Set<EffectInstance> getEffects() {
                return new HashSet<>();
            }

            @Override
            public String getRepairString() {
                return "";
            }

            @Override
            public String getBlockString() {
                return "";
            }

            @Override
            public String getMod() {
                return "";
            }

            @Override
            public String getUSTranslation() {
                return StringUtils.capitalize(this.getName());
            }

        };
    }

    Set<EffectInstance> getEffects();
    String getName();
    String getRepairString();
    String getBlockString();

    String getMod();
    String getUSTranslation();

    default Identifier getID() {
        return new Identifier(getMod(), getName());
    }
}