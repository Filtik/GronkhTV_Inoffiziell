package de.filtik.gronkhtv.twitch.helix.auth.basic

/**
 * Holds the configuration properties for the basic Twitch Helix authentication, where only a client ID is used.
 */
open class BaseAuthConfig {

    internal var clientKey: String = "l4zwy2n66wg8z3lvdwgfadteys6mot"

    internal lateinit var clientId: String

    internal var sendWithoutRequest: Boolean = true
}