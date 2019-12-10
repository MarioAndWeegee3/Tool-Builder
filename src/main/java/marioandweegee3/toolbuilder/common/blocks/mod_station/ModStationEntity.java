package marioandweegee3.toolbuilder.common.blocks.mod_station;

import marioandweegee3.toolbuilder.ToolBuilder;
import marioandweegee3.toolbuilder.api.BuiltTool;
import marioandweegee3.toolbuilder.api.item.Modifier;
import marioandweegee3.toolbuilder.common.effect.Effects;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Identifier;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;

public class ModStationEntity extends BlockEntity implements Tickable, SidedInventory {
    private DefaultedList<ItemStack> inventory = DefaultedList.ofSize(3, ItemStack.EMPTY);

    public ModStationEntity() {
        super(ToolBuilder.MOD_STATION);
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        
        CompoundTag tool = new CompoundTag();
        tool.putString("item", Registry.ITEM.getId(inventory.get(0).getItem()).toString());
        tool.put("data", inventory.get(0).getOrCreateTag());
        tool.putInt("count", inventory.get(0).getCount());

        CompoundTag mod = new CompoundTag();
        mod.putString("item", Registry.ITEM.getId(inventory.get(1).getItem()).toString());
        mod.put("data", inventory.get(1).getOrCreateTag());
        mod.putInt("count", inventory.get(1).getCount());

        CompoundTag result = new CompoundTag();
        result.putString("item", Registry.ITEM.getId(inventory.get(2).getItem()).toString());
        result.put("data", inventory.get(2).getOrCreateTag());
        result.putInt("count", inventory.get(2).getCount());

        tag.put("tool", tool);
        tag.put("modifier", mod);
        tag.put("result", result);

        return tag;
    }

    @Override
    public void fromTag(CompoundTag tag) {
        super.fromTag(tag);

        CompoundTag toolTag = tag.getCompound("tool");
        ItemStack toolStack = new ItemStack(Registry.ITEM.get(new Identifier(toolTag.getString("item"))), toolTag.getInt("count"));
        toolStack.setTag(toolTag.getCompound("data"));

        CompoundTag modTag = tag.getCompound("tool");
        ItemStack modStack = new ItemStack(Registry.ITEM.get(new Identifier(modTag.getString("item"))), modTag.getInt("count"));
        modStack.setTag(modTag.getCompound("data"));

        CompoundTag resultTag = tag.getCompound("tool");
        ItemStack resultStack = new ItemStack(Registry.ITEM.get(new Identifier(resultTag.getString("item"))), resultTag.getInt("count"));
        resultStack.setTag(resultTag.getCompound("data"));

        inventory.set(0, toolStack);
        inventory.set(1, modStack);
        inventory.set(2, resultStack);
    }

    @Override
    public boolean canPlayerUseInv(PlayerEntity player) {
        return false;
    }

    @Override
    public int getInvSize() {
        return 2;
    }

    @Override
    public ItemStack getInvStack(int slot) {
        return inventory.get(slot);
    }

    @Override
    public boolean isInvEmpty() {
        return inventory.isEmpty();
    }

    @Override
    public ItemStack removeInvStack(int slot) {
        return Inventories.removeStack(inventory, slot);
    }

    @Override
    public void setInvStack(int slot, ItemStack stack) {
        this.inventory.set(slot, stack);
        if (stack.getCount() > this.getInvMaxStackAmount()) {
            stack.setCount(this.getInvMaxStackAmount());
        }
    }

    @Override
    public ItemStack takeInvStack(int slot, int amount) {
        return Inventories.splitStack(inventory, slot, amount);
    }

    @Override
    public void clear() {
        inventory.clear();
    }

    @Override
    public boolean canExtractInvStack(int slot, ItemStack stack, Direction dir) {
        return dir == Direction.DOWN && slot == 2;
    }

    @Override
    public boolean canInsertInvStack(int slot, ItemStack stack, Direction dir) {
        if(dir == Direction.DOWN) return false;

        if(dir == Direction.UP && (!(stack.getItem() instanceof Modifier) || stack.getItem() != inventory.get(1).getItem())) return false;

        if(stack.getItem() instanceof BuiltTool) return true;
        else return false;
    }

    @Override
    public int[] getInvAvailableSlots(Direction dir) {
        switch(dir){
            case UP: return new int[]{1};
            case DOWN: return new int[0];
            default: return new int[]{0};
        }
    }

    @Override
    public void tick() {
        if(!world.isClient){
            ItemStack toolStack = inventory.get(0);
            ItemStack modStack = inventory.get(1);
            if(toolStack.isEmpty() && modStack.isEmpty()) return;
            if(modStack.getItem() instanceof Modifier && toolStack.getItem() instanceof BuiltTool){
                BuiltTool tool = (BuiltTool) toolStack.getItem();
                Modifier modifier = (Modifier) modStack.getItem();
                if(tool.getEffects(toolStack).contains(modifier.getEffect())) {
                    inventory.set(2, toolStack.copy());
                    inventory.get(0).setCount(0);
                    return;
                }
                CompoundTag toolTag = toolStack.getOrCreateTag();
                int modifiers = toolTag.getInt("num_modifiers");
                int maxModifiers = tool.getNumModifiers(toolStack);
                if(modifiers >= maxModifiers){
                    inventory.set(2, toolStack.copy());
                    inventory.get(0).setCount(0);
                    return;
                }

                ListTag effectsTag = new ListTag();
                if(toolTag.containsKey(Effects.effectNBTtag)){
                    effectsTag = toolTag.getList(Effects.effectNBTtag, 8);
                }
                effectsTag.add(new StringTag(modifier.getEffect().getID().toString()));
                toolTag.put(Effects.effectNBTtag, effectsTag);

                ToolBuilder.logger.info("Adding modifier "+modifier.getEffect().getID().toString()+" to tool "+toolStack.getTranslationKey());

                modifiers++;
                toolTag.putInt("num_modifiers", modifiers);

                toolStack.setTag(toolTag);
                inventory.set(2, toolStack.copy());
                inventory.get(0).setCount(0);
                modStack.decrement(1);
            }
        }
    }

    public boolean addTool(ItemStack stack) {
        if (inventory.get(0).isEmpty()) {
            inventory.set(0, stack.copy());
            return true;
        } else return false;
    }

    public int addModifier(ItemStack stack) {
        if (inventory.get(1).isEmpty()) {
            inventory.set(1, stack.copy());
            return 0;
        } else if(stack.getItem() == inventory.get(1).getItem()){
            int prevCount = inventory.get(1).getCount();
            inventory.get(1).increment(stack.getCount());
            if (inventory.get(1).getCount() == inventory.get(1).getMaxCount()) {
                int added = inventory.get(1).getMaxCount() - prevCount;
                return stack.getCount() - added;
            } else {
                return 0;
            }
        } else {
            //Returns if the item is not the same as the current item or the current item is not empty.
            return -1;
        }
    }

    public void dropItems() {
        if (!world.isClient) {
            ItemScatterer.spawn(world, pos, inventory);
        }
    }

    public void giveInventory(PlayerEntity player){
        if (!world.isClient) {
            player.inventory.insertStack(inventory.get(0));
            player.inventory.insertStack(inventory.get(1));
            player.inventory.insertStack(inventory.get(2));
            
        }
    }

}