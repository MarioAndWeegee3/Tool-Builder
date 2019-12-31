package marioandweegee3.toolbuilder.client.models;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.swordglowsblue.artifice.api.ArtificeResourcePack.ClientResourcePackBuilder;
import com.swordglowsblue.artifice.api.builder.assets.ModelBuilder;
import com.swordglowsblue.artifice.api.util.Processor;

import marioandweegee3.toolbuilder.ToolBuilder;
import net.minecraft.util.Identifier;

public class TBModels {
    public static Set<ToolModel> toolModels = new HashSet<>(0);
    public static Set<BowModel> bowModels = new HashSet<>(0);

    public static Map<String, String> simpleItems = new HashMap<>(0);
    public static Set<String> blockItems = new HashSet<>(0);
    public static Map<String, String> simpleBlocks = new HashMap<>(0);

    public static Map<String, Processor<ModelBuilder>> customItems = new HashMap<>(0);

    public static void addModels(ClientResourcePackBuilder pack){
        for(ToolModel model : toolModels){
            model.add(pack);
        }
        for(BowModel model : bowModels){
            model.add(pack);
        }
        for(Map.Entry<String, String> entry : simpleItems.entrySet()){
            pack.addItemModel(ToolBuilder.makeID(entry.getKey()), model->{
                model.parent(new Identifier("item/generated"));
                model.texture("layer0", ToolBuilder.makeID("item/"+entry.getValue()));
            });
        }
        for(String item : blockItems){
            pack.addItemModel(ToolBuilder.makeID(item), model->{
                model.parent(ToolBuilder.makeID("block/"+item));
            });
        }
        for(Map.Entry<String, String> entry : simpleBlocks.entrySet()){
            pack.addBlockState(ToolBuilder.makeID(entry.getKey()), state->{
                state.variant("", settings->{
                    settings.model(ToolBuilder.makeID("block/"+entry.getKey()));
                });
            });

            pack.addBlockModel(ToolBuilder.makeID(entry.getKey()), model->{
                model.parent(new Identifier("block/cube_all"));
                Identifier texture = ToolBuilder.makeID("block/"+entry.getValue());
                model.texture("all", texture);
                model.texture("particle", texture);
            });
        }

        for(Map.Entry<String, Processor<ModelBuilder>> entry : customItems.entrySet()){
            pack.addItemModel(ToolBuilder.makeID(entry.getKey()), entry.getValue());
        }
    }
}