package marioandweegee3.toolbuilder.api.material;

import java.util.ArrayList;
import java.util.Set;

import marioandweegee3.toolbuilder.api.effect.EffectInstance;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

public interface HandleMaterial {
    int getExtraDurability();
    ArrayList<Item> getRepairItems(boolean grip);
    float getMiningSpeedMultiplier();
    float getDrawSpeedMultiplier();
    float getDurabilityMultiplier();
    int getEnchantabilityModifier();
    String getName();
    String getMod();
    Set<EffectInstance> getEffects();
    String getCraftIngredient();

    default String getTranslationKey(){
        return "handle."+getMod()+"."+getName();
    }

    default Identifier getID(){
        return new Identifier(getMod(), getName());
    }
}