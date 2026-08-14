package wuritz.bcc

import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.commands.Commands
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier
import net.minecraft.server.level.ServerPlayer
import net.minecraft.server.permissions.Permissions
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import wuritz.bcc.network.connection.IncomingConnection
import wuritz.bcc.network.payloads.OpenOptionsScreenPayload
import kotlin.random.Random

object BetterCreeperConsent : ModInitializer {
	const val MOD_ID: String = "better-creeper-consent"

	val LOG: Logger = LoggerFactory.getLogger(MOD_ID)

	override fun onInitialize() {
		LOG.info("[Init] Initializing on server-side...")

		IncomingConnection.init()
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

		LOG.info("[Init] Server-side initialized!")
	}

	fun id(path: String): Identifier
		= Identifier.fromNamespaceAndPath(MOD_ID, path)
}
