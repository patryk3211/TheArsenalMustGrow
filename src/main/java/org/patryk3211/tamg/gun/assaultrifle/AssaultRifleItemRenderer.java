package org.patryk3211.tamg.gun.assaultrifle;

import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import org.patryk3211.tamg.collections.TamgPartialModels;
import org.patryk3211.tamg.gun.GunItemRenderer;

public class AssaultRifleItemRenderer extends GunItemRenderer<AssaultRifleAnimationData> {
    public AssaultRifleItemRenderer() {
        super(AssaultRifleAnimationData.class);
    }

    @Override
    protected PartialModel flash() {
        return TamgPartialModels.PISTOL_FLASH;
    }
}
