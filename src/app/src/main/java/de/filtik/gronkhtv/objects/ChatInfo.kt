package de.filtik.gronkhtv.objects

data class ChatInfo (
    var chunk_id: Long = 0,
    var chunk_time_length: Long = 0,
    var initial_start_time: String? = null,
    var messages: ArrayList<ChatInfoMessage> = ArrayList(),
)

data class ChatInfoMessage (
    var badge_info: String,
    var badges: String,
    var login: String,
    var display_name: String,
    var emote_data: String,
    var color: String,
    var created_at: String,
    var offset: Long,
    var message: String,
)