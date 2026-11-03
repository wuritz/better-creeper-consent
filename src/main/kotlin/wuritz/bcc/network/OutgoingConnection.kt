/*
 * Copyright (c) 2026. wuritz
 * SPDX-License-Identifier: Apache-2.0
 */

package wuritz.bcc.network

import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.monster.Creeper
import net.minecraft.world.phys.AABB
import wuritz.bcc.platform.Services
import wuritz.bcc.utils.Constants

object OutgoingConnection {

    fun triggerConsent(creeper: Creeper) {
        if (creeper.level() !is ServerLevel) return
        val level = creeper.level() as ServerLevel

        val explosion = AABB(creeper.blockPosition()).inflate(10f.toDouble())

        val nearbyPlayers = level.getPlayers { player ->
            player.boundingBox.intersects(explosion)
        }
        if (nearbyPlayers.isEmpty()) return
        val nearestPlayer = nearbyPlayers[0]

        if (!CreeperQueue.markPending(creeper.uuid, nearestPlayer.uuid)) return

        creeper.swellDir = -1

        //Constants.LOG.info("Sending consent screen to {} for creeper {}", nearestPlayer.name, creeper.uuid)
        sendConsentScreen(nearbyPlayers[0], creeper)
    }

    private fun sendConsentScreen(player: ServerPlayer, creeper: Creeper) {
        creeper.swellDir = -1

        Services.SEND_CONSENT_SCREEN.sendConsentScreen(player, creeper)
        //Constants.LOG.info("Sent consent screen to {} for creeper {} at {}", player.name, creeper.id, player.blockPosition())
    }

}