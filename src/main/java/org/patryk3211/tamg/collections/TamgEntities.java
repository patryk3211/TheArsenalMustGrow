package org.patryk3211.tamg.collections;

import com.tterrag.registrate.util.entry.EntityEntry;
import net.minecraft.world.entity.MobCategory;
import org.patryk3211.tamg.gun.BulletEntity;
import org.patryk3211.tamg.gun.BulletRenderer;

import static org.patryk3211.tamg.Tamg.REGISTRATE;

public class TamgEntities {
    public static final EntityEntry<BulletEntity> BULLET = REGISTRATE.entity("bullet", BulletEntity::new, MobCategory.MISC)
            .properties(b -> b
                    .setTrackingRange(4)
                    .clientTrackingRange(4)
                    .setUpdateInterval(20)
                    .sized(0.25f, 0.25f)
                    .setShouldReceiveVelocityUpdates(true)
            )
            .renderer(() -> BulletRenderer::new)
            .register();

    public static void register() { /* Initialize static fields */ }
}
