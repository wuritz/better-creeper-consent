package wuritz.bcc

import net.minecraft.resources.Identifier
import wuritz.bcc.utils.Constants

object BCCCommon {

    fun init() {
        Constants.LOG.info("Initializing Better Creeper Consent...")
    }

    fun id(path: String): Identifier
            = Identifier.fromNamespaceAndPath(Constants.MOD_ID, path)
}