package marioandweegee3.toolbuilder.common.tools;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import marioandweegee3.toolbuilder.api.effect.Effect;
import marioandweegee3.toolbuilder.api.material.StringMaterial;
import marioandweegee3.toolbuilder.common.effect.Effects;
import net.minecraft.item.Item;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;
import net.minecraft.util.Lazy;
import net.minecraft.util.registry.Registry;

public enum StringMaterials implements StringMaterial {
    STRING("string", new Effect[0], new Identifier("string")),
    BLAZE("blaze", new Effect[] { Effects.FLAMING }, new Identifier("toolbuilder:blaze_string"));

    private Lazy<Ingredient> ingredient;
    private Set<Effect> effects;
    private String name;
    private Identifier[] ids;

    private StringMaterials(String name, Effect[] effects, Identifier... ids) {
        this.name = name;
        this.effects = new HashSet<>(0);
        for (Effect effect : effects) {
            this.effects.add(effect);
        }
        this.ingredient = new Lazy<Ingredient>(() -> {
            Item[] items = new Item[ids.length];
            for (int i = 0; i < ids.length; i++) {
                items[i] = Registry.ITEM.get(ids[i]);
            }
            return Ingredient.ofItems(items);
        });
        this.ids = ids;
    }

    @Override
    public Ingredient getIngredient() {
        return ingredient.get();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ArrayList<Effect> getEffects() {
        return new ArrayList<>(effects);
    }

    @Override
    public Identifier[] getIngredientIds() {
        return ids;
    }

}