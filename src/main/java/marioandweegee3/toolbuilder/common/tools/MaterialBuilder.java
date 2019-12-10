package marioandweegee3.toolbuilder.common.tools;

import marioandweegee3.toolbuilder.api.material.*;

public class MaterialBuilder{
    private BuiltToolMaterial material;
    private MaterialBuilder(HandleMaterial handle, HeadMaterial head, String name, Boolean grip) {
        this.material = BuiltToolMaterial.of(handle, head, name, grip);
    }

    private BuiltToolMaterial getMaterial(){
        return this.material;
    }

    public static BuiltToolMaterial build(HandleMaterial handle, HeadMaterial head, String name, Boolean grip){
        MaterialBuilder builder = new MaterialBuilder(handle, head, name, grip);
        return builder.getMaterial();
    }
}