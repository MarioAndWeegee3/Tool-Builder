package marioandweegee3.toolbuilder.api.entry;

import marioandweegee3.toolbuilder.api.effect.Effect;
import marioandweegee3.toolbuilder.api.material.BuiltArmorMaterial;
import marioandweegee3.toolbuilder.api.material.HandleMaterial;
import marioandweegee3.toolbuilder.api.material.HeadMaterial;
import marioandweegee3.toolbuilder.api.material.StringMaterial;

public interface TBInitializer {
    HeadMaterial[] headMaterials();
    HandleMaterial[] handleMaterials();
    StringMaterial[] stringMaterials();
    BuiltArmorMaterial[] armorMaterials();
    Effect[] effects();
}