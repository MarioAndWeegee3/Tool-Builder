package marioandweegee3.toolbuilder.client;

import com.swordglowsblue.artifice.api.Artifice;

import marioandweegee3.toolbuilder.client.models.TBModels;
import net.fabricmc.api.ClientModInitializer;

public class ToolBuilderClient implements ClientModInitializer{

    @Override
    public void onInitializeClient() {
        Artifice.registerAssets("toolbuilder:items", pack ->{
            TBModels.addModels(pack);
        });
    }
}