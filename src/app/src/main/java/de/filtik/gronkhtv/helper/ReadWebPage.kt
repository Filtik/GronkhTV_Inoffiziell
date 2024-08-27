package de.filtik.gronkhtv.helper;

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import de.filtik.gronkhtv.classes.MovieInfo
import de.filtik.gronkhtv.objects.BadgeSets
import de.filtik.gronkhtv.objects.BetterEmotes
import de.filtik.gronkhtv.objects.ChatInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


object ReadWebPage {
    private var Twitch_ID: String = "l4"
    private var Twitch_Access_Token: String = "pa"

    fun fetch(url_fetch: String): JSONArray {
        val json = URL(url_fetch).readText()
        if (json.isEmpty()) {
            return JSONArray()
        }

        val obj = JSONObject(json)

        return obj.getJSONObject("results").getJSONArray("videos")
    }

    fun episodeURL(episode: Long): String {
        val json = URL("https://api.gronkh.tv/v1/video/playlist?episode=$episode").readText()

        val obj = JSONObject(json)

        return obj.getString("playlist_url")
    }

    fun videoInfo(episode: Long): MovieInfo {
        val json = URL("https://api.gronkh.tv/v1/video/info?episode=$episode").readText()

        val gson = Gson()

        return gson.fromJson(json, MovieInfo::class.java)
    }

    suspend fun chatInfo(chat: String, chat_num: Int): ChatInfo = withContext(Dispatchers.IO) {
        val json = URL("$chat/$chat_num").readText()

        val gson = Gson()

        gson.fromJson(json, ChatInfo::class.java)
    }

    fun getChatBadges(channelId: Long = 12875057): BadgeSets? {
        try {
            val gson = Gson()

//            val globalBadges = URL("https://badges.twitch.tv/v1/badges/global/display").readText()
//            val globalBadgeSets = gson.fromJson(globalBadges, BadgeSets::class.java)
//
//            val channelBadges =
//                URL("https://badges.twitch.tv/v1/badges/channels/$channelId/display").readText()
//            val channelBadgeSets = gson.fromJson(channelBadges, BadgeSets::class.java)

            var globalBadges = ""
            var channelBadges = ""

            try {
                val url = URL("https://api.twitch.tv/helix/chat/badges/global")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.setRequestProperty("Authorization", "Bearer ${Twitch_Access_Token}")
                connection.setRequestProperty("Client-Id", Twitch_ID)
//                connection.setRequestProperty("Host", "api.twitch.tv")

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val reader = BufferedReader(InputStreamReader(connection.inputStream))
                    val response = StringBuilder()
                    var line: String?
                    while (reader.readLine().also { line = it } != null) {
                        response.append(line)
                    }
                    reader.close()
                    globalBadges = response.toString()
                } else {
                    println("Request failed with response code: $responseCode")
                }
                connection.disconnect()
            }
            catch (ex: Exception) {
                println(ex.message)
            }

            try {
                val url = URL("https://api.twitch.tv/helix/chat/badges?broadcaster_id=$channelId")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.setRequestProperty("Authorization", "Bearer ${Twitch_Access_Token}")
                connection.setRequestProperty("Client-Id", Twitch_ID)

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val reader = BufferedReader(InputStreamReader(connection.inputStream))
                    val response = StringBuilder()
                    var line: String?
                    while (reader.readLine().also { line = it } != null) {
                        response.append(line)
                    }
                    reader.close()
                    channelBadges = response.toString()
                } else {
                    println("Request failed with response code: $responseCode")
                }
                connection.disconnect()
            }
            catch (ex: Exception) {
                println(ex.message)
            }

            val globalBadgeSets = gson.fromJson(globalBadges, BadgeSets::class.java)
            val channelBadgeSets = gson.fromJson(channelBadges, BadgeSets::class.java)

            return globalBadgeSets.copy(data = channelBadgeSets.data + globalBadgeSets.data)
        }
        catch (ex: Exception) {
            println(ex.message)
            return null
        }
    }

    fun getBetterEmotes(channelId: Long = 12875057): ArrayList<BetterEmotes> {
        val gson = Gson()
        val emotes = ArrayList<BetterEmotes>()

        val globalBadges = URL("https://api.betterttv.net/3/cached/emotes/global").readText()
        val arrayType = object : TypeToken<ArrayList<BetterEmotes>>() {}.type
        val globalBadgeSets: ArrayList<BetterEmotes> = gson.fromJson(globalBadges, arrayType)

        val channelBadges =
            URL("https://api.betterttv.net/3/cached/users/twitch/$channelId").readText()
        val json = JSONObject(channelBadges).getJSONArray("sharedEmotes").toString()
        val channelBadgeSets: ArrayList<BetterEmotes> = gson.fromJson(json, arrayType)

        emotes.addAll(globalBadgeSets)
        emotes.addAll(channelBadgeSets)

        return emotes
    }
}
