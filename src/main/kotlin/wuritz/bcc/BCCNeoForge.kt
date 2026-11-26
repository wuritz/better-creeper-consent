/*
 * Copyright (c) 2026. wuritz
 * SPDX-License-Identifier: Apache-2.0
 */

package wuritz.bcc

import net.minecraft.world.entity.monster.Creeper
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.common.Mod
import net.neoforged.neoforge.common.NeoForge
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent
import wuritz.bcc.network.CreeperQueue
import wuritz.bcc.network.IncomingConnectionNF
import wuritz.bcc.utils.Constants

@Mod(Constants.MOD_ID)
class BCCNeoForge(eventBus: IEventBus) {

    init {
        BCCCommon.init()
        Constants.LOG.info("Initializing on server-side...")

        eventBus.register(IncomingConnectionNF)
        NeoForge.EVENT_BUS.addListener { event: LivingDeathEvent ->
            val entity = event.entity
            if (entity is Creeper) CreeperQueue.clearEntry(entity.uuid)
        }

        Constants.LOG.info("Server-side initialized!")
    }

}
