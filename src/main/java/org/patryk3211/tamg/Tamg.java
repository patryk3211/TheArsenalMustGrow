package org.patryk3211.tamg;

import com.mojang.logging.LogUtils;
import com.simibubi.create.foundation.data.CreateRegistrate;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.patryk3211.tamg.collections.TamgEntities;
import org.patryk3211.tamg.collections.TamgItems;
import org.patryk3211.tamg.data.CuttingRecipes;
import org.patryk3211.tamg.data.PressingRecipes;
import org.patryk3211.tamg.data.SequencedAssemblyRecipes;
import org.slf4j.Logger;

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
                .defaultCreativeTab("the_arsenal_must_grow", b -> b
                        .title(Component.translatable("itemGroup.tamg.main")))
                .build();
        REGISTRATE.registerEventListeners(modEventBus);

        TamgItems.register();
        TamgEntities.register();

        Networking.init();

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

        generator.addProvider(true, new SequencedAssemblyRecipes(pack));
        generator.addProvider(true, new CuttingRecipes(pack));
        generator.addProvider(true, new PressingRecipes(pack));
    }
}
