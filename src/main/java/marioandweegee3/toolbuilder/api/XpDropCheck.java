package marioandweegee3.toolbuilder.api;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;

public interface XpDropCheck {
    public abstract boolean check(BlockState state, ItemStack stack);
}