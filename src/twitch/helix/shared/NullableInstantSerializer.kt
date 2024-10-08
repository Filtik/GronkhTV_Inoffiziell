package de.filtik.gronkhtv.twitch.helix.shared

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.temporal.ChronoUnit

/**
 * Serializer class used to convert a nullable [String] containing a timestamp to a nullable [Instant].
 * @constructor Creates a new instant serializer object.
 */
object NullableInstantSerializer : KSerializer<Instant?> {

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("Instant", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Instant?) {
        value?.let { encoder.encodeString(DateTimeFormatter.ISO_INSTANT.format(value.truncatedTo(ChronoUnit.SECONDS))) }
    }

    override fun deserialize(decoder: Decoder): Instant? {
        return try {
            Instant.parse(decoder.decodeString())
        } catch (e: DateTimeParseException) {
            null
        }
    }
}