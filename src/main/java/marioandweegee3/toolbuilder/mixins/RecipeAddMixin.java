package marioandweegee3.toolbuilder.mixins;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.swordglowsblue.artifice.api.builder.data.recipe.CookingRecipeBuilder;
import com.swordglowsblue.artifice.api.builder.data.recipe.ShapedRecipeBuilder;
import com.swordglowsblue.artifice.api.builder.data.recipe.ShapelessRecipeBuilder;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import marioandweegee3.toolbuilder.ToolBuilder;
import marioandweegee3.toolbuilder.common.data.TBData;
import marioandweegee3.toolbuilder.common.data.recipes.ArmorRecipe;
import marioandweegee3.toolbuilder.common.data.recipes.BowRecipe;
import marioandweegee3.toolbuilder.common.data.recipes.ToolRecipe;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.recipe.RecipeType;
import net.minecraft.resource.JsonDataLoader;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;

@Mixin(RecipeManager.class)
public abstract class RecipeAddMixin extends JsonDataLoader {

    @Shadow
    private Map<RecipeType<?>, Map<Identifier, Recipe<?>>> recipes;

    public RecipeAddMixin(Gson gson, String dataType) {
        super(gson, dataType);
    }

    @Inject(method = "apply", at = @At("RETURN"))
    private void addRecipes(Map<Identifier, JsonObject> map, ResourceManager resourceManager, Profiler profiler, CallbackInfo ci){
        Map<RecipeType<?>, Map<Identifier, Recipe<?>>> mutableRecipes = new HashMap<>(recipes);
        Map<Identifier, Recipe<?>> craftingRecipes = new HashMap<>(mutableRecipes.get(RecipeType.CRAFTING));
        Map<Identifier, Recipe<?>> blastingRecipes = new HashMap<>(mutableRecipes.get(RecipeType.BLASTING));
        
        for(ToolRecipe recipe : TBData.toolRecipes){
            ShapedRecipeBuilder recipeBuilder = new ShapedRecipeBuilder();
            Identifier recipeId = ToolBuilder.makeID(recipe.builder.name);

            recipe.processRecipe(recipeBuilder);

            craftingRecipes.put(recipeId, RecipeManager.deserialize(recipeId, recipeBuilder.build().getData()));
        }

        for(BowRecipe recipe : TBData.bowRecipes){
            ShapedRecipeBuilder recipeBuilder = new ShapedRecipeBuilder();
            Identifier recipeId = ToolBuilder.makeID(recipe.builder.name);

            recipe.processRecipe(recipeBuilder);

            craftingRecipes.put(recipeId, RecipeManager.deserialize(recipeId, recipeBuilder.build().getData()));
        }

        for(ArmorRecipe recipe : TBData.armorRecipes){
            ShapedRecipeBuilder recipeBuilder = new ShapedRecipeBuilder();
            Identifier recipeId = ToolBuilder.makeID(recipe.builder.makeName(recipe.slot));

            recipe.processRecipe(recipeBuilder);

            craftingRecipes.put(recipeId, RecipeManager.deserialize(recipeId, recipeBuilder.build().getData()));
        }

        for(Identifier id : TBData.shapedRecipes.keySet()){
            ShapedRecipeBuilder recipeBuilder = new ShapedRecipeBuilder();

            TBData.shapedRecipes.get(id).accept(recipeBuilder);

            craftingRecipes.put(id, RecipeManager.deserialize(id, recipeBuilder.build().getData()));
        }

        for(Identifier id : TBData.shapelessRecipes.keySet()){
            ShapelessRecipeBuilder recipeBuilder = new ShapelessRecipeBuilder();

            TBData.shapelessRecipes.get(id).accept(recipeBuilder);

            craftingRecipes.put(id, RecipeManager.deserialize(id, recipeBuilder.build().getData()));
        }

        for(Identifier id : TBData.blastingRecipes.keySet()){
            CookingRecipeBuilder recipeBuilder = new CookingRecipeBuilder();

            TBData.blastingRecipes.get(id).accept(recipeBuilder);

            blastingRecipes.put(id, RecipeManager.deserialize(id, recipeBuilder.build().getData()));
        }

        mutableRecipes.put(RecipeType.CRAFTING, new ImmutableMap.Builder<Identifier, Recipe<?>>().putAll(craftingRecipes).putAll(blastingRecipes).build());
        this.recipes = new ImmutableMap.Builder<RecipeType<?>, Map<Identifier, Recipe<?>>>().putAll(mutableRecipes).build();
        ToolBuilder.logger.info("Loaded Tool Builder Recipes");
    }

}