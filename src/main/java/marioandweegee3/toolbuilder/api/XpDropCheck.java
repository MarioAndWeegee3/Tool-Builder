package marioandweegee3.toolbuilder.api;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;

@FunctionalInterface
public interface XpDropCheck {
    boolean check(BlockState state, ItemStack stack);
}