package marioandweegee3.toolbuilder;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.swordglowsblue.artifice.api.Artifice;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import marioandweegee3.toolbuilder.api.ToolType;
import marioandweegee3.toolbuilder.api.effect.Effect;
import marioandweegee3.toolbuilder.api.effect.EffectInstance;
import marioandweegee3.toolbuilder.api.entry.TBInitializer;
import marioandweegee3.toolbuilder.api.item.BuiltArmorItem;
import marioandweegee3.toolbuilder.api.item.ModifierItem;
import marioandweegee3.toolbuilder.api.loot.BuiltToolLootCondition;
import marioandweegee3.toolbuilder.api.material.BowMaterial;
import marioandweegee3.toolbuilder.api.material.BuiltArmorMaterial;
import marioandweegee3.toolbuilder.api.material.BuiltToolMaterial;
import marioandweegee3.toolbuilder.api.material.HandleMaterial;
import marioandweegee3.toolbuilder.api.material.HeadMaterial;
import marioandweegee3.toolbuilder.api.material.StringMaterial;
import marioandweegee3.toolbuilder.api.registry.TBRegistries;
import marioandweegee3.toolbuilder.client.models.BowModel;
import marioandweegee3.toolbuilder.client.models.NewToolModel;
import marioandweegee3.toolbuilder.client.models.TBModels;
import marioandweegee3.toolbuilder.client.models.ToolModel;
import marioandweegee3.toolbuilder.common.blocks.BlockTorches;
import marioandweegee3.toolbuilder.common.blocks.Torch;
import marioandweegee3.toolbuilder.common.blocks.WallTorch;
import marioandweegee3.toolbuilder.common.blocks.gripping_station.GripStationBlock;
import marioandweegee3.toolbuilder.common.blocks.gripping_station.GripStationEntity;
import marioandweegee3.toolbuilder.common.blocks.mod_station.ModStationBlock;
import marioandweegee3.toolbuilder.common.blocks.mod_station.ModStationEntity;
import marioandweegee3.toolbuilder.common.command.TBEffectCommand;
import marioandweegee3.toolbuilder.common.config.ConfigHandler;
import marioandweegee3.toolbuilder.common.data.TBData;
import marioandweegee3.toolbuilder.common.data.loot_tables.BasicBlockLootTable;
import marioandweegee3.toolbuilder.common.data.recipes.ArmorRecipe;
import marioandweegee3.toolbuilder.common.data.recipes.BowRecipe;
import marioandweegee3.toolbuilder.common.data.recipes.ToolRecipe;
import marioandweegee3.toolbuilder.common.itemgroups.Groups;
import marioandweegee3.toolbuilder.common.items.Handles;
import marioandweegee3.toolbuilder.common.items.HolyWaterItem;
import marioandweegee3.toolbuilder.common.items.StringItems;
import marioandweegee3.toolbuilder.common.effect.Effects;
import marioandweegee3.toolbuilder.common.tools.HandleMaterials;
import marioandweegee3.toolbuilder.common.tools.HeadMaterials;
import marioandweegee3.toolbuilder.common.tools.tooltypes.Bow;
import marioandweegee3.toolbuilder.common.tools.tooltypes.ToolTypes;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.fabricmc.fabric.api.loot.v1.FabricLootPoolBuilder;
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback;
import net.fabricmc.fabric.api.registry.CommandRegistry;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.command.arguments.IdentifierArgumentType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;
import net.minecraft.item.WallStandingBlockItem;
import net.minecraft.loot.ConstantLootTableRange;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Style;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ToolBuilder implements ModInitializer {
    public static final String modID = "toolbuilder";

    public static final Logger logger = LogManager.getLogger("ToolBuilder");

    public static final Item obsidian_plate = new Item(new Item.Settings().group(ItemGroup.MATERIALS));

    public static final Style toolStyle = new Style().setColor(Formatting.GRAY);
    public static final Style effectStyle = new Style().setColor(Formatting.AQUA);
    public static final Style modifierStyle = new Style().setColor(Formatting.DARK_GREEN);

    public static final BlockEntityType<GripStationEntity> GRIPPING_STATION = BlockEntityType.Builder.create(GripStationEntity::new, GripStationBlock.BLOCK).build(null);
    public static final BlockEntityType<ModStationEntity> MOD_STATION = BlockEntityType.Builder.create(ModStationEntity::new, ModStationBlock.BLOCK).build(null);

    public static void debugDummy(){
        logger.info("DEBUG DUMMY METHOD CALLED - CHANGE CODE FOR RELEASE");
    }

    @Override
    public void onInitialize() {
        ConfigHandler.init();

        final boolean cottonResourcesLoaded = FabricLoader.getInstance().isModLoaded("cotton-resources");

        Groups.makeGroupSets();

        register(BlockTorches.stone_torch, BlockTorches.wall_stone_torch, "stone_torch", ItemGroup.DECORATIONS);

        TBData.blockLootTables.add(new BasicBlockLootTable(makeID("stone_torch")));

        register(GripStationBlock.BLOCK, "grip_station", ItemGroup.DECORATIONS);
        register(ModStationBlock.BLOCK, "mod_station", ItemGroup.DECORATIONS);

        TBModels.blockItems.add("grip_station");
        TBModels.blockItems.add("mod_station");

        Registry.register(Registry.BLOCK_ENTITY, makeID("gripping_station"), GRIPPING_STATION);
        Registry.register(Registry.BLOCK_ENTITY, makeID("mod_station"), MOD_STATION);

        TBData.blockLootTables.add(new BasicBlockLootTable(makeID("grip_station")));
        TBData.blockLootTables.add(new BasicBlockLootTable(makeID("mod_station")));

        register(StringItems.blazeString, "blaze_string");
        register(StringItems.enderString, "ender_string");

        TBModels.simpleItems.put("blaze_string", "string/blaze");
        TBModels.simpleItems.put("ender_string", "string/ender");

        register(obsidian_plate, "obsidian_plate");
        register(new Item(new Item.Settings().group(ItemGroup.MATERIALS)), "slime_crystal");

        TBModels.simpleItems.put("slime_crystal", "misc/slime_crystal");

        register(new Block(FabricBlockSettings.copy(Blocks.OBSIDIAN).strength(100, 2400).build()), "dense_obsidian", ItemGroup.BUILDING_BLOCKS);
        register(new Block(FabricBlockSettings.of(Material.GLASS).build()), "slime_crystal_block", ItemGroup.BUILDING_BLOCKS);

        TBModels.simpleBlocks.put("slime_crystal_block", "slime_crystal_block");
        TBModels.simpleBlocks.put("dense_obsidian", "dense_obsidian");
        TBData.blockLootTables.add(new BasicBlockLootTable(makeID("slime_crystal_block")));
        TBData.blockLootTables.add(new BasicBlockLootTable(makeID("dense_obsidian")));
        TBModels.blockItems.add("slime_crystal_block");
        TBModels.blockItems.add("dense_obsidian");

        register(new ModifierItem(Effects.POISONOUS), "poison_tip");
        register(new HolyWaterItem(), "holy_water");
        register(new ModifierItem(Effects.GROWING), "moss");
        register(new ModifierItem(Effects.FLAMING), "blazing_stone");
        register(new ModifierItem(Effects.DURABLE), "heavy_plate");

        TBModels.simpleItems.put("poison_tip", "modifier/poison");
        TBModels.simpleItems.put("holy_water", "modifier/holy");
        TBModels.simpleItems.put("moss", "modifier/growing");
        TBModels.simpleItems.put("blazing_stone", "modifier/flaming");
        TBModels.simpleItems.put("heavy_plate", "modifier/durable");

        register(Handles.wood_handle, "wood_handle");
        register(Handles.stone_handle, "stone_handle");
        register(Handles.gold_handle, "gold_handle");
        register(Handles.bone_handle, "bone_handle");
        register(Handles.diamond_handle, "diamond_handle");

        register(Handles.wood_gripped_handle, "wood_gripped_handle");
        register(Handles.stone_gripped_handle, "stone_gripped_handle");
        register(Handles.gold_gripped_handle, "gold_gripped_handle");
        register(Handles.bone_gripped_handle, "bone_gripped_handle");
        register(Handles.diamond_gripped_handle, "diamond_gripped_handle");

        register(new Item(new Item.Settings().group(ItemGroup.MISC)), "raw_heavy_plate");
        register(new Item(new Item.Settings().group(ItemGroup.MISC)), "ender_dust");

        TBModels.simpleItems.put("raw_heavy_plate", "misc/raw_heavy_plate");
        TBModels.simpleItems.put("ender_dust", "misc/ender_dust");

        if(ConfigHandler.INSTANCE.shouldAddNetherCobaltLootTable() && (cottonResourcesLoaded || ConfigHandler.INSTANCE.shouldIgnoreCottonResourcesExclusion())){
            TBData.blockLootTables.add(new BasicBlockLootTable(new Identifier("c:cobalt_nether_ore"), "blocks/"));
        }

        FabricLoader.getInstance().getEntrypoints("toolbuilder", TBInitializer.class).forEach(mod -> {
            for(HeadMaterial material : mod.headMaterials()){
                if(material.isCotton() && !(cottonResourcesLoaded || ConfigHandler.INSTANCE.shouldIgnoreCottonResourcesExclusion())){
                    continue;
                }
                TBRegistries.HEAD_MATERIALS.put(new Identifier(material.getMod(), material.getName()), material);
            }
            for(HandleMaterial material : mod.handleMaterials()){
                TBRegistries.HANDLE_MATERIALS.put(new Identifier(material.getMod(), material.getName()), material);
            }
            for(StringMaterial material : mod.stringMaterials()){
                TBRegistries.STRING_MATERIALS.put(new Identifier(material.getMod(), material.getName()), material);
            }
            for(BuiltArmorMaterial material : mod.armorMaterials()){
                if(material.isCotton() && !(cottonResourcesLoaded || ConfigHandler.INSTANCE.shouldIgnoreCottonResourcesExclusion())){
                    continue;
                }
                TBRegistries.ARMOR_MATERIALS.put(new Identifier(material.getMod(), material.getMaterialName()), material);
            }
            for(Effect effect : mod.effects()){
                TBRegistries.EFFECTS.put(effect.getID(), effect);
            }
        });

        for (ToolType toolType : ToolTypes.values()) {
            for (HandleMaterial handle : TBRegistries.HANDLE_MATERIALS.values()) {
                for (HeadMaterial head : TBRegistries.HEAD_MATERIALS.values()) {
                    makeToolItem(handle, head, toolType, true);
                    makeToolItem(handle, head, toolType, false);
                }
            }
        }

        for(BuiltArmorMaterial material : TBRegistries.ARMOR_MATERIALS.values()){
            makeArmorItems(material);
        }

        for(HandleMaterial material : TBRegistries.HANDLE_MATERIALS.values()){
            for(StringMaterial string : TBRegistries.STRING_MATERIALS.values()){
                makeBowItem(material, false, string);
                makeBowItem(material, true, string);
            }
        }

        LootTableLoadingCallback.EVENT.register((resourceManager, lootManager, id, supplier, setter) -> {
            if(id.equals(new Identifier("entities/wither_skeleton"))){
                FabricLootPoolBuilder poolBuilder = FabricLootPoolBuilder.builder()
                .withRolls(ConstantLootTableRange.create(1))
                .withEntry(ItemEntry.builder(Items.WITHER_SKELETON_SKULL))
                .withCondition(new BuiltToolLootCondition(ToolTypes.GREATSWORD.getName(), true));

                supplier.withPool(poolBuilder);
            }
        });

        Artifice.registerData("toolbuilder:recipes", pack -> {
            TBData.addRecipes(pack);

            if(ConfigHandler.INSTANCE.shouldAddSteelRecipe() && (cottonResourcesLoaded || ConfigHandler.INSTANCE.shouldIgnoreCottonResourcesExclusion())){
                pack.addBlastingRecipe(makeID("steel_ingot"), recipe -> {
                    recipe.ingredientTag(new Identifier("c:iron_plate"));
                    recipe.experience(2);
                    recipe.result(new Identifier("c:steel_ingot"));
                });
            }

            pack.addBlastingRecipe(makeID("slime_crystal_blasting"), recipe -> {
                recipe.ingredientItem(new Identifier("slime_block"));
                recipe.experience(1);
                recipe.result(makeID("slime_crystal"));
            });

            pack.addShapelessRecipe(makeID("slime_crystal_block"), recipe -> {
                for(int i = 0; i < 9; i++){
                    recipe.ingredientItem(makeID("slime_crystal"));
                }
                recipe.result(makeID("slime_crystal_block"), 1);
            });

            pack.addShapelessRecipe(makeID("slime_crystal_from_block"), recipe -> {
                recipe.ingredientItem(makeID("slime_crystal_block"));
                recipe.result(makeID("slime_crystal"), 9);
            });

            pack.addShapelessRecipe(makeID("holy_water"), recipe -> {
                recipe.ingredientItem(new Identifier("water_bucket"));
                recipe.ingredientItem(new Identifier("glass_bottle"));
                for(int i = 0; i < 3; i++){
                    if((cottonResourcesLoaded || ConfigHandler.INSTANCE.shouldIgnoreCottonResourcesExclusion())){
                        recipe.ingredientTag(new Identifier("c:silver_ingot"));
                    } else {
                        recipe.ingredientItem(new Identifier("iron_ingot"));
                    }
                }
                recipe.result(makeID("holy_water"), 1);
            });

            pack.addShapelessRecipe(makeID("poison_tip"), recipe -> {
                recipe.ingredientTag(new Identifier("logs"));
                recipe.ingredientItem(new Identifier("spider_eye"));
                recipe.result(makeID("poison_tip"), 1);
            });

            pack.addShapelessRecipe(makeID("moss"), recipe -> {
                for(int i = 0; i < 9; i++){
                    recipe.ingredientItem(new Identifier("vine"));
                }
                recipe.result(makeID("moss"), 1);
            });

            pack.addShapelessRecipe(makeID("blazing_stone"), recipe -> {
                recipe.ingredientItem(new Identifier("smooth_stone"));
                for(int i = 0; i < 6; i++){
                    recipe.ingredientItem(new Identifier("blaze_powder"));
                }
                recipe.result(makeID("blazing_stone"), 1);
            });

            pack.addShapelessRecipe(makeID("raw_heavy_plate"), recipe -> {
                if((cottonResourcesLoaded || ConfigHandler.INSTANCE.shouldIgnoreCottonResourcesExclusion())){
                    recipe.ingredientTag(new Identifier("c:lead_plate"));
                    recipe.ingredientTag(new Identifier("c:tungsten_plate"));
                } else {
                    recipe.ingredientItem(makeID("obsidian_plate"));
                    recipe.ingredientItem(makeID("obsidian_plate"));
                }
                recipe.ingredientItem(new Identifier("slime_ball"));
                recipe.ingredientItem(makeID("obsidian_plate"));
                recipe.result(makeID("raw_heavy_plate"), 1);
            });

            pack.addBlastingRecipe(makeID("heavy_plate"), recipe -> {
                recipe.cookingTime(200);
                recipe.ingredientItem(makeID("raw_heavy_plate"));
                recipe.experience(1);
                recipe.result(makeID("heavy_plate"));
            });

            pack.addShapedRecipe(makeID("obsidian_plate"), recipe -> {
                recipe.pattern(
                    "xx",
                    "xx"
                );
                recipe.ingredientItem('x', new Identifier("obsidian"));
                recipe.result(makeID("obsidian_plate"), 1);
            });

            pack.addShapedRecipe(makeID("blaze_string"), recipe -> {
                recipe.pattern(
                    "bb",
                    "ss"
                );
                recipe.ingredientItem('b', new Identifier("blaze_powder"));
                recipe.ingredientItem('s', new Identifier("string"));
                recipe.result(makeID("blaze_string"), 2);
            });

            pack.addBlastingRecipe(makeID("ender_dust"), recipe -> {
                recipe.ingredientItem(new Identifier("ender_pearl"));
                recipe.experience(3);
                recipe.result(makeID("ender_dust"));
            });

            pack.addShapedRecipe(makeID("ender_string"), recipe -> {
                recipe.pattern(
                    "ee",
                    "ss"
                );
                recipe.ingredientItem('e', makeID("ender_dust"));
                recipe.ingredientItem('s', new Identifier("string"));
                recipe.result(makeID("ender_string"), 2);
            });
        });

        CommandRegistry.INSTANCE.register(false, dispatcher -> {
            LiteralCommandNode<ServerCommandSource> toolbuilderNode = CommandManager
            .literal("toolbuilder")
            .build();

            LiteralCommandNode<ServerCommandSource> tbNode = CommandManager
            .literal("tb")
            .build();

            LiteralCommandNode<ServerCommandSource> effectsNode = CommandManager
            .literal("effects")
            .build();

            LiteralCommandNode<ServerCommandSource> effectGetNode = CommandManager
            .literal("get")
            .executes(TBEffectCommand::get)
            .build();

            LiteralCommandNode<ServerCommandSource> effectSetNameNode = CommandManager
            .literal("set")
            .requires(source -> source.hasPermissionLevel(3))
            .build();

            LiteralCommandNode<ServerCommandSource> effectClearNode = CommandManager
            .literal("clear")
            .executes(TBEffectCommand::clear)
            .requires(source -> source.hasPermissionLevel(3))
            .build();

            ArgumentCommandNode<ServerCommandSource, Identifier> effectSetNode = CommandManager
            .argument("effect", IdentifierArgumentType.identifier())
            .suggests(Effects.effectSuggestions())
            .executes(TBEffectCommand::set)
            .requires(source -> source.hasPermissionLevel(3))
            .build();

            dispatcher.getRoot().addChild(toolbuilderNode);
            dispatcher.getRoot().addChild(tbNode);
            tbNode.addChild(effectsNode);
            toolbuilderNode.addChild(effectsNode);
            effectsNode.addChild(effectGetNode);
            effectsNode.addChild(effectSetNameNode);
            effectsNode.addChild(effectClearNode);
            effectSetNameNode.addChild(effectSetNode);
        });

        Groups.init();

        int effectCount = 0;
        logger.info("Registered these effects: ");

        for(Map.Entry<Identifier, Effect> entry : TBRegistries.EFFECTS.entrySet()){
            effectCount++;
            logger.info(entry.getKey());
        }

        logger.info("Registered "+effectCount+" effects.");
    }

    public static void makeToolItem(HandleMaterial handle, HeadMaterial head, ToolType toolType, Boolean grip) {
        ToolItemBuilder builder = new ToolItemBuilder(handle, head, toolType, grip);
        Item tool = builder.build();
        List<ToolType> newModels = Arrays.asList(
            ToolTypes.SWORD,
            ToolTypes.HAMMER,
            ToolTypes.GREATSWORD
        );
        if(newModels.contains(toolType)){
            TBModels.toolModels.add(new NewToolModel(builder));
        } else {
            TBModels.toolModels.add(new ToolModel(builder));
        }
        TBData.toolRecipes.add(new ToolRecipe(builder));
        register(tool, builder.name, "tools");
        if(head == HeadMaterials.WOOD && handle == HandleMaterials.WOOD && !grip){
            FuelRegistry.INSTANCE.add(tool, 200);
        }
    }

    public static void makeBowItem(HandleMaterial material, Boolean grip, StringMaterial string) {
        BowBuilder builder = new BowBuilder(material, grip, string);
        Item bow = builder.build();
        TBModels.bowModels.add(new BowModel(builder));
        TBData.bowRecipes.add(new BowRecipe(builder));
        
        register(bow, builder.name, "tools");
        if(material == HandleMaterials.WOOD){
            int burnTime = 200;
            if(EffectInstance.toEffectSet(builder.material.getEffects()).contains(Effects.FLAMING)) burnTime += 100;
            FuelRegistry.INSTANCE.add(bow, burnTime);
        }
    }

    public static void makeArmorItems(BuiltArmorMaterial material){
        ArmorBuilder builder = new ArmorBuilder(material);
        builder.register();
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

    public static void register(Block block, String name, ItemGroup group){
        BlockItem item = new BlockItem(block, new Item.Settings().group(group));
        Registry.register(Registry.ITEM, makeID(name), item);
        Registry.register(Registry.BLOCK, makeID(name), block);
    }

    public static void register(Torch block, WallTorch block2, String name, ItemGroup group){
        WallStandingBlockItem item = new WallStandingBlockItem(block, block2, new Item.Settings().group(group));
        Registry.register(Registry.ITEM, makeID(name), item);
        Registry.register(Registry.BLOCK, makeID(name), block);
        Registry.register(Registry.BLOCK, makeID("wall_"+name), block2);
    }

    public static class BowBuilder{
        private BowMaterial material;

        public String name;

        public BowBuilder(HandleMaterial material2, Boolean grip, StringMaterial string) {
            this.material = new BowMaterial(material2, grip, string);

            this.name = makeName(material);
        }

        public BowMaterial getMaterial(){
            return material;
        }

        public Item build(){
            return Bow.create(material);
        }

        private String makeName(BowMaterial material2) {
            String name = material2.getName() + "_bow";
            if(material.grip) name += "_gripped";
            return name;
        }
    }

    public static class ArmorBuilder{
        public BuiltArmorMaterial armorMaterial;

        public ArmorBuilder(BuiltArmorMaterial material){
            armorMaterial = material;
        }

        public String makeName(EquipmentSlot slot){
            return armorMaterial.getMaterialName()+"_"+getTypeString(slot);
        }

        public String getTypeString(EquipmentSlot slot){
            String typeString = "";
            switch(slot){
                case FEET: typeString = "boots"; break;
                case LEGS: typeString = "leggings"; break;
                case CHEST: typeString = "chestplate"; break;
                case HEAD: typeString = "helmet"; break;
                case MAINHAND: break;
                case OFFHAND: break;
            }
            return typeString;
        }

        public void register(){
            register(EquipmentSlot.FEET);
            register(EquipmentSlot.LEGS);
            register(EquipmentSlot.CHEST);
            register(EquipmentSlot.HEAD);
        }

        private void register(EquipmentSlot slot){
            TBData.armorRecipes.add(new ArmorRecipe(this, slot));
            TBModels.simpleItems.put(makeName(slot), "armor/"+armorMaterial.getMaterialName()+"/"+getTypeString(slot));
            ToolBuilder.register(build(slot), makeName(slot), "armor");
        }

        public Item build(EquipmentSlot slot){
            return new BuiltArmorItem(armorMaterial, slot, new Item.Settings());
        }
    }
    
    public static class ToolItemBuilder{
        private BuiltToolMaterial material;
        private ToolType toolType;

        public String name;

        public ToolItemBuilder(HandleMaterial handle, HeadMaterial head, ToolType toolType2, Boolean grip) {
            this.toolType = toolType2;
            this.name = makeName(head, handle, grip);
            this.material = BuiltToolMaterial.of(handle, head, name, grip);
        }

        public Item build(){
            Item tool = toolType.getBuilder().build(material);
            return tool;
        }

        public BuiltToolMaterial getMaterial(){
            return material;
        }

        public ToolType getType(){
            return toolType;
        }

        private String makeName(HeadMaterial head, HandleMaterial handle) {
            String nameString = toolType.getName();
            nameString += "_" + head.getName();
            nameString += "_" + handle.getName();
            return nameString;
        }

        private String makeName(HeadMaterial head, HandleMaterial handle, Boolean grip) {
            if (grip)
                return makeName(head, handle) + "_gripped";
            else
                return makeName(head, handle);
        }
    }
}