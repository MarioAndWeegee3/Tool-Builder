package marioandweegee3.toolbuilder.mixins;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import marioandweegee3.toolbuilder.ToolBuilder;
import marioandweegee3.toolbuilder.api.BuiltTool;
import marioandweegee3.toolbuilder.api.effect.Effect;
import marioandweegee3.toolbuilder.api.effect.EffectInstance;
import marioandweegee3.toolbuilder.api.item.BuiltArmorItem;
import marioandweegee3.toolbuilder.api.item.ModifierItem;
import marioandweegee3.toolbuilder.common.effect.Effects;
import marioandweegee3.toolbuilder.common.tools.tooltypes.ToolTypes;
import net.minecraft.container.AnvilContainer;
import net.minecraft.container.Container;
import net.minecraft.container.ContainerType;
import net.minecraft.container.Property;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

@Mixin(AnvilContainer.class)
public abstract class AnvilModifyMixin extends Container {
    @Shadow
    private Inventory result;

    @Shadow
    private Inventory inventory;

    @Shadow
    private int repairItemUsage;

    @Shadow
    private Property levelCost;

    protected AnvilModifyMixin(ContainerType<?> type, int syncId) {
        super(type, syncId);
    }

    @Inject(method = "updateResult", at = @At("RETURN"))
    public void onResultUpdate(CallbackInfo ci){
        ItemStack stack = this.inventory.getInvStack(0).copy();
        ItemStack modStack = this.inventory.getInvStack(1).copy();
        ItemStack resultStack = this.result.getInvStack(0).copy();
        if(stack.getItem() instanceof BuiltTool){
            BuiltTool tool = (BuiltTool) stack.getItem();
            if(!modStack.isEmpty() && resultStack.isEmpty()){
                if(modStack.getItem() instanceof ModifierItem){
                    ModifierItem modifier = (ModifierItem) modStack.getItem();

                    Effect effect = modifier.getEffect();
                    
                    if(effect == null){
                        return;
                    }
        
                    Set<EffectInstance> effects = EffectInstance.mergeSets(tool.getEffects(stack), new HashSet<>(Arrays.asList(new EffectInstance(effect, 1))));
        
                    if(!tool.getEffects(stack).equals(effects)){
                        CompoundTag toolTag = stack.getOrCreateTag();

                        int numModifiers = Optional.of(toolTag.getInt("num_modifiers")).orElse(0);

                        int effectLevel = EffectInstance.getLevel(effects, effect);

                        if(numModifiers >= tool.getNumModifiers(stack) && effectLevel >= effect.getMaxLevel()){
                            return;
                        }

                        numModifiers++;
                        toolTag.putInt("num_modifiers", numModifiers);

                        ListTag effectListTag = new ListTag();
                        for(EffectInstance instance : tool.getModifierEffects(stack)){
                            effectListTag.add(instance.toTag());
                        }
                        effectListTag.add(new EffectInstance(effect, 1).toTag());
                        toolTag.put(Effects.effectNBTtag, effectListTag);

                        stack.setTag(toolTag);
                        repairItemUsage = 1;

                        result.setInvStack(0, stack);
                        levelCost.set(1);
                    }
                } else if(modStack.getItem() == Items.LEATHER && !tool.getMaterial().isGripped){
                    int gripCost = ToolTypes.get(tool.getType()).getHandleGripCost();
                    if(gripCost > 0){
                        ToolBuilder.debugDummy();
                        CompoundTag tag = stack.getOrCreateTag();

                        Item grippedItem = Registry.ITEM.get(new Identifier(Registry.ITEM.getId((Item) tool).toString()+"_gripped"));

                        ItemStack resStack = new ItemStack(grippedItem);
                        resStack.setTag(tag);

                        result.setInvStack(0, resStack);
                        repairItemUsage = gripCost;
                        levelCost.set(1);
                    }
                }
            }
        } else if(stack.getItem() instanceof BuiltArmorItem){
            BuiltArmorItem armor = (BuiltArmorItem) stack.getItem();
            if(!modStack.isEmpty() && resultStack.isEmpty()){
                if(modStack.getItem() instanceof ModifierItem){
                    Effect effect = ((ModifierItem)modStack.getItem()).getEffect();
                    if(effect == null){
                        return;
                    }
        
                    Set<EffectInstance> effects = EffectInstance.mergeSets(armor.getEffects(stack), new HashSet<>(Arrays.asList(new EffectInstance(effect, 1))));
        
                    if(!armor.getEffects(stack).equals(effects)){
                        CompoundTag armorTag = stack.getOrCreateTag();

                        int numModifiers = Optional.of(armorTag.getInt("num_modifiers")).orElse(0);

                        int effectLevel = EffectInstance.getLevel(effects, effect);

                        if(numModifiers >= armor.getNumModifiers(stack) && effectLevel >= effect.getMaxLevel()){
                            return;
                        }

                        numModifiers++;
                        armorTag.putInt("num_modifiers", numModifiers);

                        ListTag effectListTag = new ListTag();
                        for(EffectInstance instance : effects){
                            effectListTag.add(instance.toTag());
                        }
                        armorTag.put(Effects.effectNBTtag, effectListTag);

                        stack.setTag(armorTag);
                        repairItemUsage = 1;

                        result.setInvStack(0, stack);
                        levelCost.set(1);
                    }
                }
            }
        }
    }

}