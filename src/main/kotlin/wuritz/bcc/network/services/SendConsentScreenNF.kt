/*
 * Copyright (c) 2026. wuritz
 * SPDX-License-Identifier: Apache-2.0
 */

package wuritz.bcc.network.services

import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.monster.Creeper
import net.neoforged.neoforge.network.PacketDistributor
import wuritz.bcc.network.payloads.outgoing.OpenConsentPayload
import wuritz.bcc.platform.interfaces.SendConsentScreen

class SendConsentScreenNF : SendConsentScreen {

    override fun sendConsentScreen(
        player: ServerPlayer,
        creeper: Creeper
    ) {
        PacketDistributor.sendToPlayer(player, OpenConsentPayload(creeper.id))
    }

}