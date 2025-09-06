package org.patryk3211.tamg.gun.sniper;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.patryk3211.tamg.Networking;
import org.patryk3211.tamg.gun.GunItem;
import org.patryk3211.tamg.gun.ZoomC2SPacket;

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
    public InteractionResult onLeftClick(Player player, ItemStack stack, InteractionHand hand) {
        if(hand != InteractionHand.MAIN_HAND)
            return InteractionResult.PASS;
        var tag = stack.getOrCreateTag();
        if(tag.contains("Zoom")) {
            tag.remove("Zoom");
        } else {
            tag.putBoolean("Zoom", true);
        }
        Networking.getChannel().sendToServer(new ZoomC2SPacket(tag.getBoolean("Zoom")));
        return InteractionResult.CONSUME;
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
