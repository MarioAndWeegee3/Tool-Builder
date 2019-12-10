package marioandweegee3.toolbuilder.common.data;

import java.util.HashSet;
import java.util.Set;

import com.swordglowsblue.artifice.api.ArtificeResourcePack.ServerResourcePackBuilder;

import marioandweegee3.toolbuilder.common.data.loot_tables.BasicBlockLootTable;
import marioandweegee3.toolbuilder.common.data.recipes.ArmorRecipe;
import marioandweegee3.toolbuilder.common.data.recipes.BowRecipe;
import marioandweegee3.toolbuilder.common.data.recipes.ToolRecipe;

public class TBData {
    public static Set<ToolRecipe> toolRecipes = new HashSet<>();
    public static Set<BowRecipe> bowRecipes = new HashSet<>();
    public static Set<ArmorRecipe> armorRecipes = new HashSet<>();
    public static Set<BasicBlockLootTable> blockLootTables = new HashSet<>();

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
        
        for(BasicBlockLootTable table : blockLootTables){
            table.add(pack);
        }
    }
}