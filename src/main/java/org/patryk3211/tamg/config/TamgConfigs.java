package org.patryk3211.tamg.config;

import net.createmod.catnip.config.ConfigBase;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Supplier;

public class TamgConfigs {
    public static final Map<ModConfig.Type, ConfigBase> CONFIGS = new EnumMap<>(ModConfig.Type.class);

    private static CServer server;

    public static CServer server() {
        return server;
    }

    public static ConfigBase byType(ModConfig.Type type) {
        return CONFIGS.get(type);
    }

    private static <T extends ConfigBase> T register(Supplier<T> factory, ModConfig.Type side) {
        Pair<T, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(builder -> {
            T config = factory.get();
            config.registerAll(builder);
            return config;
        });

        T config = specPair.getLeft();
        config.specification = specPair.getRight();
        CONFIGS.put(side, config);
        return config;
    }

    public static void register(ModLoadingContext context) {
        server = register(CServer::new, ModConfig.Type.SERVER);

        for(Map.Entry<ModConfig.Type, ConfigBase> pair : CONFIGS.entrySet())
            context.registerConfig(pair.getKey(), pair.getValue().specification);

//        CStress stress = server().kinetics.stressValues;
//        BlockStressValues.IMPACTS.registerProvider(stress::getImpact);
//        BlockStressValues.CAPACITIES.registerProvider(stress::getCapacity);

//        ResistanceValues.register(server.electricity.resistance);
//        ThermalValues.register(server.electricity.thermal);
    }

    public static void onLoad(ModConfig modConfig) {
        for(ConfigBase config : CONFIGS.values())
            if(config.specification == modConfig.getSpec())
                config.onLoad();
    }

    public static void onReload(ModConfig modConfig) {
        for(ConfigBase config : CONFIGS.values())
            if(config.specification == modConfig.getSpec())
                config.onReload();
    }
}
