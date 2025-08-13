package org.patryk3211.tamg;

import com.simibubi.create.foundation.networking.SimplePacketBase;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import org.patryk3211.tamg.gun.GunS2CPacket;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class Networking {
    public static final ResourceLocation CHANNEL_NAME = Tamg.asResource("main");
    public static final int NETWORK_VERSION = 1;
    public static final String NETWORK_VERSION_STR = String.valueOf(NETWORK_VERSION);
    private static int packetIndex = 0;

    private static SimpleChannel channel;


    private static <T extends SimplePacketBase> void simpleHandler(T packet, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (packet.handle(context)) {
            context.setPacketHandled(true);
        }
    }

    private static <T extends SimplePacketBase> void registerPacket(Class<T> clazz, Function<FriendlyByteBuf, T> decoder, NetworkDirection direction) {
        channel.messageBuilder(clazz, packetIndex++, direction)
                .encoder(SimplePacketBase::write)
                .decoder(decoder)
                .consumerNetworkThread((BiConsumer<T, Supplier<NetworkEvent.Context>>) Networking::simpleHandler);
    }

    public static void init() {
        channel = NetworkRegistry.ChannelBuilder.named(CHANNEL_NAME)
                .clientAcceptedVersions(NETWORK_VERSION_STR::equals)
                .serverAcceptedVersions(NETWORK_VERSION_STR::equals)
                .networkProtocolVersion(() -> NETWORK_VERSION_STR)
                .simpleChannel();

        registerPacket(GunS2CPacket.class, GunS2CPacket::new, NetworkDirection.PLAY_TO_CLIENT);
    }

    public static SimpleChannel getChannel() {
        return channel;
    }
}
