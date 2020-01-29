package marioandweegee3.toolbuilder.client;

import com.swordglowsblue.artifice.api.Artifice;

import marioandweegee3.toolbuilder.ToolBuilder;
import marioandweegee3.toolbuilder.ToolBuilder.ArmorBuilder;
import marioandweegee3.toolbuilder.ToolBuilder.BowBuilder;
import marioandweegee3.toolbuilder.ToolBuilder.ToolItemBuilder;
import marioandweegee3.toolbuilder.api.ToolType;
import marioandweegee3.toolbuilder.api.material.HandleMaterial;
import marioandweegee3.toolbuilder.api.material.HeadMaterial;
import marioandweegee3.toolbuilder.api.material.StringMaterial;
import marioandweegee3.toolbuilder.client.lang.TBLang;
import marioandweegee3.toolbuilder.client.models.BowModel;
import marioandweegee3.toolbuilder.client.models.NewToolModel;
import marioandweegee3.toolbuilder.client.models.TBModels;
import marioandweegee3.toolbuilder.client.models.ToolModel;
import marioandweegee3.toolbuilder.common.blocks.BlockTorches;
import marioandweegee3.toolbuilder.common.config.ConfigHandler;
import marioandweegee3.toolbuilder.common.tools.HandleMaterials;
import marioandweegee3.toolbuilder.common.tools.StringMaterials;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.Identifier;

public class ToolBuilderClient implements ClientModInitializer{

    @Override
    public void onInitializeClient() {
        TBModels.blockItems.add("stone_torch");

        TBModels.blockItems.add("grip_station");
        TBModels.blockItems.add("mod_station");

        TBModels.simpleItems.put("blaze_string", "string/blaze");
        TBModels.simpleItems.put("ender_string", "string/ender");

        TBModels.simpleItems.put("obsidian_plate", "plate/obsidian");

        TBModels.simpleItems.put("slime_crystal", "misc/slime_crystal");

        TBModels.simpleBlocks.put("slime_crystal_block", "slime_crystal_block");
        TBModels.simpleBlocks.put("dense_obsidian", "dense_obsidian");

        TBModels.blockItems.add("slime_crystal_block");
        TBModels.blockItems.add("dense_obsidian");

        TBModels.simpleItems.put("poison_tip", "modifier/poison");
        TBModels.simpleItems.put("holy_water", "modifier/holy");
        TBModels.simpleItems.put("moss", "modifier/growing");
        TBModels.simpleItems.put("blazing_stone", "modifier/flaming");
        TBModels.simpleItems.put("heavy_plate", "modifier/durable");
        TBModels.simpleItems.put("magnet", "modifier/magnetic");

        for(HandleMaterial handle : HandleMaterials.values()){
            TBModels.simpleItems.put(handle.getName()+"_handle", "handle/"+handle.getName()+"/full");
            TBModels.customItems.put(handle.getName()+"_gripped_handle", model -> {
                model.parent(new Identifier("item/generated"));
                model.texture("layer0", ToolBuilder.makeID("item/handle/"+handle.getName()+"/full"));
                model.texture("layer1", ToolBuilder.makeID("item/handle/grip/full"));
            });
            TBLang.addHandle(handle);
        }

        for(StringMaterial string : StringMaterials.values()){
            TBLang.addStringMaterial(string);
        }

        String[] miscItems = new String[]{
            "raw_heavy_plate",
            "ender_dust"
        };

        for(String item : miscItems){
            TBModels.simpleItems.put(item, "misc/"+item);
        }

        Artifice.registerAssets("toolbuilder:items", pack ->{
            TBModels.addModels(pack);
            TBLang.add(pack);
        });

        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), BlockTorches.stone_torch, BlockTorches.wall_stone_torch);
    }

    public static void addToolModel(HandleMaterial handle, HeadMaterial head, ToolType toolType, Boolean grip) {
        ToolItemBuilder builder = new ToolItemBuilder(handle, head, toolType, grip);
        if(ConfigHandler.INSTANCE.shouldUseNewModels()){
            TBModels.toolModels.add(new NewToolModel(builder));
        } else {
            TBModels.toolModels.add(new ToolModel(builder));
        }
    }

    public static void addBowModel(HandleMaterial material, Boolean grip, StringMaterial string) {
        BowBuilder builder = new BowBuilder(material, grip, string);
        TBModels.bowModels.add(new BowModel(builder));
    }

    public static void addArmorModel(ArmorBuilder builder, EquipmentSlot slot){
        TBModels.simpleItems.put(builder.makeName(slot), "armor/"+builder.armorMaterial.getMaterialName()+"/"+builder.getTypeString(slot));
    }
}