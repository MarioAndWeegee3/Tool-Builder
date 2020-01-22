package marioandweegee3.toolbuilder.client.models;

import com.swordglowsblue.artifice.api.ArtificeResourcePack.ClientResourcePackBuilder;

import marioandweegee3.toolbuilder.ToolBuilder;
import marioandweegee3.toolbuilder.api.material.BuiltToolMaterial;
import net.minecraft.util.Identifier;

public class ToolModel {
    protected ToolBuilder.ToolItemBuilder builder;

    public ToolModel(ToolBuilder.ToolItemBuilder builder){
        this.builder = builder;
    }

    public void add(ClientResourcePackBuilder pack){
        BuiltToolMaterial material = builder.getMaterial();
        String head = material.head.getName();
        String handle = material.handle.getName();
        String type = builder.getType().getName();

        pack.addItemModel(ToolBuilder.makeID(builder.name), model -> {
            model.parent(new Identifier("item/handheld"));
            model.texture("layer1", makeid("item/handle/"+handle+"/"+type));
            model.texture("layer0", makeid("item/"+type+"/"+head));
            if(material.isGripped){
                model.texture("layer2", makeid("item/handle/grip/"+type));
            }
        });
    }

    protected Identifier makeid(String name){
        return ToolBuilder.makeID(name);
    }
}