package marioandweegee3.toolbuilder.common.itemgroups;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;

import marioandweegee3.toolbuilder.ToolBuilder;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.registry.Registry;

public class Groups{
    private static Map<String, List<Item>> groupItems;

    private static int toolCount = 0;
    public static ItemGroup toolGroup;

    private static int armorCount = 0;
    public static ItemGroup armorGroup;

    public static void makeGroupSets(){
        groupItems = Maps.newHashMap();
        groupItems.put("tools", new ArrayList<>(0));
        groupItems.put("armor", new ArrayList<>(0));
    }

    public static void addTo(Item item, String group){
        switch(group){
            case "tools":
                toolCount++;
                break;
            case "armor":
                armorCount++;
                break;
            default: return;
        }
        groupItems.get(group).add(item);
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
        ToolBuilder.logger.info("Registered "+toolCount+" tools!");

        armorGroup = FabricItemGroupBuilder.create(ToolBuilder.makeID("armor"))
        .icon(()->new ItemStack(Registry.ITEM.get(ToolBuilder.makeID("copper_chestplate"))))
        .appendItems(stacks ->{
            for(Item item : groupItems.get("armor")){
                stacks.add(new ItemStack(item));
            }
        })
        .build();
        ToolBuilder.logger.info("Registered "+armorCount+" armor!");
    }
}