package marioandweegee3.toolbuilder.api.material;

import java.util.ArrayList;

import marioandweegee3.toolbuilder.api.effect.Effect;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;

public interface StringMaterial{
    public Ingredient getIngredient();
    public String getName();
    public ArrayList<Effect> getEffects();
    public Identifier[] getIngredientIds();
}