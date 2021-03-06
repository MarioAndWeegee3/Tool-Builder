package marioandweegee3.toolbuilder.common.data.recipes;

import com.swordglowsblue.artifice.api.ArtificeResourcePack.ServerResourcePackBuilder;
import com.swordglowsblue.artifice.api.builder.data.recipe.ShapedRecipeBuilder;

import marioandweegee3.toolbuilder.ToolBuilder;
import marioandweegee3.toolbuilder.api.material.BowMaterial;
import marioandweegee3.toolbuilder.api.material.HandleMaterial;
import marioandweegee3.toolbuilder.api.material.StringMaterial;
import marioandweegee3.toolbuilder.common.config.ConfigHandler;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class BowRecipe {
    public ToolBuilder.BowBuilder builder;
    public BowRecipe(ToolBuilder.BowBuilder builder){
        this.builder = builder;
    }

    public void add(ServerResourcePackBuilder pack){
        pack.addShapedRecipe(ToolBuilder.makeID(builder.name), this::processRecipe);
    }

    public void processRecipe(ShapedRecipeBuilder recipe){
        BowMaterial material = builder.getMaterial();
        StringMaterial string = material.string;
        HandleMaterial handle = material.handle;

        recipe.pattern(
                " ys",
                "y s",
                " ys"
        );
        
        recipe.group(ToolBuilder.makeID("bow_"+builder.getMaterial().handle.getName()));

        if(handle.getRepairItems(material.grip).contains(Items.STICK)){
            if(ConfigHandler.getInstance().canCraftWithSticks()){
                recipe.multiIngredient('y', ing -> {
                    ing.item(new Identifier("stick"));
                    for(Item item : handle.getRepairItems(material.grip)){
                        if(item != Items.STICK){
                            ing.item(Registry.ITEM.getId(item));
                        }
                    }
                });
            } else {
                recipe.multiIngredient('y', ing -> {
                    for(Item item : handle.getRepairItems(material.grip)){
                        if(item != Items.STICK){
                            ing.item(Registry.ITEM.getId(item));
                        }
                    }
                });
            }
        } else {
            recipe.multiIngredient('y', ing -> {
                for(Item item : handle.getRepairItems(material.grip)){
                    ing.item(Registry.ITEM.getId(item));
                }
            });
        }

        recipe.multiIngredient('s', ing -> {
            for(Identifier id : string.getIngredientIds()){
                ing.item(id);
            }
        });

        recipe.result(ToolBuilder.makeID(builder.name), 1);
    }
}