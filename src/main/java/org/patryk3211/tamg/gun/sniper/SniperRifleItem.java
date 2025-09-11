package org.patryk3211.tamg.gun.sniper;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.patryk3211.tamg.gun.BulletEntity;
import org.patryk3211.tamg.gun.GunItem;

public class SniperRifleItem extends GunItem {
    private static final Vec3 BARREL = new Vec3(0, -0.125, 0);

    public SniperRifleItem(Properties settings) {
        super(settings);
    }

    @Override
    public Vec3 getBarrel(ItemStack stack) {
        var tag = stack.getTag();
        if(tag != null && tag.getBoolean("Zoom"))
            return BARREL;
        return super.getBarrel(stack);
    }

    @Override
    public Vec3 getCorrection(ItemStack stack) {
        var tag = stack.getTag();
        if(tag != null && tag.getBoolean("Zoom"))
            return Vec3.ZERO;
        return super.getCorrection(stack);
    }

    @Override
    public boolean hasZoom() {
        return true;
    }

    @Override
    protected void spawnProjectile(Level world, Vec3 barrelPos, Vec3 motion, Player user, ItemStack stack) {
        var projectileEntity = BulletEntity.create(world, barrelPos, motion, this);
        projectileEntity.setOwner(user);
        var tag = stack.getTag();
        if(tag != null && tag.getBoolean("Zoom"))
            projectileEntity.setNoGravity(true);
        world.addFreshEntity(projectileEntity);
    }

    public static boolean isZooming(Player player) {
        var stack = player.getMainHandItem();
        if(!(stack.getItem() instanceof SniperRifleItem))
            return false;
        var tag = stack.getTag();
        if(tag == null)
            return false;
        return tag.getBoolean("Zoom");
    }
}
