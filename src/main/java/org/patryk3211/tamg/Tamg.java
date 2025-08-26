package org.patryk3211.tamg;

import com.mojang.logging.LogUtils;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.utility.FilesHelper;
import com.tterrag.registrate.providers.ProviderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.patryk3211.tamg.collections.TamgEntities;
import org.patryk3211.tamg.collections.TamgItems;
import org.patryk3211.tamg.collections.TamgParticles;
import org.patryk3211.tamg.collections.TamgSoundEvents;
import org.patryk3211.tamg.config.TamgConfigs;
import org.patryk3211.tamg.data.CuttingRecipes;
import org.patryk3211.tamg.data.CompactingRecipes;
import org.patryk3211.tamg.data.SequencedAssemblyRecipes;
import org.slf4j.Logger;

import java.util.function.BiConsumer;

@Mod(Tamg.MOD_ID)
public class Tamg  {
    public static final String MOD_ID = "tamg";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static CreateRegistrate REGISTRATE;
    public static IEventBus modEventBus;

    public Tamg(FMLJavaModLoadingContext context) {
        modEventBus = context.getModEventBus();
        modEventBus.register(Tamg.class);
        LOGGER.info("Create: The Arsenal Must Grow is loading");

        REGISTRATE = CreateRegistrate.create(MOD_ID)
                .defaultCreativeTab("the_arsenal_must_grow")
                .build();
        REGISTRATE.registerEventListeners(modEventBus);

        TamgSoundEvents.prepare();

        TamgItems.register();
        TamgEntities.register();
        TamgParticles.register();

        Networking.init();

        TamgParticles.PARTICLE_TYPES.register(modEventBus);

        TamgConfigs.register(context);

        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> TamgClient::init);
    }

    public static ResourceLocation asResource(String path) {
        return ResourceLocation.tryBuild(MOD_ID, path);
    }

    public static ResourceLocation texture(String path) {
        return asResource("textures/" + path + ".png");
    }

    @SubscribeEvent
    public static void gatherData(final GatherDataEvent event) {
        var generator = event.getGenerator();
        var pack = generator.getPackOutput();

        REGISTRATE.addDataGenerator(ProviderType.LANG, provider -> {
            BiConsumer<String, String> langConsumer = provider::add;
            TamgSoundEvents.provideLang(langConsumer);

            provideDefaultLang("tooltips", langConsumer);
        });

        generator.addProvider(true, new SequencedAssemblyRecipes(pack));
        generator.addProvider(true, new CuttingRecipes(pack));
        generator.addProvider(true, new CompactingRecipes(pack));
        generator.addProvider(true, TamgSoundEvents.provider(pack));
    }

    @SubscribeEvent
    public static void configLoad(ModConfigEvent.Loading event) {
        TamgConfigs.onLoad(event.getConfig());
    }

    @SubscribeEvent
    public static void configReload(ModConfigEvent.Reloading event) {
        TamgConfigs.onReload(event.getConfig());
    }

    private static void provideDefaultLang(String fileName, BiConsumer<String, String> consumer) {
        var path = "assets/tamg/lang/default/" + fileName + ".json";
        var jsonElement = FilesHelper.loadJsonResource(path);
        if (jsonElement == null) {
            throw new IllegalStateException(String.format("Could not find default lang file: %s", path));
        }
        var jsonObject = jsonElement.getAsJsonObject();
        for(var entry : jsonObject.entrySet()) {
            var key = entry.getKey();
            var value = entry.getValue().getAsString();
            consumer.accept(key, value);
        }
    }
}
