package foundry.alembic.types.tag;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.mojang.serialization.Codec;
import foundry.alembic.Alembic;
import foundry.alembic.util.CodecUtil;
import net.minecraft.resources.ResourceLocation;

import java.util.Collections;
import java.util.Set;

public class AlembicTagRegistry<T extends AlembicTag> {
    private static final BiMap<ResourceLocation, AlembicTagType<?>> TAGS = HashBiMap.create();

    public static final Codec<AlembicTagType<?>> TAG_MAP_CODEC = CodecUtil.ALEMBIC_RL_CODEC.xmap(TAGS::get, TAGS.inverse()::get);

    public static void init(){
        AlembicTagType.bootstrap();
    }

    public static void register(ResourceLocation name, AlembicTagType<?> type) {
        TAGS.put(name, type);
    }

    public static boolean isRegistered(String name) {
        ResourceLocation id = name.contains(":") ? new ResourceLocation(name) : Alembic.location(name);
        return TAGS.containsKey(id);
    }

    public static Set<ResourceLocation> getRegisteredTags() {
        return Collections.unmodifiableSet(TAGS.keySet());
    }
}
