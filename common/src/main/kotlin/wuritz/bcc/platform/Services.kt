package wuritz.bcc.platform;

import wuritz.bcc.platform.interfaces.SendConsentScreen
import wuritz.bcc.platform.interfaces.SendPayload
import java.util.ServiceLoader;

object Services {

    val SEND_CONSENT_SCREEN: SendConsentScreen = this.load(SendConsentScreen::class.java)
    val SEND_PAYLOAD: SendPayload = this.load(SendPayload::class.java)

    fun <T> load(clazz: Class<T>): T =
        ServiceLoader.load(clazz, Services::class.java.classLoader)
            .findFirst()
            .orElseThrow { NullPointerException("Failed to load service for ${clazz.name}") }
}