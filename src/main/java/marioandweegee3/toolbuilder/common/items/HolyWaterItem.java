package marioandweegee3.toolbuilder.common.items;

import marioandweegee3.toolbuilder.api.item.ModifierItem;
import marioandweegee3.toolbuilder.common.config.ConfigHandler;
import marioandweegee3.toolbuilder.common.effect.Effects;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

public class HolyWaterItem extends ModifierItem {

    public HolyWaterItem() {
        super(Effects.HOLY);
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.DRINK;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        if(!world.isClient){
            player.addStatusEffect(
                    new StatusEffectInstance(StatusEffects.LUCK,
                    ConfigHandler.INSTANCE.getHolyLuckTime(),
                    ConfigHandler.INSTANCE.getHolyLuckLevel()-1)
            );
            stack.decrement(1);
        }
        return new TypedActionResult<>(ActionResult.SUCCESS, stack);
    }

}