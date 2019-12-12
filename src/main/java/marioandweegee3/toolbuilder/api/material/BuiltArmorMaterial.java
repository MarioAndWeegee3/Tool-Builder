package marioandweegee3.toolbuilder.api.material;

import java.util.ArrayList;

import marioandweegee3.toolbuilder.api.effect.Effect;
import net.minecraft.item.ArmorMaterial;

public interface BuiltArmorMaterial extends ArmorMaterial{
    public String getMaterialName();
    public String getMod();
    public ArrayList<Effect> getEffects();
    public String getRepairString();
    public boolean isCotton();
}