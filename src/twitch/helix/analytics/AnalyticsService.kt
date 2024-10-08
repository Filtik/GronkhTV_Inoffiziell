package de.filtik.gronkhtv.twitch.helix.analytics

import de.filtik.gronkhtv.twitch.helix.auth.model.AuthCredentials
import de.filtik.gronkhtv.twitch.helix.auth.model.AuthScope
import de.filtik.gronkhtv.twitch.helix.http.ResourceService
import de.filtik.gronkhtv.twitch.helix.analytics.model.GameReport
import de.filtik.gronkhtv.twitch.helix.analytics.model.ExtensionReport
import io.ktor.client.*
import io.ktor.client.request.*
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

/**
 * Service class that can be used to access *Analytics* endpoints:
 * *GET game analytics* and *GET extension analytics*.
 */
class AnalyticsService : ResourceService {

    /**
     * Creates a new analytics service object given some authentication credentials and a HTTP client configuration.
     * @param credentials The authentication credentials: can be OAuth or simple credentials with only a client ID
     */
    constructor(credentials: AuthCredentials) : super(credentials)

    /**
     * Creates a new analytics service object given an HTTP client.
     * @param httpClient The HTTP client.
     */
    constructor(httpClient: HttpClient) : super(httpClient)

    private companion object {
        private const val BASE_URL = "${ResourceService.BASE_URL}/analytics"
    }

    /**
     * Gets a URL that game developers can use to download analytics reports (CSV files) for their games.
     * The URL is valid for 5 minutes.
     * Requires [AuthScope.ANALYTICS_READ_GAMES].
     * For more information see the [Twitch developer reference](https://dev.twitch.tv/docs/api/reference#get-game-analytics)
     *
     * @param gameId If this is specified, the returned URL points to an analytics report for just the specified game. If this is not specified, the response includes multiple URLs (paginated), pointing to separate analytics reports for each of the authenticated user’s games.
     * @param startedAt Starting date/time for returned reports. If this is provided, [endedAt] also must be specified. If [startedAt] is earlier than the default start date, the default date is used.
     * @param endedAt Ending date/time for returned reports. If this is provided, [startedAt] also must be specified. If [endedAt] is later than the default end date, the default date is used.
     * @param first Maximum number of objects to return. Maximum: 100, Default: 100
     * @param type Type of analytics report that is returned. Valid values: "overview_v1", "overview_v2". Default: all report types for the authenticated user’s games.
     * @return A [GameAnalyticsScrollableResponse] that holds a collection of [GameReport] resources (can have multiple pages).
     * @sample samples.getGameAnalytics
     */
    suspend fun getGameAnalytics(
        gameId: Long? = null,
        startedAt: Instant? = null,
        endedAt: Instant? = null,
        first: Int = 100,
        type: String? = null
    ) =
        GameAnalyticsScrollableResponse(
            httpClient.get("$BASE_URL/games") {
                gameId?.let {
                    parameter("game_id", gameId)
                }
                addAnalyticsParameters(startedAt, endedAt, first, type)
            }, httpClient
        )

    /**
     * Gets a URL that extension developers can use to download analytics reports (CSV files) for their extensions.
     * The URL is valid for 5 minutes.
     * Requires [AuthScope.ANALYTICS_READ_EXTENSIONS].
     * For more information see the [Twitch developer reference](https://dev.twitch.tv/docs/api/reference#get-extension-analytics)
     *
     * @param extensionId If this is specified, the returned URL points to an analytics report for just the specified extension. If this is not specified, the response includes multiple URLs (paginated), pointing to separate analytics reports for each of the authenticated user’s extensions.
     * @param startedAt Starting date/time for returned reports. If this is provided, [endedAt] also must be specified. If [startedAt] is earlier than the default start date, the default date is used.
     * @param endedAt Ending date/time for returned reports. If this is provided, [startedAt] also must be specified. If [endedAt] is later than the default end date, the default date is used.
     * @param first Maximum number of objects to return. Maximum: 100, Default: 100
     * @param type Type of analytics report that is returned. Valid values: "overview_v1", "overview_v2". Default: all report types for the authenticated user’s games.
     * @return A [ExtensionAnalyticsScrollableResponse] that holds a collection of [ExtensionReport] resources (can have multiple pages).
     * @sample samples.getExtensionAnalytics
     */
    suspend fun getExtensionAnalytics(
        extensionId: Long? = null,
        startedAt: Instant? = null,
        endedAt: Instant? = null,
        first: Int = 100,
        type: String? = null
    ) = ExtensionAnalyticsScrollableResponse(
        httpClient.get("$BASE_URL/extensions") {
            extensionId?.let {
                parameter("extension_id", extensionId)
            }
            addAnalyticsParameters(startedAt, endedAt, first, type)
        }, httpClient
    )

    private fun HttpRequestBuilder.addAnalyticsParameters(
        startedAt: Instant? = null,
        endedAt: Instant? = null,
        first: Int = 100,
        type: String? = null
    ) {
        startedAt?.let {
            parameter(
                "started_at",
                DateTimeFormatter.ISO_INSTANT.format(startedAt.truncatedTo(ChronoUnit.SECONDS))
            )
        }
        endedAt?.let {
            parameter(
                "ended_at",
                DateTimeFormatter.ISO_INSTANT.format(endedAt.truncatedTo(ChronoUnit.SECONDS))
            )
        }
        parameter("first", first)
        type?.let { parameter("type", type) }
    }
}