package com.barryburgle.gameapp.model.game

import androidx.compose.ui.graphics.vector.ImageVector
import com.barryburgle.gameapp.model.enums.CountryEnum
import com.barryburgle.gameapp.model.lead.Lead

interface EventModel {

    fun getEventDate(): String?

    fun getEventTitle(): String

    fun getEventIcon(): ImageVector

    fun getEventDescription(): String

    fun getEventStickingPoints(): String?

    fun shareReport(leads: List<Lead>): String

    fun reportLeads(leads: List<Lead>): String {
        var leadsReport = ""
        if (!leads.isEmpty()) {
            leadsReport = ":"
            leads.forEach {
                leadsReport =
                    leadsReport + " " + CountryEnum.getFlagByAlpha3(it.nationality) + " " + it.age + "yo,"
            }
            leadsReport = leadsReport.dropLast(1)
        }
        return leadsReport
    }

    fun pluralMaker(quantity: Int): String {
        return if (quantity > 1 || quantity == 0) "s" else ""
    }

    fun stickingPointsReport(stickingPoints: String?): String {
        return if (stickingPoints != null && stickingPoints.isBlank()) "" else "\n\nSticking points:\n$stickingPoints"
    }

    fun checkmarkReport(flag: Boolean): String {
        return if (flag) ": ✅" else ": ❌"
    }
}