package marioandweegee3.toolbuilder.api.material;

import java.util.ArrayList;

import marioandweegee3.toolbuilder.api.effect.Effect;
import net.minecraft.item.Item;

public interface HandleMaterial {
    public float getDurabilityModifier();
    public ArrayList<Item> getRepairItems(boolean grip);
    public float getMiningSpeedMultiplier();
    public float getDrawSpeedMultiplier();
    public float getExtraDurability();
    public int getEnchantabilityModifier();
    public String getName();
    public String getMod();
    public ArrayList<Effect> getEffects();

    public default String getTranslationKey(){
        return "handle."+getMod()+"."+getName();
    }
}