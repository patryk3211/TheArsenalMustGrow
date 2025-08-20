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

import com.simibubi.create.content.equipment.zapper.ShootGadgetPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.patryk3211.tamg.Tamg;
import org.patryk3211.tamg.TamgClient;

public class GunS2CPacket extends ShootGadgetPacket {
    private Player shooter;

    public GunS2CPacket(Vec3 location, InteractionHand hand, boolean self, Player shooter) {
        super(location, hand, self);
        this.shooter = shooter;
    }

    public GunS2CPacket(FriendlyByteBuf buffer) {
        super(buffer);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    protected void readAdditional(FriendlyByteBuf buffer) {
        var level = Minecraft.getInstance().level;
        if(level.getEntity(buffer.readInt()) instanceof Player player) {
            this.shooter = player;
        } else {
            Tamg.LOGGER.warn("Received shoot packet with invalid shooter");
        }
    }

    @Override
    protected void writeAdditional(FriendlyByteBuf buffer) {
        buffer.writeInt(shooter.getId());
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    protected void handleAdditional() {
        if(shooter == null)
            return;
        TamgClient.GUN_RENDER_HANDLER.beforeShoot(shooter, hand);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    protected GunRenderHandler getHandler() {
        return TamgClient.GUN_RENDER_HANDLER;
    }
}
