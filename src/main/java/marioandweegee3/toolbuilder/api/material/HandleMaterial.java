package marioandweegee3.toolbuilder.api.material;

import java.util.ArrayList;

import marioandweegee3.toolbuilder.api.effect.Effect;
import net.minecraft.item.Item;

public interface HandleMaterial {
    public int getExtraDurability();
    public ArrayList<Item> getRepairItems(boolean grip);
    public float getMiningSpeedMultiplier();
    public float getDrawSpeedMultiplier();
    public float getDurabilityMultiplier();
    public int getEnchantabilityModifier();
    public String getName();
    public String getMod();
    public ArrayList<Effect> getEffects();

    public default String getTranslationKey(){
        return "handle."+getMod()+"."+getName();
    }
}