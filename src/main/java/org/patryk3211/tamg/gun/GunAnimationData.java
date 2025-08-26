package org.patryk3211.tamg.gun;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public abstract class GunAnimationData {
    private static final RandomSource r = RandomSource.create();

    public final Player player;
    public final InteractionHand hand;
    private ItemStack old;

    private float prevFlash;
    private float flash;
    public float flashAngle;

    public GunAnimationData(Player player, InteractionHand hand) {
        this.player = player;
        this.hand = hand;
        this.old = player.getItemInHand(hand);
        reset();
    }

    public boolean isFor(ItemStack stack) {
        if (old == stack)
            return true;
        var newStack = player.getItemInHand(hand);
        if(!newStack.is(old.getItem()))
            return false;
        old = newStack;
        return newStack == stack;
    }

    public void reset() {
        flash = 1.0f;
        prevFlash = 1.0f;
        flashAngle = (float) (r.nextFloat() * Math.PI * 2f);
    }

    public void tick() {
        prevFlash = flash;
        flash *= 0.5f;
    }

    public float flash(float pt) {
        return Mth.lerp(pt, prevFlash, flash);
    }

    public boolean isDone() {
        return flash < 0.05f;
    }

    public static float peakAt(float peak, float value) {
        if(value < peak) {
            return value / peak;
        } else {
            return (1 - value) / (1 - peak);
        }
    }
}
