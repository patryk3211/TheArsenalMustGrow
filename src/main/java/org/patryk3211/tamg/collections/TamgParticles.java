package org.patryk3211.tamg.collections;

import com.simibubi.create.foundation.particle.ICustomParticleData;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.patryk3211.tamg.Tamg;
import org.patryk3211.tamg.gun.particle.GunFlashParticleOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class TamgParticles {
    private static final List<ParticleEntry<?>> all = new ArrayList<>();
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, Tamg.MOD_ID);

    public static final ParticleType<GunFlashParticleOptions> GUN_FLASH = register("gun_flash", GunFlashParticleOptions::new);

    private static <T extends ParticleOptions> ParticleType<T> register(String name, Supplier<? extends ICustomParticleData<T>> typeFactory) {
        var type = typeFactory.get().createType();
        PARTICLE_TYPES.register(name, () -> type);
        addEntry(type, typeFactory);
        return type;
    }

    public static <T extends ParticleOptions> void addEntry(ParticleType<T> type, Supplier<? extends ICustomParticleData<T>> typeFactory) {
        all.add(new ParticleEntry<>(type, typeFactory));
    }

    @SuppressWarnings("EmptyMethod")
    public static void register() { /* Initialize static fields. */ }

    @OnlyIn(Dist.CLIENT)
    public static void registerFactories(RegisterParticleProvidersEvent event) {
        for(var entry : all) {
            entry.registerFactory(event);
        }
    }

    private record ParticleEntry<T extends ParticleOptions>(ParticleType<T> type, Supplier<? extends ICustomParticleData<T>> typeFactory) {
        @OnlyIn(Dist.CLIENT)
        public void registerFactory(RegisterParticleProvidersEvent event) {
            typeFactory.get().register(type, event);
        }
    }
}
