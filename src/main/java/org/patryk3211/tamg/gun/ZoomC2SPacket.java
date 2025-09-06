package org.patryk3211.tamg.gun;

import com.simibubi.create.foundation.networking.SimplePacketBase;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraftforge.network.NetworkEvent;
import org.patryk3211.tamg.gun.sniper.SniperRifleItem;

public class ZoomC2SPacket extends SimplePacketBase {
    private final boolean zoom;

    public ZoomC2SPacket(boolean zoom) {
        this.zoom = zoom;
    }

    public ZoomC2SPacket(FriendlyByteBuf buffer) {
        zoom = buffer.readBoolean();
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeBoolean(zoom);
    }

    @Override
    public boolean handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> {
            var stack = context.getSender().getItemInHand(InteractionHand.MAIN_HAND);
            if(stack.getItem() instanceof SniperRifleItem) {
                var tag = stack.getOrCreateTag();
                if(!zoom && tag.contains("Zoom")) {
                    tag.remove("Zoom");
                } else if(zoom && !tag.getBoolean("Zoom")) {
                    tag.putBoolean("Zoom", true);
                }
            }
        });
        return true;
    }
}
