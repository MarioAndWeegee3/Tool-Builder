package marioandweegee3.toolbuilder.client.models;

import com.swordglowsblue.artifice.api.ArtificeResourcePack.ClientResourcePackBuilder;

import marioandweegee3.toolbuilder.ToolBuilder;
import marioandweegee3.toolbuilder.api.material.BowMaterial;
import net.minecraft.util.Identifier;

public class BowModel {
    private ToolBuilder.BowBuilder builder;

    public BowModel(ToolBuilder.BowBuilder builder){
        this.builder = builder;
    }

    public void add(ClientResourcePackBuilder pack){
        BowMaterial material = builder.getMaterial();
        String handle = material.handle.getName();
        String string = material.string.getName();
        String grip = material.grip.toString();

        for(int[] i = {0}; i[0] < 3; i[0]++){
            pack.addItemModel(makeid(builder.name+"_"+i[0]), model ->{
                model.parent(makeid("item/_bow_base"));
                model.texture("layer0", makeid("item/bow/string/"+string+"/"+i[0]));
                model.texture("layer1", makeid("item/bow/handle/"+handle+"/"+i[0]));
                model.texture("layer2", makeid("item/bow/grip/"+i[0]+"/"+grip));
                model.texture("layer3", makeid("item/bow/arrow/"+i[0]));
            });
        }
    }

    private Identifier makeid(String name){
        return ToolBuilder.makeID(name);
    }
}