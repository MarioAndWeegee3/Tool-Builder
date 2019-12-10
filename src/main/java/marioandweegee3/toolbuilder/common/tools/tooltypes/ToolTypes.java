package marioandweegee3.toolbuilder.common.tools.tooltypes;

import marioandweegee3.toolbuilder.api.ToolCreator;
import marioandweegee3.toolbuilder.api.ToolType;
import net.minecraft.item.Item;

public enum ToolTypes implements ToolType{
    AXE(new String[]{
        "xx",
        "yx",
        "y "
    },"axe", Axe::create),
    PICKAXE(new String[]{
        "xxx",
        " y ",
        " y "
    }, "pickaxe", Pickaxe::create),
    HAMMER(new String[]{
        "zxz",
        " y ",
        " y "
    }, "hammer", Hammer::create),
    SWORD(new String[]{
        "x",
        "x",
        "y"
    },"sword", Sword::create),
    KNIFE(new String[]{
        "x",
        "y"
    }, "knife", Knife::create),
    SHOVEL(new String[]{
        "x",
        "y",
        "y"
    }, "shovel", Shovel::create),
    HOE(new String[]{
        "xx",
        "y ",
        "y "
    }, "hoe", Hoe::create),
    GREATSWORD(new String[]{
        "zx",
        "zx",
        "y "
    }, "greatsword", Greatsword::create),
    SHEARS(new String[]{
        "x ",
        "yy"
    }, "shears", Shears::create),
    EXCAVATOR(new String[]{
        " x ",
        "zyz",
        " y "
    }, "excavator", Excavator::create),
    RAPIER(new String[]{
        "  x",
        " x ",
        "y  "
    }, "rapier", Rapier::create)
    ;
    private String name;
    private ToolCreator<? extends Item> creator;
    private String[] pattern;

    private ToolTypes(String[] pattern, String name, ToolCreator<? extends Item> creator){
        this.name = name;
        this.creator = creator;
        this.pattern = pattern;
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
        return cost >= 1 ? cost : 1;
    }

    public static ToolType get(String id){
        for(ToolType type : values()){
            if(type.getName().equals(id)){
                return type;
            }
        }

        return null;
    }
}