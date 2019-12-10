package marioandweegee3.toolbuilder.common.effect;

import java.util.List;

import marioandweegee3.toolbuilder.common.config.ConfigHandler;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class MagneticEffect{
    public static void run(LivingEntity miner, World world){
        //Based on code from Tinkers Construct
        double x = miner.x;
        double y = miner.y;
        double z = miner.z;

        double range = ConfigHandler.INSTANCE.getMagneticRange();

        List<ItemEntity> items = world.getEntities(ItemEntity.class, new Box(x-range, y-range, z-range, x+range, y+range, z+range));

        int pulled = 0;

        for(ItemEntity item : items){
            if(item.getStack().isEmpty() || !item.isAlive()){
                continue;
            }

            if(pulled > 200){
                break;
            }

            float strength = 0.2f;

            Vec3d vec = new Vec3d(x, y, z);
            vec = vec.subtract(new Vec3d(item.x, item.y, item.z));

            vec = vec.normalize();
            vec = vec.multiply(strength);

            item.addVelocity(vec.x, vec.y, vec.z);

            pulled++;
        }
    }
}