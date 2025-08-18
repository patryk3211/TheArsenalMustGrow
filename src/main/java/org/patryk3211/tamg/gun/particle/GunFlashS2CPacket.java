package org.patryk3211.tamg.gun.particle;

import com.simibubi.create.foundation.networking.SimplePacketBase;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;
import org.joml.Vector3f;

public class GunFlashS2CPacket extends SimplePacketBase {
    private final Vector3f location;
    private final Vector3f heading;

    public GunFlashS2CPacket(Vec3 location, Vec3 heading) {
        this.location = location.toVector3f();
        this.heading = heading.toVector3f();
    }

    public GunFlashS2CPacket(FriendlyByteBuf buffer) {
        location = buffer.readVector3f();
        heading = buffer.readVector3f();
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeVector3f(location);
        buffer.writeVector3f(heading);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> {
            var level = Minecraft.getInstance().level;
            level.addAlwaysVisibleParticle(GunFlashParticleOptions.INSTANCE, location.x, location.y, location.z, heading.x, heading.y, heading.z);
        });
        return true;
    }
}
