/*
 * Copyright (c) 2026. wuritz
 * SPDX-License-Identifier: Apache-2.0
 */

package wuritz.bcc.network.handling

import net.minecraft.core.particles.ParticleTypes
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.monster.Creeper
import wuritz.bcc.network.CreeperQueue
import wuritz.bcc.utils.Constants
import wuritz.bcc.utils.LuckyAction
import wuritz.bcc.utils.MessageSender

object ClientPayloadHandler {

    fun handleLucky(player: ServerPlayer, creeperId: Int) {
        val world = player.level()

        val creeper = world.getEntity(creeperId)
        if (creeper !is Creeper) return
        val pos = creeper.blockPosition()

        val lucky = LuckyAction(pos, world, player)
        lucky.run()
    }

    fun handleResponse(player: ServerPlayer, creeperId: Int, allowed: Boolean, playerInitialized: Boolean) {
        val world = player.level()
        val creeper = world.getEntity(creeperId)

        if (creeper !is Creeper) return Constants.LOG.error("{} sent a consent to a non-creeper entity: id {}", player.name.string, creeperId)
        val creeperUuid = creeper.uuid

        val distance = player.distanceTo(creeper)
        if (distance > 10f) {
            Constants.LOG.error("{} sent a response, but is now out of the creeper's (id {}) radius.", player.name.string, creeperId)
            creeper.discard()
            CreeperQueue.clearEntry(creeperUuid)
            return
        }

        if (allowed) {
            Constants.LOG.info("{} allowed creeper id {} to explode", player.name.string, creeperId)

            CreeperQueue.approve(creeperUuid)
            creeper.swellDir = 1 // normal behaviour
            creeper.ignite()

            MessageSender.sendAllowMsg(player)
        } else {
            Constants.LOG.info("{} denied creeper id {}", player.name.string, creeperId)

            creeper.discard()
            CreeperQueue.clearEntry(creeperUuid)

            val pos = creeper.position()
            world.sendParticles(
                ParticleTypes.POOF,
                pos.x, pos.y + 1, pos.z,
                10,
                0.1, 0.1, 0.1,
                0.02
            )

            if (playerInitialized) MessageSender.sendDenyMsg(player)
            //MessageSender.sendDenyMsg(player)
        }
    }

}