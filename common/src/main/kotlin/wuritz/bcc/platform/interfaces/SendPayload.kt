/*
 * Copyright (c) 2026. wuritz
 * SPDX-License-Identifier: Apache-2.0
 */

package wuritz.bcc.platform.interfaces

import net.minecraft.network.protocol.common.custom.CustomPacketPayload

interface SendPayload {

    fun sendPayload(payload: CustomPacketPayload)

}