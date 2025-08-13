package org.patryk3211.tamg.gun;

import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;

public class GunProperties {
    public static <T extends GunItem, P> NonNullUnaryOperator<ItemBuilder<T, P>> setDamage(float damage) {
        return b -> b.onRegister(item -> item.damage = damage);
    }
}
