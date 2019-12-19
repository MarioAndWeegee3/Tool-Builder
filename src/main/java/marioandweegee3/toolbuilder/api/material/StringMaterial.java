package marioandweegee3.toolbuilder.api.material;

import java.util.Set;

import marioandweegee3.toolbuilder.api.effect.EffectInstance;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;

public interface StringMaterial{
    public Ingredient getIngredient();
    public String getMod();
    public String getName();
    public Set<EffectInstance> getEffects();
    public Identifier[] getIngredientIds();
}