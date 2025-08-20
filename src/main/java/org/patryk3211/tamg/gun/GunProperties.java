package org.patryk3211.tamg.gun;

import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.phys.Vec3;

public class GunProperties {
    public static <T extends GunItem, P> NonNullUnaryOperator<ItemBuilder<T, P>> setProperties(float damage, float knockback, TagKey<Item> bulletTag) {
        return b -> b.onRegister(item -> {
            item.damage = damage;
            item.knockback = knockback;
            item.bulletTag = bulletTag;
        });
    }

    public static <T extends GunItem, P> NonNullUnaryOperator<ItemBuilder<T, P>> setShootVectors(double barrelX, double barrelY, double barrelZ, double correctionX, double correctionY, double correctionZ) {
        return b -> b.onRegister(item -> {
            item.barrel = new Vec3(barrelX, barrelY, barrelZ);
            item.correction = new Vec3(correctionX, correctionY, correctionZ);
        });
    }

    public static <T extends GunItem, P> NonNullUnaryOperator<ItemBuilder<T, P>> setFlashOffset(double x, double y, double z) {
        return b -> b.onRegister(item -> {
            item.flashOffset = new Vec3(x, y, z);
        });
    }
}
