package marioandweegee3.toolbuilder.api.material;

import java.util.ArrayList;

import marioandweegee3.toolbuilder.api.effect.Effect;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.util.Identifier;

public interface BuiltArmorMaterial extends ArmorMaterial{
    String getMaterialName();
    String getMod();
    ArrayList<Effect> getEffects();
    String getRepairString();

    default Identifier getID(){
        return new Identifier(getMod(), getMaterialName());
    }
}