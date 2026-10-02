package wuritz.bcc

import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.commands.Commands
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier
import net.minecraft.server.level.ServerPlayer
import net.minecraft.server.permissions.Permissions
import wuritz.bcc.connection.IncomingConnectionF
import wuritz.bcc.network.payloads.outgoing.OpenOptionsScreenPayload
import wuritz.bcc.utils.Constants
import kotlin.random.Random

object BCCFabric : ModInitializer {

	override fun onInitialize() {
		BCCCommon.init()
		Constants.LOG.info("Initializing on server-side...")

		IncomingConnectionF.init()
		CommandRegistrationCallback.EVENT.register { dispatcher, registryAccess, selection ->
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
		}

		Constants.LOG.info("Server-side initialized!")
	}

	fun id(path: String): Identifier
		= Identifier.fromNamespaceAndPath(Constants.MOD_ID, path)
}
