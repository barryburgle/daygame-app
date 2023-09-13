package main.model

import java.time.Instant

open class Convo(
    private val topics: Array<String>?,
    startTime: Instant?,
    endTime: Instant?,
    numberClose: Boolean?
) : Set(startTime, endTime, true, numberClose) {
    constructor() : this(null, null, null, null)
}