package org.patryk3211.tamg.gun.revolver;

import net.createmod.catnip.animation.LerpedFloat;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import org.patryk3211.tamg.gun.GunAnimationData;

public class RevolverAnimationData extends GunAnimationData {
    private static final RandomSource r = RandomSource.create();

    private LerpedFloat hammer;
    private LerpedFloat drum;

    public RevolverAnimationData(Player player, InteractionHand hand) {
        super(player, hand);
    }

    @Override
    public void reset() {
        super.reset();
        if(hammer == null) {
            hammer = LerpedFloat.linear();
            drum = LerpedFloat.linear();
        }
        hammer.setValue(0);
        hammer.chaseTimed(1, 4);
        drum.setValue(0);
        drum.chaseTimed(1, 6);
    }

    @Override
    public void tick() {
        super.tick();
        hammer.tickChaser();
        drum.tickChaser();
    }

    public float hammer(float pt) {
        var value = hammer.getValue(pt);
        return peakAt(0.125f, value) * (float) Math.PI * -0.2f;
    }

    public float drum(float pt) {
        return drum.getValue(pt) * (float) Math.PI * 0.5f;
    }

    public boolean isDone() {
        return super.isDone() && hammer.settled() && drum.settled();
    }
}
