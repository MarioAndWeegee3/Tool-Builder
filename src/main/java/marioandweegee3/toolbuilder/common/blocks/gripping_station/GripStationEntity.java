package marioandweegee3.toolbuilder.common.blocks.gripping_station;

import marioandweegee3.toolbuilder.ToolBuilder;
import marioandweegee3.toolbuilder.api.BuiltTool;
import marioandweegee3.toolbuilder.api.ToolType;
import marioandweegee3.toolbuilder.common.tools.tooltypes.ToolTypes;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Identifier;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;

public class GripStationEntity extends BlockEntity implements Tickable, SidedInventory {
    private DefaultedList<ItemStack> inventory = DefaultedList.ofSize(3, ItemStack.EMPTY);

    public GripStationEntity() {
        super(ToolBuilder.GRIPPING_STATION);
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        CompoundTag item = new CompoundTag();
        item.putString("item", Registry.ITEM.getId(inventory.get(0).getItem()).toString());
        item.put("data", inventory.get(0).getOrCreateTag());
        tag.put("tool", item);

        tag.putInt("leather_count", inventory.get(1).getCount());

        return tag;
    }

    @Override
    public void fromTag(CompoundTag tag) {
        super.fromTag(tag);
        CompoundTag item = tag.getCompound("tool");
        ItemStack tool = new ItemStack(Registry.ITEM.get(new Identifier(item.getString("item"))));
        tool.setTag(item.getCompound("data"));
        inventory.set(0, tool);

        int leatherCount = tag.getInt("leather_count");
        inventory.set(1, new ItemStack(Items.LEATHER, leatherCount));
    }

    public boolean addTool(ItemStack stack) {
        if (inventory.get(0).isEmpty()) {
            inventory.set(0, stack.copy());
            return true;
        } else return false;
    }

    public int addLeather(ItemStack stack) {
        if (inventory.get(1).isEmpty()) {
            inventory.set(1, stack.copy());
            return 0;
        } else {
            int prevCount = inventory.get(1).getCount();
            inventory.get(1).increment(stack.getCount());
            if (inventory.get(1).getCount() == inventory.get(1).getMaxCount()) {
                int added = inventory.get(1).getMaxCount() - prevCount;
                return stack.getCount() - added;
            } else {
                return 0;
            }
        }
    }

    @Override
    public void tick() {
        if (!inventory.get(0).isEmpty() && !inventory.get(1).isEmpty()) {
            if (inventory.get(0).getItem() instanceof BuiltTool) {
                BuiltTool tool = (BuiltTool) inventory.get(0).getItem();
                ToolType toolType = ToolTypes.get(tool.getType());
                if (inventory.get(1).getCount() >= toolType.getHandleGripCost() && !tool.getMaterial().isGripped
                        && !world.isClient) {
                    ToolBuilder.Builder builder = new ToolBuilder.Builder(tool.getMaterial().handle,
                            tool.getMaterial().head, toolType, true);
                    ItemStack toolStack = new ItemStack(
                            Registry.ITEM.get(new Identifier(tool.getModName(), builder.name)));

                    toolStack.setTag(inventory.get(0).getOrCreateTag());

                    inventory.set(2, toolStack);
                    inventory.get(1).decrement(toolType.getHandleGripCost());
                    inventory.get(0).setCount(0);
                } else if(tool.getMaterial().isGripped && !world.isClient){
                    inventory.set(2, inventory.get(0));
                }
            }
        } else if (!inventory.get(0).isEmpty() && inventory.get(2).isEmpty() && inventory.get(0).getItem() instanceof BuiltTool 
                && ((BuiltTool) inventory.get(0).getItem()).getMaterial().isGripped && !world.isClient){
            inventory.set(2, inventory.get(0));
        }
    }

    public void dropItems() {
        if (!world.isClient) {
            ItemScatterer.spawn(world, pos, inventory);
        }
    }

    public void giveLeather(PlayerEntity player) {
        if (!world.isClient) {
            player.inventory.insertStack(inventory.get(1));
        }
    }

    public void giveResultTool(PlayerEntity player){
        if (!world.isClient) {
            player.inventory.insertStack(inventory.get(2));
        }
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
        if(slot == 0 && !(stack.getItem() instanceof BuiltTool)){
            return false;
        }

        if(slot == 1 && !(stack.getItem() == Items.LEATHER)){
            return false;
        }

        return dir != Direction.DOWN && slot != 2;
    }

    @Override
    public int[] getInvAvailableSlots(Direction dir) {
        if(dir == Direction.UP){
            return new int[]{1};
        } else if (dir != Direction.DOWN){
            return new int[]{0};
        } else return new int[]{2};
    }

}