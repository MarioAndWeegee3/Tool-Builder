package marioandweegee3.toolbuilder.common.data.recipes;

import com.swordglowsblue.artifice.api.ArtificeResourcePack.ServerResourcePackBuilder;
import com.swordglowsblue.artifice.api.builder.data.recipe.ShapedRecipeBuilder;

import marioandweegee3.toolbuilder.ToolBuilder;
import marioandweegee3.toolbuilder.api.material.BuiltArmorMaterial;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.Identifier;

public class ArmorRecipe {
    public ToolBuilder.ArmorBuilder builder;
    public EquipmentSlot slot;

    public ArmorRecipe(ToolBuilder.ArmorBuilder builder, EquipmentSlot slot){
        this.builder = builder;
        this.slot = slot;
    }

    public void add(ServerResourcePackBuilder pack){
        pack.addShapedRecipe(ToolBuilder.makeID(builder.makeName(slot)), this::processRecipe);
    }

    public void processRecipe(ShapedRecipeBuilder recipe){
        BuiltArmorMaterial material = builder.armorMaterial;

        recipe.pattern(getPattern(slot));
        recipe.group(ToolBuilder.makeID(builder.getTypeString(slot)));

        if (material.getRepairString().startsWith("#")) {
            Identifier id = new Identifier(material.getRepairString().substring(1));
            recipe.ingredientTag('x', id);
        } else {
            Identifier id = new Identifier(material.getRepairString());
            recipe.ingredientItem('x', id);
        }

        recipe.result(ToolBuilder.makeID(builder.makeName(slot)), 1);
    }

    private static String[] getPattern(EquipmentSlot slot){
        if(slot == EquipmentSlot.HEAD){
            return new String[]{
                "xxx",
                "x x"
            };
        } else if(slot == EquipmentSlot.CHEST){
            return new String[]{
                "x x",
                "xxx",
                "xxx"
            };
        } else if(slot == EquipmentSlot.LEGS){
            return new String[]{
                "xxx",
                "x x",
                "x x"
            };
        } else if(slot == EquipmentSlot.FEET){
            return new String[]{
                "x x",
                "x x"
            };
        } else {
            return new String[0];
        }
    }
}