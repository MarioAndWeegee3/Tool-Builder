package marioandweegee3.toolbuilder.client.models;

import com.swordglowsblue.artifice.api.ArtificeResourcePack.ClientResourcePackBuilder;

import marioandweegee3.toolbuilder.ToolBuilder;

public class NewToolModel extends ToolModel{
    public NewToolModel(ToolBuilder.ToolItemBuilder builder){
        super(builder);
    }

    @Override
    public void add(ClientResourcePackBuilder pack){
        pack.addItemModel(ToolBuilder.makeID(builder.name), model -> {
            String type = builder.getType().getName();
            String head = builder.getMaterial().head.getName();
            String handle = builder.getMaterial().handle.getName();
            model.parent(makeid("item/template/"+type));
            model.texture("head", makeid("item/"+type+"/model/"+head));
            model.texture("handle", makeid("item/handle/model/"+handle));
        });
    }
}