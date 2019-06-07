package marioandweegee3.toolbuilder;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import marioandweegee3.toolbuilder.common.Handles;
import marioandweegee3.toolbuilder.common.itemgroups.Groups;
import marioandweegee3.toolbuilder.common.tools.HandleMaterials;
import marioandweegee3.toolbuilder.common.tools.HeadMaterials;
import marioandweegee3.toolbuilder.common.tools.MaterialBuilder;
import marioandweegee3.toolbuilder.common.tools.MaterialBuilder.Material;
import marioandweegee3.toolbuilder.common.tools.tooltypes.*;
import net.fabricmc.api.ModInitializer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ToolBuilder implements ModInitializer {
    public static final String modID = "toolbuilder";

    public static final Logger logger = LogManager.getLogger("ToolBuilder");

    @Override
    public void onInitialize() {
        Groups.makeGroupSets();

        register(Handles.wood_handle, "wood_handle");
        register(Handles.stone_handle, "stone_handle");
        register(Handles.gold_handle, "gold_handle");

        for (ToolValues toolType : ToolValues.values()) {
            for (HandleMaterials handle : HandleMaterials.values()) {
                for (HeadMaterials head : HeadMaterials.values()) {
                    makeToolItem(handle, head, toolType);
                }
            }
        }

        Groups.init();
    }

    public static void makeToolItem(HandleMaterials handle, HeadMaterials head, ToolValues toolType) {
        Builder builder = new Builder(handle, head, toolType);
        Item tool = builder.build();
        register(tool, builder.name, "tools");
    }

    public static Identifier makeID(String name){
        return new Identifier(modID, name);
    }

    public static Item makeItem(){
        return new Item(new Item.Settings());
    }

    public static void register(Item item, String name, ItemGroup group){
        item = new Item(new Item.Settings().group(group));
        Registry.register(Registry.ITEM, makeID(name), item);
    }

    public static void register(Item item, String name){
        Registry.register(Registry.ITEM, makeID(name), item);
    }

    public static void register(Item item, String name, String group){
        Registry.register(Registry.ITEM, makeID(name), item);
        Groups.addTo(item, group);
    }

    
    
    public static class Builder{
        private Material material;
        private ToolValues toolType;

        public String name;

        public Builder(HandleMaterials handle, HeadMaterials head, ToolValues toolType){
            this.toolType = toolType;
            this.name = makeName(head, handle);
            this.material = MaterialBuilder.build(handle, head, this.name);
        }

        public Item build(){
            switch (this.toolType){
                case AXE: return Axe.create(material, name);
                case PICKAXE: return Pickaxe.create(material);
                case HAMMER: return Hammer.create(material);
                case HOE: return Hoe.create(material);
                case SHOVEL: return Shovel.create(material);
                case SWORD: return Sword.create(material);
                case KNIFE: return Knife.create(material);
                default: return null;
            }
        }

        public DefaultedList<Item> getDefaultInputs(){
            DefaultedList<Item> inputs = DefaultedList.create();
            Item handleItem = material.handle.handle;
            Item headItem = material.head.getRepairItem();
            switch (this.toolType){
                case AXE:
                    inputs.add(headItem); inputs.add(headItem);
                    inputs.add(handleItem); inputs.add(headItem);
                    inputs.add(handleItem); inputs.add(Items.AIR);
                    break;
                case PICKAXE:
                    inputs.add(headItem); inputs.add(headItem); inputs.add(headItem);
                    inputs.add(Items.AIR); inputs.add(handleItem); inputs.add(Items.AIR);
                    inputs.add(Items.AIR); inputs.add(handleItem); inputs.add(Items.AIR);
                    break;
                case HAMMER:
                    inputs.add(headItem); inputs.add(headItem); inputs.add(headItem);
                    inputs.add(headItem); inputs.add(handleItem); inputs.add(headItem);
                    inputs.add(Items.AIR); inputs.add(handleItem); inputs.add(Items.AIR);
                    break;
                case HOE:
                    inputs.add(headItem); inputs.add(headItem);
                    inputs.add(handleItem); inputs.add(Items.AIR);
                    inputs.add(handleItem); inputs.add(Items.AIR);
                    break;
                case SHOVEL:
                    inputs.add(headItem);
                    inputs.add(handleItem);
                    inputs.add(handleItem);
                    break;
                case SWORD:
                    inputs.add(headItem);
                    inputs.add(headItem);
                    inputs.add(handleItem);
                    break;
                case KNIFE:
                    inputs.add(headItem);
                    inputs.add(handleItem);
                    break;
                default: break;
            }
            return inputs;
        }

        private String makeName(HeadMaterials head, HandleMaterials handle){
            String nameString = toolType.getName();
            ArrayList<HeadMaterials> materials = new ArrayList<>();
            for(HeadMaterials material : HeadMaterials.values()){
                materials.add(material);
            }
            int materialNum = materials.indexOf(head);
            nameString += "_" + MaterialBuilder.materialNames[materialNum];
            nameString += "_" + handle.getName();
            return nameString;
        }
    }
}