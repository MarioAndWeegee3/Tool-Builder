package marioandweegee3.toolbuilder.common.tools.init;

import marioandweegee3.toolbuilder.api.effect.Effect;
import marioandweegee3.toolbuilder.api.entry.TBInitializer;
import marioandweegee3.toolbuilder.api.material.*;
import marioandweegee3.toolbuilder.common.tools.*;
import marioandweegee3.toolbuilder.common.armor.BuiltArmorMaterials;
import marioandweegee3.toolbuilder.common.effect.Effects;

@SuppressWarnings("unused")
public class MaterialInit implements TBInitializer {

    @Override
    public HeadMaterial[] headMaterials() {
        return HeadMaterials.values();
    }

    @Override
    public HandleMaterial[] handleMaterials() {
        return HandleMaterials.values();
    }

    @Override
    public StringMaterial[] stringMaterials() {
        return StringMaterials.values();
    }

    @Override
    public BuiltArmorMaterial[] armorMaterials() {
        return BuiltArmorMaterials.values();
    }

    @Override
    public Effect[] effects() {
        return Effects.values();
    }

}