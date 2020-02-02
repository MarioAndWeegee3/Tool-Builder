package marioandweegee3.toolbuilder.api.material;

import java.util.ArrayList;
import java.util.Set;

import marioandweegee3.toolbuilder.api.effect.EffectInstance;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

public interface HandleMaterial {
    public int getExtraDurability();
    public ArrayList<Item> getRepairItems(boolean grip);
    public float getMiningSpeedMultiplier();
    public float getDrawSpeedMultiplier();
    public float getDurabilityMultiplier();
    public int getEnchantabilityModifier();
    public String getName();
    public String getMod();
    public Set<EffectInstance> getEffects();
    public String getCraftIngredient();

    public default String getTranslationKey(){
        return "handle."+getMod()+"."+getName();
    }

    public default Identifier getID(){
        return new Identifier(getMod(), getName());
    }
}