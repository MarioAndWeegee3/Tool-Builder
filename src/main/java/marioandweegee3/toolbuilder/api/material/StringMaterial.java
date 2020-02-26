package marioandweegee3.toolbuilder.api.material;

import java.util.Set;

import marioandweegee3.toolbuilder.api.effect.EffectInstance;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;

public interface StringMaterial{
    String getMod();
    String getName();
    Set<EffectInstance> getEffects();
    Identifier[] getIngredientIds();

    default Identifier getID(){
        return new Identifier(getMod(), getName());
    }
}