package marioandweegee3.toolbuilder.common.tools.tooltypes;

import marioandweegee3.toolbuilder.api.ToolCreator;
import marioandweegee3.toolbuilder.api.ToolType;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

public enum ToolTypes implements ToolType{
    AXE(new String[]{
        "xx",
        "yx",
        "y "
    },"axe", Axe::create, new Identifier("fabric:axes")),
    PICKAXE(new String[]{
        "xxx",
        " y ",
        " y "
    }, "pickaxe", Pickaxe::create, new Identifier("fabric:pickaxes")),
    HAMMER(new String[]{
        "zxz",
        " y ",
        " y "
    }, "hammer", Hammer::create, new Identifier("fabric:pickaxes")),
    SWORD(new String[]{
        "x",
        "x",
        "y"
    },"sword", Sword::create, new Identifier("fabric:swords")),
    KNIFE(new String[]{
        "x",
        "y"
    }, "knife", Knife::create, new Identifier("fabric:swords")),
    SHOVEL(new String[]{
        "x",
        "y",
        "y"
    }, "shovel", Shovel::create, new Identifier("fabric:shovels")),
    HOE(new String[]{
        "xx",
        "y ",
        "y "
    }, "hoe", Hoe::create, new Identifier("fabric:hoes")),
    GREATSWORD(new String[]{
        "zx",
        "zx",
        "y "
    }, "greatsword", GreatSword::create, new Identifier("fabric:swords")),
    SHEARS(new String[]{
        "x ",
        "yy"
    }, "shears", Shears::create, null),
    EXCAVATOR(new String[]{
        " x ",
        "zyz",
        " y "
    }, "excavator", Excavator::create, new Identifier("fabric:shovels")),
    RAPIER(new String[]{
        "  x",
        " x ",
        "y  "
    }, "rapier", Rapier::create, new Identifier("fabric:swords"))
    ;
    private String name;
    private ToolCreator<? extends Item> creator;
    private String[] pattern;
    private Identifier tag;

    ToolTypes(String[] pattern, String name, ToolCreator<? extends Item> creator, Identifier tag){
        this.name = name;
        this.creator = creator;
        this.pattern = pattern;
        this.tag = tag;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ToolCreator<? extends Item> getBuilder() {
        return creator;
    }

    @Override
    public String[] getRecipePattern() {
        return pattern;
    }

    @Override
    public int getHandleGripCost() {
        int cost = 0;
        for(String row : pattern){
            for(char item : row.toCharArray()){
                if(item == 'y'){
                    cost++;
                }
            }
        }
        return Math.max(cost, 1);
    }

    public static ToolType get(String id){
        for(ToolType type : values()){
            if(type.getName().equals(id)){
                return type;
            }
        }

        return null;
    }

    @Override
    public Identifier fabricTagToAddTo() {
        return tag;
    }
}