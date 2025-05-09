package com.barryburgle.gameapp.service

import com.barryburgle.gameapp.model.date.Date
import com.barryburgle.gameapp.model.session.AbstractSession
import com.barryburgle.gameapp.model.set.SingleSet
import kotlin.math.truncate

class GlobalStatsService {

    companion object {

        fun computeSets(
            abstractSessionList: List<AbstractSession>
        ): Int {
            var sets: Int = 0
            for (abstractSession in abstractSessionList) {
                sets = sets + abstractSession.sets
            }
            return sets
        }

        fun computeConvos(
            abstractSessionList: List<AbstractSession>
        ): Int {
            var convos: Int = 0
            for (abstractSession in abstractSessionList) {
                convos = convos + abstractSession.convos
            }
            return convos
        }

        fun computeContacts(
            abstractSessionList: List<AbstractSession>
        ): Int {
            var contacts: Int = 0
            for (abstractSession in abstractSessionList) {
                contacts = contacts + abstractSession.contacts
            }
            return contacts
        }

        private fun computeSetTime(
            setList: List<SingleSet>
        ): Long {
            var setTime: Long = 0
            for (set in setList) {
                setTime = setTime + set.setTime
            }
            return setTime
        }

        private fun computeDateTime(
            dateList: List<Date>
        ): Long {
            var dateTime: Long = 0
            for (date in dateList) {
                dateTime = dateTime + date.dateTime
            }
            return dateTime
        }

        private fun computeSessionTime(
            abstractSessionList: List<AbstractSession>
        ): Long {
            var sessionTime: Long = 0
            for (abstractSession in abstractSessionList) {
                sessionTime = sessionTime + abstractSession.sessionTime
            }
            return sessionTime
        }

        fun computeSetsSpentHours(
            setList: List<SingleSet>
        ): Long {
            var setTime = computeSetsSpentMinutes(setList)
            var spentHours = setTime / 60L
            return spentHours
        }

        fun computeSetsSpentMinutes(
            setList: List<SingleSet>
        ): Long {
            return computeSetTime(setList)
        }

        fun computeDateSpentHours(
            dateList: List<Date>
        ): Long {
            var dateTime = computeDateTime(dateList)
            var spentHours = dateTime / 60L
            return spentHours
        }

        fun computeSessionSpentHours(
            abstractSessionList: List<AbstractSession>
        ): Long {
            var sessionTime = computeSessionTime(abstractSessionList)
            var spentHours = sessionTime / 60L
            return spentHours
        }

        fun computeAvgContactTime(
            setList: List<SingleSet>,
            contacts: Int
        ): Long {
            if (contacts == 0) {
                return 0L
            }
            var setTime = computeSetTime(setList)
            var avgContactTime = setTime / contacts
            return avgContactTime
        }

        fun computeAvgLayTime(
            dateList: List<Date>,
            lays: Int
        ): Long {
            if (lays == 0) {
                return 0L
            }
            var dateTime = computeDateTime(dateList)
            var avgLayTime = dateTime / lays
            return avgLayTime
        }

        fun computeAvgApproachTime(
            abstractSessionList: List<AbstractSession>
        ): Long {
            var sessionTime = computeSessionTime(abstractSessionList)
            var sets = computeSets(abstractSessionList).toLong()
            var avgApproachTime = sessionTime / sets
            return avgApproachTime
        }

        fun computeAvgConvoRatio(
            abstractSessionList: List<AbstractSession>
        ): Int {
            var convoRatio: Double = 0.0
            for (abstractSession in abstractSessionList) {
                convoRatio = convoRatio + abstractSession.convoRatio
            }
            var avgConvoRatio = truncate(convoRatio * 100).toInt() / abstractSessionList.size
            return avgConvoRatio
        }

        fun computeAvgRejectionRatio(
            abstractSessionList: List<AbstractSession>
        ): Int {
            var rejectionRatio: Double = 0.0
            for (abstractSession in abstractSessionList) {
                rejectionRatio =
                    rejectionRatio + abstractSession.rejectionRatio
            }
            var avgRejectionRatio =
                truncate(rejectionRatio * 100).toInt() / abstractSessionList.size
            return avgRejectionRatio
        }

        fun computeAvgContactRatio(
            abstractSessionList: List<AbstractSession>
        ): Int {
            var contactRatio: Double = 0.0
            for (abstractSession in abstractSessionList) {
                contactRatio = contactRatio + abstractSession.contactRatio
            }
            var avgContactRatio = truncate(contactRatio * 100).toInt() / abstractSessionList.size
            return avgContactRatio
        }

        fun computeAvgIndex(
            abstractSessionList: List<AbstractSession>
        ): Double {
            var index: Double = 0.0
            for (abstractSession in abstractSessionList) {
                index = index + abstractSession.index
            }
            var avgIndex = truncate(index / abstractSessionList.size * 100) / 100
            return avgIndex
        }

        fun computeAvgLeadTime(
            leadsNumber: Int,
            abstractSessionList: List<AbstractSession>
        ): Long {
            if (leadsNumber == 0) {
                return 0L
            }
            var sessionTime = computeSessionTime(abstractSessionList)
            var leadsNumber = leadsNumber.toLong()
            var avgLeadTime = sessionTime / leadsNumber
            return avgLeadTime
        }

        fun computeGenericRatio(
            whole: Int,
            portion: Int
        ): Int {
            if (whole == 0) {
                return 0
            }
            return portion * 100 / whole
        }
    }
}