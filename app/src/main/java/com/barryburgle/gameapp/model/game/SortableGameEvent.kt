package com.barryburgle.gameapp.model.game

class SortableGameEvent(
    var insertTime: String,
    var eventDate: String,
    var eventHour: String,
    var timeSpent: Long,
    var dayOfWeek: Int,
    var classType: String,
    var event: EventModel
)