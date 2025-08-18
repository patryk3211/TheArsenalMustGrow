package org.patryk3211.tamg.gun.particle;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.simibubi.create.foundation.particle.ICustomParticleDataWithSprite;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.registries.ForgeRegistries;
import org.patryk3211.tamg.collections.TamgParticles;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class GunFlashParticleOptions implements ParticleOptions, ICustomParticleDataWithSprite<GunFlashParticleOptions> {
    public static final GunFlashParticleOptions INSTANCE = new GunFlashParticleOptions();
    public static final Codec<GunFlashParticleOptions> CODEC = Codec.unit(INSTANCE);
    public static final Deserializer<GunFlashParticleOptions> DESERIALIZER = new Deserializer<>() {
        @Override
        public GunFlashParticleOptions fromCommand(ParticleType<GunFlashParticleOptions> pParticleType, StringReader pReader) throws CommandSyntaxException {
            return INSTANCE;
        }

        @Override
        public GunFlashParticleOptions fromNetwork(ParticleType<GunFlashParticleOptions> pParticleType, FriendlyByteBuf pBuffer) {
            return INSTANCE;
        }
    };

    @Override
    public ParticleType<?> getType() {
        return TamgParticles.GUN_FLASH;
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buffer) {

    }

    @Override
    public String writeToString() {
        return ForgeRegistries.PARTICLE_TYPES.getKey(getType()).toString();
    }

    @Override
    public Deserializer<GunFlashParticleOptions> getDeserializer() {
        return DESERIALIZER;
    }

    @Override
    public Codec<GunFlashParticleOptions> getCodec(ParticleType<GunFlashParticleOptions> type) {
        return CODEC;
    }

    @Override
    public ParticleEngine.SpriteParticleRegistration<GunFlashParticleOptions> getMetaFactory() {
        return new GunFlashParticle.Factory();
    }
}
