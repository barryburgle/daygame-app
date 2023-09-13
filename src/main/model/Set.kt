package main.model

import java.time.Instant

open class Set(
    private val startTime: Instant,
    private val endTime: Instant,
    private val conversation: Boolean,
    private val numberClose: Boolean
)