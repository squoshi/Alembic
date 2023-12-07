package foundry.alembic.networking;

import foundry.alembic.Alembic;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class AlembicPacketHandler {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            Alembic.location("main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );
    private static int id = 0;

    public static void init() {
        INSTANCE.messageBuilder(ClientboundAlembicDamagePacket.class, id++)
                .encoder(ClientboundAlembicDamagePacket::encode)
                .decoder(ClientboundAlembicDamagePacket::decode)
                .consumerMainThread(ClientboundAlembicDamagePacket::handle)
                .add();
        INSTANCE.messageBuilder(ClientboundAlembicFireTypePacket.class, id++)
                .encoder(ClientboundAlembicFireTypePacket::encode)
                .decoder(ClientboundAlembicFireTypePacket::decode)
                .consumerMainThread(ClientboundAlembicFireTypePacket::handle)
                .add();
    }

    public static void sendFirePacket(Entity entity, String type) {
        AlembicPacketHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), new ClientboundAlembicFireTypePacket(type, entity.getId()));
    }
}
