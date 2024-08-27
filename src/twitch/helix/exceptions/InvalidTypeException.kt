package de.filtik.gronkhtv.twitch.helix.exceptions

/**
 * An exception that is thrown when an unsupported type is given to a generic method.
 * @param message Explains why the exception was thrown
 * @constructor Creates a new invalid type exception.
 */
class InvalidTypeException(message: String) : Exception(message)