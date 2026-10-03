/*
 * Copyright (c) 2026. wuritz
 * SPDX-License-Identifier: Apache-2.0
 */

package wuritz.bcc

import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.common.Mod
import wuritz.bcc.network.IncomingConnectionNF
import wuritz.bcc.utils.Constants

@Mod(Constants.MOD_ID)
class BCCNeoForge(eventBus: IEventBus) {

    init {
        BCCCommon.init()
        Constants.LOG.info("Initializing on server-side...")

        eventBus.register(IncomingConnectionNF)

        Constants.LOG.info("Server-side initialized!")
    }

}