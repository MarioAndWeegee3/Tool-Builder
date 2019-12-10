package marioandweegee3.toolbuilder.common.data.loot_tables;

import com.swordglowsblue.artifice.api.ArtificeResourcePack.ServerResourcePackBuilder;

import net.minecraft.util.Identifier;

public class BasicBlockLootTable {
    protected Identifier block;
    protected String header;

    public BasicBlockLootTable(Identifier block){
        this(block, "block/");
    }

    public BasicBlockLootTable(Identifier block, String header){
        this.block = block;
        this.header = header;
    }

    public void add(ServerResourcePackBuilder pack){
        pack.addLootTable(new Identifier(block.getNamespace(), header+block.getPath()), table -> {
            table.pool(pool -> {
                pool.condition(new Identifier("survives_explosion"), j->{});
                pool.rolls(1);
                pool.entry(entry -> {
                    entry.type(new Identifier("item"));
                    entry.name(block);
                });
            });
        });
    }
}