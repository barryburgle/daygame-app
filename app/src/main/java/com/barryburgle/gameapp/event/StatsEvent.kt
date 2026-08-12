package com.barryburgle.gameapp.event;

import com.barryburgle.gameapp.model.enums.StatsLoadInfoEnum
import com.barryburgle.gameapp.model.pinpoint.PinPointTypeEnum

sealed interface StatsEvent : GenericEvent {
    data class ShowInfo(
        val statsLoadInfo: StatsLoadInfoEnum
    ) : StatsEvent

    object HideInfo : StatsEvent
    data class SelectPinPointType(
        val selectedTypes: List<PinPointTypeEnum>
    ) : StatsEvent
    data class SelectTimePinPointType(
        val selectedTypes: List<PinPointTypeEnum>
    ) : StatsEvent
}
