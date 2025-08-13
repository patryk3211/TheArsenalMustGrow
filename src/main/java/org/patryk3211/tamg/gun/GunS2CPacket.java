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
package org.patryk3211.tamg.gun;

import com.simibubi.create.content.equipment.potatoCannon.PotatoCannonPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.patryk3211.tamg.TamgClient;

public class GunS2CPacket extends PotatoCannonPacket {
    public GunS2CPacket(Vec3 location, Vec3 motion, ItemStack item, InteractionHand hand, float pitch, boolean self) {
        super(location, motion, item, hand, pitch, self);
    }

    public GunS2CPacket(FriendlyByteBuf buffer) {
        super(buffer);
    }

    @Override
    protected void handleAdditional() {

    }

    @Override
    @OnlyIn(Dist.CLIENT)
    protected GunRenderHandler getHandler() {
        return TamgClient.GUN_RENDER_HANDLER;
    }
}
