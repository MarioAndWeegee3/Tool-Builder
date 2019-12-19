package marioandweegee3.toolbuilder.common.command;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import marioandweegee3.toolbuilder.api.BuiltTool;
import marioandweegee3.toolbuilder.api.effect.Effect;
import marioandweegee3.toolbuilder.api.effect.EffectInstance;
import marioandweegee3.toolbuilder.api.item.BuiltArmorItem;
import marioandweegee3.toolbuilder.api.registry.TBRegistries;
import marioandweegee3.toolbuilder.common.effect.Effects;
import net.minecraft.command.arguments.IdentifierArgumentType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
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

            for(EffectInstance effect : tool.getEffects(stack)){
                context.getSource().sendFeedback(effect.getTooltip(), false);
            }
        }

        if(stack.getItem() instanceof BuiltArmorItem){
            BuiltArmorItem armor = (BuiltArmorItem) stack.getItem();
            context.getSource().sendFeedback(new TranslatableText("text.toolbuilder.commands.effect.get.effectList"), false);

            for(EffectInstance effect : armor.getEffects(stack)){
                context.getSource().sendFeedback(effect.getTooltip(), false);
            }
        }
        return 1;
    }

    public static int set(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().getPlayer();
        ItemStack stack = player.getStackInHand(Hand.MAIN_HAND);

        Identifier effectId = IdentifierArgumentType.getIdentifier(context, "effect");

        if(stack.getItem() instanceof BuiltTool){
            BuiltTool tool = (BuiltTool) stack.getItem();

            Effect effect = TBRegistries.EFFECTS.get(effectId);
            if(effect == null){
                context.getSource().sendFeedback(new TranslatableText("text.toolbuilder.commands.effect.set.invalid").append(effectId.toString()), false);
                return 0;
            }

            Set<EffectInstance> effects = EffectInstance.mergeSets(tool.getEffects(stack), new HashSet<>(Arrays.asList(new EffectInstance(effect, 1))));

            if(!tool.getEffects(stack).equals(effects)){
                CompoundTag toolTag = stack.getOrCreateTag();
                ListTag effectListTag = new ListTag();
                for(EffectInstance instance : tool.getModifierEffects(stack)){
                    effectListTag.add(instance.toTag());
                }
                effectListTag.add(new EffectInstance(effect, 1).toTag());
                toolTag.put(Effects.effectNBTtag, effectListTag);
                context.getSource().sendFeedback(new TranslatableText("text.toolbuilder.commands.effect.set.applied").append(effectId.toString()), false);
            } else {
                context.getSource().sendFeedback(new TranslatableText("text.toolbuilder.commands.effect.set.alreadyApplied").append(effectId.toString()), false);
            }
        }

        if(stack.getItem() instanceof BuiltArmorItem){
            BuiltArmorItem armor = (BuiltArmorItem) stack.getItem();

            Effect effect = TBRegistries.EFFECTS.get(effectId);
            if(effect == null){
                context.getSource().sendFeedback(new TranslatableText("text.toolbuilder.commands.effect.set.invalid").append(effectId.toString()), false);
                return 0;
            }

            Set<EffectInstance> effects = EffectInstance.mergeSets(armor.getEffects(stack), new HashSet<>(Arrays.asList(new EffectInstance(effect, 1))));

            if(!armor.getEffects(stack).equals(effects)){
                CompoundTag armorTag = stack.getOrCreateTag();
                ListTag effectListTag = new ListTag();
                for(EffectInstance instance : effects){
                    effectListTag.add(instance.toTag());
                }
                armorTag.put(Effects.effectNBTtag, effectListTag);
                context.getSource().sendFeedback(new TranslatableText("text.toolbuilder.commands.effect.set.applied").append(effectId.toString()), false);
            } else {
                context.getSource().sendFeedback(new TranslatableText("text.toolbuilder.commands.effect.set.alreadyApplied").append(effectId.toString()), false);
            }
        }

        return 1;
    }

    public static int clear(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().getPlayer();
        ItemStack stack = player.getStackInHand(Hand.MAIN_HAND);

        if(stack.getItem() instanceof BuiltTool){
            BuiltTool tool = (BuiltTool) stack.getItem();
            Set<EffectInstance> modifiers = tool.getModifierEffects(stack);
            
            tool.removeEffects(stack, modifiers);
            context.getSource().sendFeedback(new TranslatableText("text.toolbuilder.commands.effect.clear.removed"), false);
            return 1;
        }

        context.getSource().sendFeedback(new TranslatableText("text.toolbuilder.commands.effect.clear.invalid").append(stack.toString()), false);
        return 0;
    }
}