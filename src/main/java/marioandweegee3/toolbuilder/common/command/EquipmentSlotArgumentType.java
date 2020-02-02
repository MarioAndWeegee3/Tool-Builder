package marioandweegee3.toolbuilder.common.command;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.entity.EquipmentSlot;

public class EquipmentSlotArgumentType implements ArgumentType<EquipmentSlot> {

    @Override
    public EquipmentSlot parse(StringReader reader) throws CommandSyntaxException {
        String line = reader.readString();

        switch(line){
            case "helmet": return EquipmentSlot.HEAD;
            case "chestplate": return EquipmentSlot.CHEST;
            case "leggings": return EquipmentSlot.LEGS;
            case "boots": return EquipmentSlot.FEET;
            default: return EquipmentSlot.byName(line);
        }
    }

}