package marioandweegee3.toolbuilder.api;

import marioandweegee3.toolbuilder.api.material.BuiltToolMaterial;
import net.minecraft.item.Item;

@FunctionalInterface
public interface ToolCreator<X extends Item>{

    public X build(BuiltToolMaterial material);
}