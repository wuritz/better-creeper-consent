package wuritz.bcc

import net.fabricmc.api.ModInitializer
import net.minecraft.resources.Identifier
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import wuritz.bcc.network.connection.IncomingConnection

object BetterCreeperConsent : ModInitializer {
	const val MOD_ID: String = "better-creeper-consent"

	val LOG: Logger = LoggerFactory.getLogger(MOD_ID)

	override fun onInitialize() {
		LOG.info("[Init] Initializing on server-side...")

		IncomingConnection.init()

		LOG.info("[Init] Server-side initialized!")
	}

	fun id(path: String): Identifier
		= Identifier.fromNamespaceAndPath(MOD_ID, path)
}
