//package de.filtik.gronkhtv.chat
//
//class ChatEmotesPipe(private val sanitizer: Sanitizer, private val chatlogService: ChatLogService) : PipeTransform {
//    fun transform(value: String): String {
//        var transformedValue = value
//        val emojiRegex = Regex("(\\p{Emoji_Presentation})")
//        val emoteRegex = Regex("\\[emote:(.+?):(.+?):(.+?)]")
//
//        val containsEmoji = emojiRegex.containsMatchIn(transformedValue)
//
//        if (containsEmoji) {
//            transformedValue =
//                transformedValue.replace(emojiRegex, "<span class=\"g-chat-emoji\">$1</span>")
//        }
//
//        val emoteData = chatlogService.emoteData
//        if (emoteData.isNotEmpty()) {
//            val emoteRegions =
//                emoteData.split("/").map { it.split(":") }.map { (emoteId, emoteRegions) ->
//                    emoteRegions.split(",").map { emotePos ->
//                        val (start, end) = emotePos.split("-")
//                        start.toInt() to EmoteRegion(emoteId, emotePos)
//                    }
//                }
//
//            val emoteMap = mutableMapOf<Int, EmoteRegion>()
//            emoteRegions.forEach { regionList ->
//                regionList.forEach { (index, emoteRegion) ->
//                    emoteMap[index] = emoteRegion
//                }
//            }
//
//            emoteMap.toSortedMap(reverseOrder()).forEach { (_, emoteRegion) ->
//                val (start, end) = emoteRegion.emotePos.split("-")
//                val chars = transformedValue.toCharArray()
//                chars.splice(
//                    start.toInt(),
//                    end.toInt() - (start.toInt() - 1),
//                    "[emote:${emoteRegion.emoteId}:${getEmoteSize(emoteRegion.emoteId).width}:${
//                        getEmoteSize(emoteRegion.emoteId).width
//                    }]"
//                )
//                transformedValue = chars.joinToString("")
//            }
//        }
//
//        transformedValue = sanitizer.sanitize(HTML, transformedValue)
//        transformedValue = transformedValue.replace(emoteRegex) { result ->
//            val (emoteId, width, height) = result.destructured
//            "<img src=\"https://static-cdn.jtvnw.net/emoticons/v2/$emoteId/default/dark/2.0\" width=\"$width\" height=\"$height\" alt=\"emote\" class=\"g-chat-emote\">"
//        }
//
//        transformedValue = replaceGlobalEmotes(transformedValue)
//        transformedValue = replaceBttvEmotes(transformedValue)
//        transformedValue = replaceFfzEmotes(transformedValue)
//
//        return transformedValue
//    }
//
//    private fun getEmoteSize(emoteId: String): EmoteSize {
//        val emoteData = Ye[emoteId]
//        return if (emoteData?.fallback == true) {
//            emoteData
//        } else {
//            EmoteSize(
//                emoteData?.width ?: 18 / (emoteData?.height ?: 18 / 18),
//                emoteData?.height ?: 18 / (emoteData?.height ?: 18 / 18)
//            )
//        }
//    }
//
//    private fun replaceGlobalEmotes(value: String): String {
//        var replacedValue = value
//        chatlogService.globalEmotes.forEach { emote ->
//            replacedValue = replacedValue.replace(
//                "\\b${emote.code}\\b".toRegex(),
//                "<img src=\"https://cdn.betterttv.net/emote/${emote.id}/1x\" width=\"18\" height=\"18\" alt=\"${emote.code}\" class=\"g-chat-emote\">"
//            )
//        }
//        return replacedValue
//    }
//
//    private fun replaceBttvEmotes(value: String): String {
//        var replacedValue = value
//        chatlogService.channelEmotesBttv.forEach { emote ->
//            replacedValue = replacedValue.replace(
//                "\\b${emote.code}\\b".toRegex(),
//                "<img src=\"https://cdn.betterttv.net/emote/${emote.id}/1x\" width=\"18\" height=\"18\" alt=\"${emote.code}\" class=\"g-chat-emote\">"
//            )
//        }
//        return replacedValue
//    }
//
//    private fun replaceFfzEmotes(value: String): String {
//        var replacedValue = value
//        chatlogService.channelEmotesFfz.forEach { emote ->
//            replacedValue = replacedValue.replace(
//                "\\b${emote.name}\\b".toRegex(),
//                "<img src=\"https:${emote.urls[1]}\" width=\"auto\" height=\"18\" alt=\"${emote.name}\" class=\"g-chat-emote\">"
//            )
//        }
//        return replacedValue
//    }
//}
