package marioandweegee3.toolbuilder.common.data;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.swordglowsblue.artifice.api.ArtificeResourcePack.ServerResourcePackBuilder;
import com.swordglowsblue.artifice.api.builder.data.recipe.CookingRecipeBuilder;
import com.swordglowsblue.artifice.api.builder.data.recipe.ShapedRecipeBuilder;
import com.swordglowsblue.artifice.api.builder.data.recipe.ShapelessRecipeBuilder;
import com.swordglowsblue.artifice.api.util.Processor;

import marioandweegee3.toolbuilder.common.data.loot_tables.BasicBlockLootTable;
import marioandweegee3.toolbuilder.common.data.recipes.ArmorRecipe;
import marioandweegee3.toolbuilder.common.data.recipes.BowRecipe;
import marioandweegee3.toolbuilder.common.data.recipes.ToolRecipe;
import net.minecraft.util.Identifier;

public class TBData {
    public static Set<ToolRecipe> toolRecipes = new HashSet<>();
    public static Set<BowRecipe> bowRecipes = new HashSet<>();
    public static Set<ArmorRecipe> armorRecipes = new HashSet<>();
    public static Set<BasicBlockLootTable> blockLootTables = new HashSet<>();

    public static Map<Identifier, Processor<ShapedRecipeBuilder>> shapedRecipes = new HashMap<>();
    public static Map<Identifier, Processor<ShapelessRecipeBuilder>> shapelessRecipes = new HashMap<>();
    public static Map<Identifier, Processor<CookingRecipeBuilder>> blastingRecipes = new HashMap<>();

    public static void addRecipes(ServerResourcePackBuilder pack){
        for(ToolRecipe recipe : toolRecipes){
            recipe.add(pack);
        }
        for(BowRecipe recipe : bowRecipes){
            recipe.add(pack);
        }
        for(ArmorRecipe recipe : armorRecipes){
            recipe.add(pack);
        }

        for(Identifier id : shapedRecipes.keySet()){
            pack.addShapedRecipe(id, shapedRecipes.get(id));
        }

        for(Identifier id : shapelessRecipes.keySet()){
            pack.addShapelessRecipe(id, shapelessRecipes.get(id));
        }

        for(Identifier id : blastingRecipes.keySet()){
            pack.addBlastingRecipe(id, blastingRecipes.get(id));
        }
        
        for(BasicBlockLootTable table : blockLootTables){
            table.add(pack);
        }
    }
}