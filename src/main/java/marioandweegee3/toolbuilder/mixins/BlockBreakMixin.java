package marioandweegee3.toolbuilder.mixins;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import marioandweegee3.toolbuilder.api.BuiltTool;
import marioandweegee3.toolbuilder.api.item.BuiltArmorItem;
import marioandweegee3.toolbuilder.common.config.ConfigHandler;
import marioandweegee3.toolbuilder.common.effect.Effects;
import marioandweegee3.toolbuilder.common.tools.tooltypes.Shears;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.recipe.SmeltingRecipe;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.BlockView;
import net.minecraft.world.loot.context.LootContext;
import net.minecraft.world.loot.context.LootContextParameters;

@Mixin(Block.class)
public abstract class BlockBreakMixin implements ItemConvertible{
    @Inject(at = @At("RETURN"), method = "getDroppedStacks", cancellable = true)
    public void dropHandler(BlockState state, LootContext.Builder builder, CallbackInfoReturnable<List<ItemStack>> ci){
        List<ItemStack> drops = ci.getReturnValue();
        ServerWorld world = builder.getWorld();
        RecipeManager recipeManager = world.getRecipeManager();
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

            if(builtTool.getEffects(tool).contains(Effects.FLAMING)){
                List<SmeltingRecipe> smeltingRecipes = new ArrayList<>(0);
                for(Recipe<?> recipe : recipeManager.values()){
                    if(recipe instanceof SmeltingRecipe){
                        smeltingRecipes.add((SmeltingRecipe)recipe);
                    }
                }

                Set<ItemStack> smelted = new HashSet<>(0);

                for(ItemStack stack : drops){
                    for(SmeltingRecipe recipe : smeltingRecipes){
                        DefaultedList<Ingredient> inputs = recipe.getPreviewInputs();
                        for(Ingredient input : inputs){
                            for(int id : input.getIds()){
                                Item inputItem = Registry.ITEM.get(id);
                                if(stack.getItem() == inputItem){
                                    smelted.add(BuiltTool.increaseCountForFortune(tool, recipe.getOutput()));
                                    break;
                                }
                            }
                        }
                    }
                }

                if(smelted.size() == 0){
                    return;
                } else {
                    drops.clear();
                }

                for(ItemStack stack : smelted){
                    drops.add(stack);
                }

                ci.setReturnValue(drops);
            }
            if(builtTool.getEffects(tool).contains(Effects.BOUNCY)){
                if(world.random.nextInt(10) == 0){
                    drops.add(new ItemStack(Items.SLIME_BALL));
                }
                ci.setReturnValue(drops);
            }
        }
    }

    @Inject(method = "onEntityLand", at = @At("HEAD"), cancellable = true)
    public void handleEntityLanding(BlockView view, Entity entity, CallbackInfo ci){
        if(entity instanceof LivingEntity && Math.abs(entity.getVelocity().y) > 0.6){
            for(ItemStack stack : entity.getArmorItems()){
                if(stack.getItem() instanceof BuiltArmorItem){
                    BuiltArmorItem armor = (BuiltArmorItem) stack.getItem();
                    if(armor.getEffects(stack).contains(Effects.BOUNCY) && armor.getSlotType() == EquipmentSlot.FEET){
                        double speedMult = -ConfigHandler.INSTANCE.getBounceSpeedMultiplier(),
                                sneakSpeedMult = -ConfigHandler.INSTANCE.getSneakBounceSpeedMultiplier();
                        Vec3d vel = new Vec3d(entity.getVelocity().x, entity.getVelocity().y, entity.getVelocity().z)
                                .multiply(2, entity.isSneaking() ? sneakSpeedMult : speedMult, 2);
                        //This prevents the player from building up insane speed an launching 1,000,000 blocks into the air.
                        //Yes, that did happen. If the bounce speed limit is removed, you will go very fast.
                        double maxVelocity = ConfigHandler.INSTANCE.getMaxBounceVelocity();
                        if(vel.y >= maxVelocity && ConfigHandler.INSTANCE.limitBounceHeight()){
                            entity.setVelocity(vel.x, maxVelocity, vel.z);
                        } else {
                            entity.setVelocity(vel);
                        }
                        
                        entity.world.playSound(null, entity.getBlockPos(), SoundEvents.ENTITY_SLIME_JUMP, SoundCategory.PLAYERS, 1, 0.5f);
                        ci.cancel();
                        return;
                    }
                }
            }
        }
    }
}