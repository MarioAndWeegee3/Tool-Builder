package marioandweegee3.toolbuilder.api.registry;

import java.util.HashMap;
import java.util.Map;

import marioandweegee3.toolbuilder.api.effect.Effect;
import marioandweegee3.toolbuilder.api.material.*;
import net.minecraft.util.Identifier;

public class TBRegistries {
    public static Map<Identifier, HeadMaterial> HEAD_MATERIALS = new HashMap<>();
    public static Map<Identifier, HandleMaterial> HANDLE_MATERIALS = new HashMap<>();
    public static Map<Identifier, StringMaterial> STRING_MATERIALS = new HashMap<>();
    public static Map<Identifier, BuiltArmorMaterial> ARMOR_MATERIALS = new HashMap<>();
    public static Map<Identifier, Effect> EFFECTS = new HashMap<>();
}