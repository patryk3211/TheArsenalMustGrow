/*
 * Copyright 2025 patryk3211
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.patryk3211.tamg.collections;

import com.google.gson.JsonObject;
import com.simibubi.create.AllSoundEvents;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import org.jetbrains.annotations.NotNull;
import org.patryk3211.tamg.Tamg;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class TamgSoundEvents {
    public static final Map<ResourceLocation, AllSoundEvents.SoundEntry> ALL = new HashMap<>();

    public static final AllSoundEvents.SoundEntry

    GUN_SHOT = create("gun_shot").subtitle("Gun Shot")
            .category(SoundSource.PLAYERS)
            .build();
            ;

    private static SoundEntryBuilder create(String name) {
        return create(Tamg.asResource(name));
    }

    public static SoundEntryBuilder create(ResourceLocation id) {
        return new SoundEntryBuilder(id);
    }

    public static void prepare() {
        for(AllSoundEvents.SoundEntry entry : ALL.values())
            entry.prepare();
    }

    public static void provideLang(BiConsumer<String, String> consumer) {
        for(AllSoundEvents.SoundEntry entry : ALL.values())
            if(entry.hasSubtitle())
                consumer.accept(entry.getSubtitleKey(), entry.getSubtitle());
    }

    public static DataProvider provider(PackOutput output) {
        return new SoundEntryProvider(output);
    }

    private static class SoundEntryProvider implements DataProvider {
        private final PackOutput output;

        public SoundEntryProvider(PackOutput output) {
            this.output = output;
        }

        @NotNull
        @Override
        public CompletableFuture<?> run(@NotNull CachedOutput cache) {
            return generate(output.getOutputFolder(), cache);
        }

        @NotNull
        @Override
        public String getName() {
            return "TAMG's Custom Sounds";
        }

        public CompletableFuture<?> generate(Path path, CachedOutput cache) {
            path = path.resolve("assets/tamg");
            JsonObject json = new JsonObject();
            ALL.entrySet()
                    .stream()
                    .sorted(Map.Entry.comparingByKey())
                    .forEach(entry -> entry.getValue().write(json));
            return DataProvider.saveStable(cache, json, path.resolve("sounds.json"));
        }
    }

    public static class SoundEntryBuilder extends AllSoundEvents.SoundEntryBuilder {
        public SoundEntryBuilder(ResourceLocation id) {
            super(id);
        }

        @Override
        public AllSoundEvents.SoundEntryBuilder addVariant(String name) {
            return addVariant(Tamg.asResource(name));
        }

        @Override
        public AllSoundEvents.SoundEntry build() {
            var entry = super.build();
            ALL.put(entry.getId(), entry);
            return entry;
        }
    }
}
