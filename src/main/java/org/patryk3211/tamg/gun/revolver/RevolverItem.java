package org.patryk3211.tamg.gun.revolver;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.patryk3211.tamg.gun.GunItem;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class RevolverItem extends GunItem {
    public RevolverItem(Properties settings) {
        super(settings);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        var result = super.use(world, user, hand);
        if(!world.isClientSide && result.getResult().consumesAction()) {
            // Knockback user
            var lookVec = user.getLookAngle();
            var motion = lookVec
                    .multiply(1, 0, 1)
                    .normalize()
                    .scale(-0.3f);
            user.addDeltaMovement(motion.add(0, 0.15, 0));
            user.hurtMarked = true;
        }
        return result;
    }
}
