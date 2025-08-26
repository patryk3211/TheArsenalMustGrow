package org.patryk3211.tamg.config;

import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.createmod.catnip.config.ConfigBase;
import net.createmod.catnip.platform.CatnipServices;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import org.patryk3211.tamg.Tamg;
import org.patryk3211.tamg.gun.GunItem;

import java.util.HashMap;
import java.util.Map;

public class CGuns extends ConfigBase {
    private static final int VERSION = 1;

    private static final Object2DoubleMap<ResourceLocation> DEFAULT_DOUBLES = new Object2DoubleOpenHashMap<>();
    private static final Object2IntMap<ResourceLocation> DEFAULT_INTS = new Object2IntOpenHashMap<>();

    protected final Map<ResourceLocation, ForgeConfigSpec.ConfigValue<?>> values = new HashMap<>();

    @Override
    public void registerAll(ForgeConfigSpec.Builder builder) {
        builder.comment(".")
                .push("guns");
        DEFAULT_DOUBLES.forEach((id, value) -> this.values.put(id, builder.define(id.getPath(), value)));
        DEFAULT_INTS.forEach((id, value) -> this.values.put(id, builder.define(id.getPath(), value)));
        builder.pop();
    }

    @Override
    public String getName() {
        return "guns-v" + VERSION;
    }

    public double getDouble(GunItem item, GunProperties property) {
        return ((Number) values.get(property.makeKey(item)).get()).doubleValue();
    }

    public int getInt(GunItem item, GunProperties property) {
        return ((Number) values.get(property.makeKey(item)).get()).intValue();
    }

    public static <T extends GunItem, P> NonNullUnaryOperator<ItemBuilder<T, P>> base(double damage, double knockback, double heatCapacity, double heatPerShot, double heatDissipation, double selfKnockback, int useCooldown) {
        return b -> {
            ResourceLocation id = Tamg.asResource(b.getName());
            DEFAULT_DOUBLES.put(GunProperties.DAMAGE.makeKey(id), damage);
            DEFAULT_DOUBLES.put(GunProperties.KNOCKBACK.makeKey(id), knockback);
            DEFAULT_DOUBLES.put(GunProperties.HEAT_CAPACITY.makeKey(id), heatCapacity);
            DEFAULT_DOUBLES.put(GunProperties.HEAT_PER_SHOT.makeKey(id), heatPerShot);
            DEFAULT_DOUBLES.put(GunProperties.HEAT_DISSIPATION.makeKey(id), heatDissipation);
            DEFAULT_DOUBLES.put(GunProperties.SELF_KNOCKBACK.makeKey(id), selfKnockback);
            DEFAULT_INTS.put(GunProperties.USE_COOLDOWN.makeKey(id), useCooldown);
            return b;
        };
    }

    public enum GunProperties {
        DAMAGE("damage"),
        KNOCKBACK("knockback"),
        HEAT_CAPACITY("heat_capacity"),
        HEAT_PER_SHOT("heat_per_shot"),
        HEAT_DISSIPATION("heat_dissipation"),
        SELF_KNOCKBACK("self_knockback"),
        USE_COOLDOWN("use_cooldown")
        ;

        public final String key;

        GunProperties(String key) {
            this.key = key;
        }

        public ResourceLocation makeKey(GunItem forItem) {
            var itemId = CatnipServices.REGISTRIES.getKeyOrThrow(forItem);
            return makeKey(itemId);
        }

        public ResourceLocation makeKey(ResourceLocation id) {
            return id.withSuffix("." + key);
        }
    }
}
