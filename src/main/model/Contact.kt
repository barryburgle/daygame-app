package model;

import java.time.Instant

open class Contact(
    private val number: String,
    topics: Array<String>,
    startTime: Instant,
    endTime: Instant
) : Convo(topics, startTime, endTime, true)