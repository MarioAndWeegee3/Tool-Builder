package marioandweegee3.toolbuilder.common.command;

import java.util.Locale;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;

import marioandweegee3.toolbuilder.ToolBuilder;
import marioandweegee3.toolbuilder.api.ToolType;
import marioandweegee3.toolbuilder.api.registry.TBRegistries;
import marioandweegee3.toolbuilder.common.tools.tooltypes.ToolTypes;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import net.minecraft.util.Lazy;
import net.minecraft.util.registry.Registry;

public class TBItemCommand {
    public static SuggestionProvider<ServerCommandSource> toolTypeSuggestions() {
        return (ctx, builder) -> {
            String remaining = builder.getRemaining().toLowerCase(Locale.ROOT);

            for (ToolType tool : ToolTypes.values()) {
                String name = tool.getName();
                if (name.toLowerCase(Locale.ROOT).startsWith(remaining)) {
                    builder.suggest(name);
                }
            }

            return builder.buildFuture();
        };
    }

    public static SuggestionProvider<ServerCommandSource> headMaterialSuggestions() {
        return (ctx, builder) -> {
            String remaining = builder.getRemaining().toLowerCase(Locale.ROOT);

            for (Identifier head : TBRegistries.HEAD_MATERIALS.keySet()) {
                String name = head.toString();
                if (name.toLowerCase(Locale.ROOT).startsWith(remaining)) {
                    builder.suggest(name);
                }
            }

            return builder.buildFuture();
        };
    }

    public static SuggestionProvider<ServerCommandSource> handleMaterialSuggestions() {
        return (ctx, builder) -> {
            String remaining = builder.getRemaining().toLowerCase(Locale.ROOT);

            for (Identifier handle : TBRegistries.HANDLE_MATERIALS.keySet()) {
                String name = handle.toString();
                if (name.toLowerCase(Locale.ROOT).startsWith(remaining)) {
                    builder.suggest(name);
                }
            }

            return builder.buildFuture();
        };
    }

    public static SuggestionProvider<ServerCommandSource> armorMaterialSuggestions() {
        return (ctx, builder) -> {
            String remaining = builder.getRemaining().toLowerCase(Locale.ROOT);

            for (Identifier armor : TBRegistries.ARMOR_MATERIALS.keySet()) {
                String name = armor.toString();
                if (name.toLowerCase(Locale.ROOT).startsWith(remaining)) {
                    builder.suggest(name);
                }
            }

            return builder.buildFuture();
        };
    }

    public static SuggestionProvider<ServerCommandSource> armorSlotSuggestions() {
        return (ctx, builder) -> {
            String remaining = builder.getRemaining().toLowerCase(Locale.ROOT);

            for (EquipmentSlot slot : EquipmentSlot.values()) {
                if (slot == EquipmentSlot.MAINHAND || slot == EquipmentSlot.OFFHAND)
                    continue;
                String name = ToolBuilder.ArmorBuilder.getTypeString(slot);
                if (name.toLowerCase(Locale.ROOT).startsWith(remaining)) {
                    builder.suggest(name);
                }
            }

            return builder.buildFuture();
        };
    }

    public static int giveTool(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        PlayerEntity player = context.getSource().getPlayer();

        String type = context.getArgument("tool", Identifier.class).getPath();

        Identifier head = context.getArgument("head", Identifier.class);

        Identifier handle = context.getArgument("handle", Identifier.class);

        Boolean grip = new Lazy<Boolean>(() -> {
            try {
                Boolean b = context.getArgument("gripped", Boolean.class);
                return b;
            } catch (IllegalArgumentException e) {
                return false;
            }
        }).get();

        Item tool = Registry.ITEM.get(
                ToolBuilder.makeID(type + "_" + head.getPath() + "_" + handle.getPath() + (grip ? "_gripped" : "")));

        if (tool != Items.AIR && player != null) {
            player.giveItemStack(new ItemStack(tool));
            context.getSource().sendFeedback(new LiteralText("Given player "+context.getSource().getName()+" "+Registry.ITEM.getId(tool)), false);
        }

        return 1;
    }

    public static int giveArmor(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        PlayerEntity player = context.getSource().getPlayer();

        Identifier material = context.getArgument("armor", Identifier.class);

        EquipmentSlot slot = context.getArgument("slot", EquipmentSlot.class);

        Item armor = Registry.ITEM.get(ToolBuilder.makeID(new ToolBuilder.ArmorBuilder(TBRegistries.ARMOR_MATERIALS.get(material)).makeName(slot)));

        if (armor != Items.AIR && player != null) {
            player.giveItemStack(new ItemStack(armor));
            context.getSource().sendFeedback(new LiteralText("Given player "+context.getSource().getName()+" "+Registry.ITEM.getId(armor)), false);
        }

        return 1;
    }
}