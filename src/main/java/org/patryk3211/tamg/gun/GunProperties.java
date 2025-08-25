package org.patryk3211.tamg.gun;

import com.simibubi.create.foundation.item.render.SimpleCustomRenderer;
import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import org.patryk3211.tamg.mixin.ItemAccessor;

import java.util.function.BiFunction;
import java.util.function.Supplier;

public class GunProperties {
    public static <T extends GunItem, P> NonNullUnaryOperator<ItemBuilder<T, P>> setProperties(float damage, float knockback, TagKey<Item> bulletTag) {
        return b -> b.onRegister(item -> {
            item.damage = damage;
            item.knockback = knockback;
            item.bulletTag = bulletTag;
        });
    }

    public static <T extends GunItem, P> NonNullUnaryOperator<ItemBuilder<T, P>> setThermalProperties(float heatCapacity, float heatPerShot, float heatDissipation) {
        return b -> b.onRegister(item -> {
            item.heatCapacity = heatCapacity;
            item.heatPerShot = heatPerShot;
            item.heatDissipation = heatDissipation;
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

    public static <T extends GunItem, P> NonNullUnaryOperator<ItemBuilder<T, P>> withAnimationData(Supplier<BiFunction<Player, InteractionHand, GunAnimationData>> animationConstructor) {
        return b -> b.onRegister(item ->
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () ->
                        GunRenderHandler.animationConstructors.put(item, animationConstructor.get())));
    }

    public static <T extends GunItem, P, R extends GunItemRenderer<?>> NonNullUnaryOperator<ItemBuilder<T, P>> withRenderer(Supplier<Supplier<R>> renderer) {
        return b -> b.onRegister(item -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            var object = SimpleCustomRenderer.create(item, renderer.get().get());
            ((ItemAccessor) item).setRenderProperties(object);
        }));
    }
}
