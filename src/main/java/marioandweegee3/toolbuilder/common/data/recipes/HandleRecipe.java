package marioandweegee3.toolbuilder.common.data.recipes;

import com.swordglowsblue.artifice.api.ArtificeResourcePack.ServerResourcePackBuilder;

import marioandweegee3.toolbuilder.api.material.HandleMaterial;
import marioandweegee3.toolbuilder.common.tools.HandleMaterials;
import net.minecraft.util.Identifier;

public class HandleRecipe {
    private HandleMaterial material;

    public HandleRecipe(HandleMaterial material){
        this.material = material;
    }

    public void add(ServerResourcePackBuilder pack){
        String[] handleCraft = new String[]{material.getCraftIngredient()};
        boolean[] ingTag = new boolean[]{false};

        if(handleCraft[0].startsWith("#")){
            ingTag[0] = true;
            handleCraft[0] = handleCraft[0].substring(1);
        }
        
        if(material != HandleMaterials.WOOD){
            pack.addShapedRecipe(new Identifier(material.getMod(), material.getName()), recipe -> {
                recipe.pattern(
                    "x",
                    "x"
                );
    
                if(ingTag[0]){
                    recipe.ingredientTag('x', new Identifier(handleCraft[0]));
                } else {
                    recipe.ingredientItem('x', new Identifier(handleCraft[0]));
                }
    
                recipe.result(new Identifier(material.getMod(), material.getName()+"_handle"), 4);
            });
        }

        pack.addShapedRecipe(new Identifier(material.getMod(), material.getName()+"_2"), recipe -> {
            recipe.pattern(
                "x",
                "x",
                "x"
            );

            if(ingTag[0]){
                recipe.ingredientTag('x', new Identifier(handleCraft[0]));
            } else {
                recipe.ingredientItem('x', new Identifier(handleCraft[0]));
            }

            recipe.result(new Identifier(material.getMod(), material.getName()+"_handle"), 6);
        });

        pack.addStonecuttingRecipe(new Identifier(material.getMod(), material.getName()+"_stonecutting"), recipe -> {
            if(ingTag[0]){
                recipe.ingredientTag(new Identifier(handleCraft[0]));
            } else {
                recipe.ingredientItem(new Identifier(handleCraft[0]));
            }

            recipe.result(new Identifier(material.getMod(), material.getName()+"_handle"));
            recipe.count(2);
        });
    }
}