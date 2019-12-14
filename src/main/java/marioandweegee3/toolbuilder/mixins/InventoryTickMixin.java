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
import marioandweegee3.toolbuilder.api.item.BuiltArmorItem;
import marioandweegee3.toolbuilder.common.config.ConfigHandler;
import marioandweegee3.toolbuilder.common.effect.MagneticEffect;
import marioandweegee3.toolbuilder.common.effect.Effects;
import marioandweegee3.toolbuilder.common.tools.tooltypes.Bow;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.tag.FluidTags;
import net.minecraft.world.World;

@Mixin(ItemStack.class)
public abstract class InventoryTickMixin{
    private static final int tb_repairChance = 30;
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

    @SuppressWarnings("all")
    @Inject(method = "inventoryTick", at = @At("RETURN"))
    private void tick(World world, Entity holder, int int1, boolean boolean1, CallbackInfo ci){
        if(getItem() != null){
            if(holder != null){
                if(getItem() instanceof BuiltTool){
                    BuiltTool tool = (BuiltTool)getItem();
                    if(tool.getEffects(copy()).contains(Effects.GROWING) && getDamage()-1 >= 0){
                        if(!world.isClient && holder instanceof LivingEntity && world.random.nextInt(20*tb_repairChance) == 0){
                            if(((LivingEntity)holder).getActiveItem().getItem() != getItem()){
                                setDamage(getDamage()-1);
                            }
                        }
                    }
                } else if(getItem() instanceof Bow){
                    Bow bow = (Bow)getItem();
                    if(bow.getEffects().contains(Effects.GROWING) && getDamage()-1 >= 0){
                        if(!world.isClient && holder instanceof LivingEntity && world.random.nextInt(20*tb_repairChance) == 0){
                            if(((LivingEntity)holder).getActiveItem().getItem() != getItem()){
                                setDamage(getDamage()-1);
                            }
                        }
                    }
                } else if(getItem() instanceof BuiltArmorItem){
                    ArrayList<ItemStack> armorItems = new ArrayList<ItemStack>((Collection<ItemStack>)holder.getArmorItems());
                    BuiltArmorItem armor = (BuiltArmorItem) getItem();
                    if(armor.getEffects(copy()).contains(Effects.GROWING) && getDamage()-1 >= 0){
                        if(!world.isClient && holder instanceof LivingEntity && world.random.nextInt(20*tb_repairChance) == 0){
                            setDamage(getDamage()-1);
                        }
                    }
                    if(armor.getEffects(copy()).contains(Effects.FLAMING) && !holder.isOnFire()){
                        if(!world.isClient){
                            holder.setOnFireFor(ConfigHandler.INSTANCE.getFlamingTime());
                        }
                    }
                    if(armor.getEffects(copy()).contains(Effects.MAGNETIC) && holder instanceof LivingEntity){
                        int tick = tb_readArmorMagnetic();

                        if(armorItems.contains(this) && !world.isClient){
                            if(tick <= 2){
                                tick++;
                            } else {
                                MagneticEffect.run((LivingEntity) holder, world);
                                tick = 0;
                            }

                            tb_writeArmorMagnetic(tick);
                        }
                    }
                    if(armor.getEffects(copy()).contains(Effects.AQUATIC) && holder instanceof LivingEntity){
                        if(armorItems.contains(this) && !world.isClient){
                            LivingEntity entity = (LivingEntity) holder;
                            if(entity.isInFluid(FluidTags.WATER)){
                                if(armor.getSlotType() == EquipmentSlot.HEAD){
                                    entity.addStatusEffect(new StatusEffectInstance(StatusEffects.WATER_BREATHING, 20, 0, false, false));
                                } else {
                                    entity.addStatusEffect(new StatusEffectInstance(StatusEffects.DOLPHINS_GRACE, 20, 1, false, false));
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void tb_writeArmorMagnetic(int ticks){
        CompoundTag tag = getOrCreateTag();
        tag.putInt(Effects.magneticTickNBTtag, ticks);
    }

    private int tb_readArmorMagnetic(){
        CompoundTag tag = getOrCreateTag();
        if(tag.contains(Effects.magneticTickNBTtag)){
            return tag.getInt(Effects.magneticTickNBTtag);
        }

        return 0;
    }

    @Inject(method = "getMaxDamage", at = @At("RETURN"), cancellable = true)
    private void maxDamageHandler(CallbackInfoReturnable<Integer> ci){
        int maxDamage = ci.getReturnValueI();

        if(getItem() instanceof BuiltTool){
            BuiltTool tool = (BuiltTool)getItem();
            if(tool.getEffects(copy()).contains(Effects.DURABLE)){
                maxDamage *= ConfigHandler.INSTANCE.getDurableMultiplier();
            }
        } else if(getItem() instanceof BuiltArmorItem){
            BuiltArmorItem armor = (BuiltArmorItem) getItem();
            if(armor.getEffects(copy()).contains(Effects.DURABLE)){
                maxDamage *= ConfigHandler.INSTANCE.getDurableMultiplier();
            }
        }

        ci.setReturnValue(maxDamage);
    }
}