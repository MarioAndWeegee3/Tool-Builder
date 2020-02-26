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
import marioandweegee3.toolbuilder.common.data.recipes.HandleRecipe;
import marioandweegee3.toolbuilder.common.data.recipes.ToolRecipe;
import net.minecraft.util.Identifier;

public class TBData {
    private static Set<ToolRecipe> toolRecipes = new HashSet<>();
    private static Set<BowRecipe> bowRecipes = new HashSet<>();
    private static Set<ArmorRecipe> armorRecipes = new HashSet<>();
    private static Set<HandleRecipe> handleRecipes = new HashSet<>();
    private static Set<BasicBlockLootTable> blockLootTables = new HashSet<>();

    private static Map<Identifier, Processor<ShapedRecipeBuilder>> shapedRecipes = new HashMap<>();
    private static Map<Identifier, Processor<ShapelessRecipeBuilder>> shapelessRecipes = new HashMap<>();
    private static Map<Identifier, Processor<CookingRecipeBuilder>> blastingRecipes = new HashMap<>();

    public static void addRecipes(ServerResourcePackBuilder pack){
        for(ToolRecipe recipe : getToolRecipes()){
            recipe.add(pack);
        }
        for(BowRecipe recipe : getBowRecipes()){
            recipe.add(pack);
        }
        for(ArmorRecipe recipe : getArmorRecipes()){
            recipe.add(pack);
        }
        for(HandleRecipe recipe : getHandleRecipes()){
            recipe.add(pack);
        }

        for(Identifier id : getShapedRecipes().keySet()){
            pack.addShapedRecipe(id, getShapedRecipes().get(id));
        }

        for(Identifier id : getShapelessRecipes().keySet()){
            pack.addShapelessRecipe(id, getShapelessRecipes().get(id));
        }

        for(Identifier id : getBlastingRecipes().keySet()){
            pack.addBlastingRecipe(id, getBlastingRecipes().get(id));
        }
        
        for(BasicBlockLootTable table : getBlockLootTables()){
            table.add(pack);
        }
    }

    public static Set<ToolRecipe> getToolRecipes() {
        return toolRecipes;
    }

    public static Set<BowRecipe> getBowRecipes() {
        return bowRecipes;
    }

    public static Set<ArmorRecipe> getArmorRecipes() {
        return armorRecipes;
    }

    public static Set<HandleRecipe> getHandleRecipes() {
        return handleRecipes;
    }

    public static Set<BasicBlockLootTable> getBlockLootTables() {
        return blockLootTables;
    }

    public static Map<Identifier, Processor<ShapedRecipeBuilder>> getShapedRecipes() {
        return shapedRecipes;
    }

    public static Map<Identifier, Processor<ShapelessRecipeBuilder>> getShapelessRecipes() {
        return shapelessRecipes;
    }

    public static Map<Identifier, Processor<CookingRecipeBuilder>> getBlastingRecipes() {
        return blastingRecipes;
    }
}