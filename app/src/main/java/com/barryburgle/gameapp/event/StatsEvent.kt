package com.barryburgle.gameapp.event;

import com.barryburgle.gameapp.model.enums.StatsLoadInfoEnum

sealed interface StatsEvent : GenericEvent {
    data class ShowInfo(
        val statsLoadInfo: StatsLoadInfoEnum
    ) : StatsEvent

    object HideInfo : StatsEvent
}
