package org.patryk3211.tamg.gun.pistol;

import net.createmod.catnip.animation.LerpedFloat;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.patryk3211.tamg.gun.GunAnimationData;

public class PistolAnimationData extends GunAnimationData {
    private static final RandomSource r = RandomSource.create();

    private LerpedFloat l;
    private float prevTop;
    private float topOffset;

    public PistolAnimationData(Player player, InteractionHand hand) {
        super(player, hand);
    }

    @Override
    public void reset() {
        super.reset();
        prevTop = 0.0f;
        topOffset = 0.0f;
    }

    @Override
    public void tick() {
        super.tick();

        prevTop = topOffset;
        if (topOffset < 1.0f) {
            topOffset += 0.4f;
        }
    }

    public float top(float pt) {
        var value = Mth.clamp(Mth.lerp(pt, prevTop, topOffset), 0, 1);
        if (value < 0.125f) {
            value = 8 * value;
        } else {
            value = -8 * (value - 1) / 7;
        }
        return value * 2 / 16f;
    }

    public boolean isDone() {
        return super.isDone() && topOffset >= 1.0f;
    }
}
