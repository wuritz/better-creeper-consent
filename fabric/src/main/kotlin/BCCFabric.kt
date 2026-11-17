/*
 * Copyright (c) 2026. wuritz
 * SPDX-License-Identifier: Apache-2.0
 */

package wuritz.bcc

import net.fabricmc.api.ModInitializer
import net.minecraft.resources.Identifier
import wuritz.bcc.network.IncomingConnectionF
import wuritz.bcc.utils.Constants

object BCCFabric : ModInitializer {

	override fun onInitialize() {
		BCCCommon.init()
		Constants.LOG.info("Initializing on server-side...")

		IncomingConnectionF.init()
		//TODO: command for options screen
		/*CommandRegistrationCallback.EVENT.register { dispatcher, registryAccess, selection ->
			dispatcher.register(Commands.literal("bcc-options").executes { context ->
				// Admin only
				val sPlayer = context.source.player
				if (sPlayer !is ServerPlayer) return@executes 0

				if (!context.source.permissions().hasPermission(Permissions.COMMANDS_OWNER)) {
					context.source.sendFailure(Component.literal("You don't have permission to run this command."))
					return@executes 0
				}

				ServerPlayNetworking.send(sPlayer, OpenOptionsScreenPayload(Random.nextInt()))
				return@executes 1
			})
		}*/

		Constants.LOG.info("Server-side initialized!")
	}

	fun id(path: String): Identifier
		= Identifier.fromNamespaceAndPath(Constants.MOD_ID, path)
}
