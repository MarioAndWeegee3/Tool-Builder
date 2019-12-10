package marioandweegee3.toolbuilder.common.command;

import java.util.Set;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import marioandweegee3.toolbuilder.api.BuiltTool;
import marioandweegee3.toolbuilder.api.effect.Effect;
import marioandweegee3.toolbuilder.api.item.BuiltArmorItem;
import marioandweegee3.toolbuilder.api.registry.TBRegistries;
import marioandweegee3.toolbuilder.common.effect.Effects;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;

public class TBEffectCommand {
    public static int get(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().getPlayer();
        ItemStack stack = player.getStackInHand(Hand.MAIN_HAND);

        if(stack.getItem() instanceof BuiltTool){
            BuiltTool tool = (BuiltTool) stack.getItem();
            context.getSource().sendFeedback(new TranslatableText("text.toolbuilder.commands.effect.get.effectList"), false);

            for(Effect effect : tool.getEffects(stack)){
                context.getSource().sendFeedback(effect.getTranslationName(), false);
            }
        }

        if(stack.getItem() instanceof BuiltArmorItem){
            BuiltArmorItem armor = (BuiltArmorItem) stack.getItem();
            context.getSource().sendFeedback(new TranslatableText("text.toolbuilder.commands.effect.get.effectList"), false);

            for(Effect effect : armor.getEffects(stack)){
                context.getSource().sendFeedback(effect.getTranslationName(), false);
            }
        }
        return 1;
    }

    public static int set(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().getPlayer();
        ItemStack stack = player.getStackInHand(Hand.MAIN_HAND);

        String effectString = StringArgumentType.getString(context, "effect");

        if(stack.getItem() instanceof BuiltTool){
            BuiltTool tool = (BuiltTool) stack.getItem();

            Effect effect = TBRegistries.EFFECTS.get(new Identifier(effectString));
            if(effect == null){
                context.getSource().sendFeedback(new TranslatableText("text.toolbuilder.commands.effect.set.invalid").append(effectString), false);
                return 0;
            }

            if(!tool.getEffects(stack).contains(effect)){
                CompoundTag toolTag = stack.getOrCreateTag();
                ListTag effectsTag = new ListTag();
                if(toolTag.containsKey(Effects.effectNBTtag)){
                    effectsTag = toolTag.getList(Effects.effectNBTtag, 8);
                }
                effectsTag.add(new StringTag(effect.getID().toString()));
                toolTag.put(Effects.effectNBTtag, effectsTag);
                context.getSource().sendFeedback(new TranslatableText("text.toolbuilder.commands.effect.set.applied").append(effectString), false);
            } else {
                context.getSource().sendFeedback(new TranslatableText("text.toolbuilder.commands.effect.set.alreadyApplied").append(effectString), false);
            }
        }

        if(stack.getItem() instanceof BuiltArmorItem){
            BuiltArmorItem armor = (BuiltArmorItem) stack.getItem();

            Effect effect = TBRegistries.EFFECTS.get(new Identifier(effectString));
            if(effect == null){
                context.getSource().sendFeedback(new TranslatableText("text.toolbuilder.commands.effect.set.invalid").append(effectString), false);
                return 0;
            }

            if(!armor.getEffects(stack).contains(effect)){
                CompoundTag tag = stack.getOrCreateTag();
                ListTag effectsTag = new ListTag();
                if(tag.containsKey(Effects.effectNBTtag)){
                    effectsTag = tag.getList(Effects.effectNBTtag, 8);
                }
                effectsTag.add(new StringTag(effect.getID().toString()));
                tag.put(Effects.effectNBTtag, effectsTag);
                context.getSource().sendFeedback(new TranslatableText("text.toolbuilder.commands.effect.set.applied").append(effectString), false);
            } else {
                context.getSource().sendFeedback(new TranslatableText("text.toolbuilder.commands.effect.set.alreadyApplied").append(effectString), false);
            }
        }

        return 1;
    }

    public static int clear(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().getPlayer();
        ItemStack stack = player.getStackInHand(Hand.MAIN_HAND);

        if(stack.getItem() instanceof BuiltTool){
            BuiltTool tool = (BuiltTool) stack.getItem();
            Set<Effect> modifiers = tool.getModifierEffects(stack);
            
            tool.removeEffects(stack, modifiers);
            context.getSource().sendFeedback(new TranslatableText("text.toolbuilder.commands.effect.clear.removed"), false);
            return 1;
        }

        context.getSource().sendFeedback(new TranslatableText("text.toolbuilder.commands.effect.clear.invalid").append(stack.toString()), false);
        return 0;
    }
}