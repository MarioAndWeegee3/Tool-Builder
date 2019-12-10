package marioandweegee3.toolbuilder.common.tools.init;

import marioandweegee3.toolbuilder.ToolBuilder;
import marioandweegee3.toolbuilder.api.effect.Effect;
import marioandweegee3.toolbuilder.api.entry.TBInitializer;
import marioandweegee3.toolbuilder.api.material.*;
import marioandweegee3.toolbuilder.api.registry.TBRegistries;
import marioandweegee3.toolbuilder.common.tools.*;
import marioandweegee3.toolbuilder.common.effect.Effects;
import net.minecraft.util.Identifier;

public class MaterialInit implements TBInitializer {

    @Override
    public void tbInitialize() {
        ToolBuilder.logger.info("Registering Materials and Effects");
        for(HeadMaterial material : HeadMaterials.values()){
            TBRegistries.HEAD_MATERIALS.put(new Identifier("toolbuilder", material.getName()), material);
        }

        for(HandleMaterial material : HandleMaterials.values()){
            TBRegistries.HANDLE_MATERIALS.put(new Identifier("toolbuilder", material.getName()), material);
        }

        for(StringMaterial material : StringMaterials.values()){
            TBRegistries.STRING_MATERIALS.put(new Identifier("toolbuilder", material.getName()), material);
        }

        for(Effect effect : Effects.values()){
            TBRegistries.EFFECTS.put(new Identifier("toolbuilder", effect.getName()), effect);
        }
    }

}