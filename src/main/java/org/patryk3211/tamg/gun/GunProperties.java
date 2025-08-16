package org.patryk3211.tamg.gun;

import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class GunProperties {
    public static <T extends GunItem, P> NonNullUnaryOperator<ItemBuilder<T, P>> setProperties(float damage, TagKey<Item> bulletTag) {
        return b -> b.onRegister(item -> {
            item.damage = damage;
            item.bulletTag = bulletTag;
        });
    }
}
