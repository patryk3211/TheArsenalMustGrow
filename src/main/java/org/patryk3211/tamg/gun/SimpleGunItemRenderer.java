package org.patryk3211.tamg.gun;

import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import org.patryk3211.tamg.collections.TamgPartialModels;

public class SimpleGunItemRenderer extends GunItemRenderer<EmptyAnimationData> {
    public SimpleGunItemRenderer() {
        super(EmptyAnimationData.class);
    }

    @Override
    protected PartialModel flash() {
        return TamgPartialModels.PISTOL_FLASH;
    }
}
