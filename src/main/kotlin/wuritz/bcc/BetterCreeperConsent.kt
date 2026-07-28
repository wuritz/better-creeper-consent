package wuritz.bcc

import net.fabricmc.api.ModInitializer
import net.minecraft.resources.Identifier
import org.slf4j.LoggerFactory

object BetterCreeperConsent : ModInitializer {
	const val MOD_ID: String = "better-creeper-consent"

	val LOG = LoggerFactory.getLogger(MOD_ID)

	override fun onInitialize() {
		LOG.info("Initializing mod")


	}

	fun id(path: String): Identifier
		= Identifier.fromNamespaceAndPath(MOD_ID, path)
}
