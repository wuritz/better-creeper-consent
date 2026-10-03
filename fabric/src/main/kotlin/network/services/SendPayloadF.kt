/*
 * Copyright (c) 2026. wuritz
 * SPDX-License-Identifier: Apache-2.0
 */

package wuritz.bcc.network.services

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import wuritz.bcc.platform.interfaces.SendPayload

class SendPayloadF : SendPayload {

    override fun sendPayload(payload: CustomPacketPayload) {
        ClientPlayNetworking.send(payload)
    }

}