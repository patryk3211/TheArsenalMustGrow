package org.patryk3211.tamg.gun.shotgun;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.patryk3211.tamg.gun.BulletEntity;
import org.patryk3211.tamg.gun.GunItem;

public class ShotgunItem extends GunItem {
    public ShotgunItem(Properties settings) {
        super(settings);
    }

    @Override
    protected void spawnProjectile(Level world, Vec3 barrelPos, Vec3 motion, Player user, ItemStack stack) {
        final float yawSpread = 1.5f;
        final int bulletCount = 10;
        for(int i = 0; i < bulletCount; ++i) {
            var pitch = world.random.nextFloat() * 0.4f - 0.2f;
            var yaw = -(yawSpread * 0.5f) + yawSpread * i / (bulletCount - 1);
            yaw += world.random.nextFloat() * 0.05f - 0.025f;
            var rMotion = motion.xRot(pitch).yRot(yaw);
            var projectileEntity = BulletEntity.create(world, barrelPos, rMotion, this);
            projectileEntity.small();
            projectileEntity.setOwner(user);
            world.addFreshEntity(projectileEntity.withTTL(5));
        }
    }
}
