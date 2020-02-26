package marioandweegee3.toolbuilder.mixins;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import marioandweegee3.toolbuilder.api.BuiltTool;
import marioandweegee3.toolbuilder.api.effect.EffectInstance;
import marioandweegee3.toolbuilder.api.item.BuiltArmorItem;
import marioandweegee3.toolbuilder.common.tools.tooltypes.Bow;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;

@Mixin(ItemStack.class)
public abstract class InventoryTickMixin{
    @Shadow
    public abstract Item getItem();

    @Shadow
    public abstract ItemStack copy();

    @Shadow
    public abstract CompoundTag getOrCreateTag();

    @Shadow
    public abstract boolean isDamaged();

    @Shadow
    public abstract int getDamage();

    @Shadow
    public abstract void setDamage(int int_1);

    @Shadow
    public abstract int getMaxDamage();

    @Shadow
    public abstract boolean damage(int int_1, Random random_1, ServerPlayerEntity serverPlayerEntity_1);

    @Shadow
    public abstract void setTag(CompoundTag tag);

    @SuppressWarnings("all")
    @Inject(method = "inventoryTick", at = @At("RETURN"))
    private void tick(World world, Entity holder, int int1, boolean boolean1, CallbackInfo ci){
        if(getItem() != null){
            if(holder != null){
                if(!(holder instanceof LivingEntity) || world.isClient) return;

                ItemStack copy = copy();
                if(getItem() instanceof BuiltTool){
                    BuiltTool tool = (BuiltTool)getItem();
                    for(EffectInstance effect : tool.getEffects(copy)) {
                        effect.getEffect().onInventoryTick(copy, (LivingEntity) holder, effect.getLevel());
                    }
                    this.setTag(copy.getOrCreateTag());
                } else if(getItem() instanceof Bow){
                    Bow bow = (Bow)getItem();
                    for(EffectInstance effect : bow.getEffects()) {
                        effect.getEffect().onInventoryTick(copy, (LivingEntity) holder, effect.getLevel());
                    }
                    this.setTag(copy.getOrCreateTag());
                } else if(getItem() instanceof BuiltArmorItem){
                    ArrayList<ItemStack> armorItems = new ArrayList<>((Collection<ItemStack>)holder.getArmorItems());
                    BuiltArmorItem armor = (BuiltArmorItem) getItem();
                    if(armorItems.contains(this) && !world.isClient) {
                        for(EffectInstance instance : armor.getEffects(copy)){
                            instance.getEffect().onArmorInventoryTick(copy, (LivingEntity) holder, instance.getLevel());
                        }
                    }
                    this.setTag(copy.getOrCreateTag());
                }

                
            }
        }
    }

    @Inject(method = "getMaxDamage", at = @At("RETURN"), cancellable = true)
    private void maxDamageHandler(CallbackInfoReturnable<Integer> ci){
        int maxDamage = ci.getReturnValueI();

        if(getItem() instanceof BuiltTool){
            BuiltTool tool = (BuiltTool)getItem();
            for(EffectInstance instance : tool.getEffects(copy())){
                maxDamage = instance.getEffect().modifyDurability(maxDamage);
            }
        } else if(getItem() instanceof BuiltArmorItem){
            BuiltArmorItem armor = (BuiltArmorItem) getItem();
            for(EffectInstance instance : armor.getEffects(copy())){
                maxDamage = instance.getEffect().modifyDurability(maxDamage);
            }
        }

        ci.setReturnValue(maxDamage);
    }
}