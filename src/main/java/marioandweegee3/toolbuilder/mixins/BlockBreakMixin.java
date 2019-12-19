package marioandweegee3.toolbuilder.mixins;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import marioandweegee3.toolbuilder.api.BuiltTool;
import marioandweegee3.toolbuilder.api.effect.EffectInstance;
import marioandweegee3.toolbuilder.api.item.BuiltArmorItem;
import marioandweegee3.toolbuilder.common.config.ConfigHandler;
import marioandweegee3.toolbuilder.common.tools.tooltypes.Shears;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.world.BlockView;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;

@Mixin(Block.class)
public abstract class BlockBreakMixin implements ItemConvertible{
    @Inject(at = @At("RETURN"), method = "getDroppedStacks", cancellable = true)
    public void dropHandler(BlockState state, LootContext.Builder builder, CallbackInfoReturnable<List<ItemStack>> ci){
        List<ItemStack> drops = ci.getReturnValue();
        ItemStack tool = builder.get(LootContextParameters.TOOL);

        if(tool.getItem() instanceof BuiltTool){
            BuiltTool builtTool = (BuiltTool)tool.getItem();

            if(tool.getItem() instanceof Shears){
                List<Identifier> lootTables = ConfigHandler.INSTANCE.shearLootTables();
                for(Identifier table : lootTables){
                    Block block = state.getBlock();
                    if(block.getDropTableId().equals(table)){
                        List<ItemStack> list = new ArrayList<>();
                        list.add(new ItemStack(block));
                        ci.setReturnValue(list);
                    }
                }
            }

            for(EffectInstance effect : builtTool.getEffects(tool)){
                drops = effect.getEffect().modifyBlockDrops(state, builtTool, tool, builder, drops, effect.getLevel());
            }
        }
    }

    @Inject(method = "onEntityLand", at = @At("HEAD"), cancellable = true)
    public void handleEntityLanding(BlockView view, Entity entity, CallbackInfo ci){
        if(entity instanceof LivingEntity && Math.abs(entity.getVelocity().y) > 0.6){
            for(ItemStack stack : entity.getArmorItems()){
                if(stack.getItem() instanceof BuiltArmorItem){
                    BuiltArmorItem armor = (BuiltArmorItem) stack.getItem();
                    for(EffectInstance effect : armor.getEffects(stack)) {
                        effect.getEffect().onEntityLandOnBlock(view, armor, (LivingEntity) entity, effect.getLevel());
                    }
                }
            }
        }
    }
}