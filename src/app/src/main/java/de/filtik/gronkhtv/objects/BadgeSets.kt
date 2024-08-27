package de.filtik.gronkhtv.objects

data class Badge(
    val id: String,
    val image_url_1x: String,
    val image_url_2x: String,
    val image_url_4x: String,
    val title: String,
    val description: String,
    val click_action: String,
    val click_url: String?
)

data class BadgeData(
    val set_id: String,
    val versions: List<Badge>
)

data class BadgeSets(
    val data: List<BadgeData>
)
