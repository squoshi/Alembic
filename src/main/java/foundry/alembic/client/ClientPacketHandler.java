package foundry.alembic.client;

import foundry.alembic.caps.AlembicFlammableHandler;
import foundry.alembic.compat.TESCompat;
import foundry.alembic.networking.ClientboundAlembicDamagePacket;
import foundry.alembic.networking.ClientboundAlembicFireTypePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientPacketHandler {
    public static void handleDamagePacket(ClientboundAlembicDamagePacket msg, Supplier<NetworkEvent.Context> ctx) {
        if(ModList.get().isLoaded("tslatentitystatus")){
            try{
                TESCompat.spawnParticle(Minecraft.getInstance().level, msg.entityID, msg.damageType, msg.damageAmount, msg.color);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public static void handleFireTypePacket(ClientboundAlembicFireTypePacket msg) {
        Level level = Minecraft.getInstance().level;
        if(level == null) return;
        Entity entity = level.getEntity(msg.fireType.getInt("entityID"));
        if(entity != null){
            entity.getCapability(AlembicFlammableHandler.CAPABILITY, null).ifPresent((handler) -> {
                handler.setFireType(msg.fireType.getString("fireType"));
            });
        }
    }
}
