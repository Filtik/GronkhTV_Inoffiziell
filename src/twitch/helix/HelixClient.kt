package de.filtik.gronkhtv.twitch.helix

import de.filtik.gronkhtv.twitch.helix.analytics.AnalyticsService
import de.filtik.gronkhtv.twitch.helix.auth.model.AuthCredentials
import de.filtik.gronkhtv.twitch.helix.bits.BitsService
import de.filtik.gronkhtv.twitch.helix.channels.ChannelService
import de.filtik.gronkhtv.twitch.helix.clips.ClipService
import de.filtik.gronkhtv.twitch.helix.entitlements.EntitlementService
import de.filtik.gronkhtv.twitch.helix.extensions.ExtensionService
import de.filtik.gronkhtv.twitch.helix.games.GameService
import de.filtik.gronkhtv.twitch.helix.moderation.ModerationService
import de.filtik.gronkhtv.twitch.helix.streams.StreamService
import de.filtik.gronkhtv.twitch.helix.subscriptions.SubscriptionService
import de.filtik.gronkhtv.twitch.helix.users.UserService
import de.filtik.gronkhtv.twitch.helix.videos.VideoService
import de.filtik.gronkhtv.twitch.helix.webhook.WebhookService

/**
 * The main entry-point of the library. Can be used to access all Twitch Helix endpoints.
 * @constructor Creates a new Helix Client object.
 * @param credentials The credentials necessary to authenticate all HTTP requests.
 */
class HelixClient(
    credentials: AuthCredentials
) {
    /**
     * Can be used to access all *Analytics* Helix endpoints.
     */
    val analytics: AnalyticsService = AnalyticsService(credentials)

    /**
     * Can be used to access all *Bits* Helix endpoints.
     */
    val bits: BitsService = BitsService(credentials)

    /**
     * Can be used to access all *Channels* Helix endpoints.
     */
    val channels = ChannelService(credentials)

    /**
     * Can be used to access all *Clips* Helix endpoints.
     */
    val clips = ClipService(credentials)

    /**
     * Can be used to access all *Entitlements* Helix endpoints.
     */
    val entitlements = EntitlementService(credentials)

    /**
     * Can be used to access all *Extensions* Helix endpoints.
     */
    val extensions = ExtensionService(credentials)

    /**
     * Can be used to access all *Games* Helix endpoints.
     */
    val games = GameService(credentials)

    /**
     * Can be used to access all *Moderation* Helix endpoints.
     */
    val moderation = ModerationService(credentials)

    /**
     * Can be used to access all *Streams* Helix endpoints.
     */
    val streams = StreamService(credentials)

    /**
     * Can be used to access all *Subscriptions* Helix endpoints.
     */
    val subscriptions = SubscriptionService(credentials)

    /**
     * Can be used to access all *Users* Helix endpoints.
     */
    val users = UserService(credentials)

    /**
     * Can be used to access all *Videos* Helix endpoints.
     */
    val videos = VideoService(credentials)

    /**
     * Can be used to access all *Webhooks* Helix endpoints.
     */
    val webhooks = WebhookService(credentials)
}