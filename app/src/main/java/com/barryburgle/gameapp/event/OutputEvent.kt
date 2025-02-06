package com.barryburgle.gameapp.event;

sealed interface OutputEvent : GenericEvent {
    object SwitchShowLeadLegend : OutputEvent
}
