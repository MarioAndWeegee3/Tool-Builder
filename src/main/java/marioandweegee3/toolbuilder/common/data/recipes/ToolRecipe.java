package marioandweegee3.toolbuilder.common.data.recipes;

import com.swordglowsblue.artifice.api.ArtificeResourcePack.ServerResourcePackBuilder;
import com.swordglowsblue.artifice.api.builder.data.recipe.ShapedRecipeBuilder;

import marioandweegee3.toolbuilder.ToolBuilder;
import marioandweegee3.toolbuilder.api.ToolType;
import marioandweegee3.toolbuilder.api.item.BigTool;
import marioandweegee3.toolbuilder.api.material.BuiltToolMaterial;
import marioandweegee3.toolbuilder.api.material.HandleMaterial;
import marioandweegee3.toolbuilder.api.material.HeadMaterial;
import marioandweegee3.toolbuilder.common.config.ConfigHandler;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ToolRecipe {
    public ToolBuilder.ToolItemBuilder builder;
    public ToolRecipe(ToolBuilder.ToolItemBuilder builder){
        this.builder = builder;
    }

    public void add(ServerResourcePackBuilder pack){
        pack.addShapedRecipe(ToolBuilder.makeID(builder.name), this::processRecipe);

        if(builder.getType().fabricTagToAddTo() == null) return;

        pack.addItemTag(builder.getType().fabricTagToAddTo(), tag -> {
            tag.replace(false);
            tag.value(ToolBuilder.makeID(builder.name));
        });
    }

    public void processRecipe(ShapedRecipeBuilder recipe) {
        BuiltToolMaterial material = builder.getMaterial();
        ToolType type = builder.getType();
        HeadMaterial head = material.head;
        HandleMaterial handle = material.handle;

        recipe.pattern(type.getRecipePattern());
        recipe.group(ToolBuilder.makeID(builder.getType().getName()+"_"+builder.getMaterial().head.getName()));

        if(head.getRepairString().startsWith("#")){
            Identifier id = new Identifier(head.getRepairString().substring(1));
            recipe.ingredientTag('x', id);
        } else {
            Identifier id = new Identifier(head.getRepairString());
            recipe.ingredientItem('x', id);
        }

        if (handle.getRepairItems(material.isGripped).contains(Items.STICK)) {
            if (ConfigHandler.getInstance().canCraftWithSticks()) {
                recipe.multiIngredient('y', ing -> {
                    ing.item(new Identifier("stick"));
                    for (Item item : handle.getRepairItems(material.isGripped)) {
                        if (item != Items.STICK) {
                            ing.item(Registry.ITEM.getId(item));
                        }
                    }
                });
            } else {
                recipe.multiIngredient('y', ing -> {
                    for (Item item : handle.getRepairItems(material.isGripped)) {
                        if (item != Items.STICK) {
                            ing.item(Registry.ITEM.getId(item));
                        }
                    }
                });
            }
        } else {
            recipe.multiIngredient('y', ing -> {
                for (Item item : handle.getRepairItems(material.isGripped)) {
                    ing.item(Registry.ITEM.getId(item));
                }
            });
        }

        Item tool = Registry.ITEM.get(ToolBuilder.makeID(builder.name));
        if (tool instanceof BigTool) {
            if (head.getBlockString().startsWith("#")) {
                Identifier id = new Identifier(head.getBlockString().substring(1));
                recipe.ingredientTag('z', id);
            } else {
                Identifier id = new Identifier(head.getBlockString());
                recipe.ingredientItem('z', id);
            }
        }
        recipe.result(ToolBuilder.makeID(builder.name), 1);
    }
}