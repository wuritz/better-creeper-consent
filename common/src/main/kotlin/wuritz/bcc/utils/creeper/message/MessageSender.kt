/*
 * Copyright (c) 2026. wuritz
 * SPDX-License-Identifier: Apache-2.0
 */

package wuritz.bcc.utils.creeper.message

import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.level.LightLayer
import net.minecraft.world.level.biome.Biomes
import net.minecraft.world.level.levelgen.Heightmap

object MessageSender {

    val prefix = Component.literal("<Creeper> ")
        .withStyle(ChatFormatting.WHITE)

    fun sendAllowMsg(player: ServerPlayer) {
        val output = Component.literal("")
        output.append(prefix)

        player.sendSystemMessage(
            output.append(
                Component.literal(
                    if (isInsideCave(player)) CreeperMessages.randomCaveAllowMessage() else CreeperMessages.randomAllowMessage())
                    .withStyle(ChatFormatting.GREEN)
            )
        )
    }

    fun sendDenyMsg(player: ServerPlayer) {
        val output = Component.literal("")
        output.append(prefix)

        player.sendSystemMessage(
            output.append(
                Component.literal(
                    if (isInsideCave(player)) CreeperMessages.randomCaveDenyMessage() else CreeperMessages.randomDenyMessage())
                    .withStyle(ChatFormatting.GREEN)
            )
        )
    }

    fun sendTntMsg(player: ServerPlayer) {
        val output = Component.literal("")
        output.append(prefix)

        player.sendSystemMessage(
            output.append(
                Component.literal(
                    "Here's a gift for you :3")
                    .withStyle(ChatFormatting.RED)
            )
        )
    }

    private fun isInsideCave(player: ServerPlayer): Boolean {
        val biomeHolder = player.level().getBiome(player.blockPosition())

        if (biomeHolder.`is`(Biomes.LUSH_CAVES) || biomeHolder.`is`(Biomes.DRIPSTONE_CAVES) || biomeHolder.`is`(Biomes.SULFUR_CAVES))
            return true
        else {
            val skyLight = player.level().getBrightness(LightLayer.SKY, player.blockPosition())
            val surfaceY = player.level().getHeight(Heightmap.Types.WORLD_SURFACE, player.blockPosition().x, player.blockPosition().z)

            return skyLight == 0 && player.blockPosition().y < (surfaceY - 10)
        }
    }
}