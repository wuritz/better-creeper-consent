/*
 * Copyright (c) 2026. wuritz
 * SPDX-License-Identifier: Apache-2.0
 */

package wuritz.bcc.platform.interfaces

import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.monster.Creeper

interface SendConsentScreen {

    fun sendConsentScreen(player: ServerPlayer, creeper: Creeper)

}