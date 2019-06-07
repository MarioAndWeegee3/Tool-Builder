package marioandweegee3.toolbuilder.common.itemgroups;

import java.util.Map;

import com.google.common.collect.Maps;

import marioandweegee3.toolbuilder.ToolBuilder;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class Groups{
    private static Map<String, Item[]> groupItems;

    private static int toolCount = 0;
    public static ItemGroup toolGroup;

    public static void makeGroupSets(){
        groupItems = Maps.newHashMap();
        groupItems.put("tools", new Item[200]);
    }

    public static void addTo(Item item, String group){
        int num;
        switch(group){
            case "tools":
                num = toolCount;
                toolCount++;
                break;
            default: return;
        }
        groupItems.get(group)[num] = item;
    }

    public static void init(){
        toolGroup = FabricItemGroupBuilder.create(ToolBuilder.makeID("tools"))
        .icon(()->new ItemStack(Items.IRON_PICKAXE))
        .appendItems(stacks ->{
            for(Item item : groupItems.get("tools")){
                stacks.add(new ItemStack(item));
            }
        })
        .build();
    }
}