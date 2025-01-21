package com.barryburgle.gameapp.service

import com.barryburgle.gameapp.model.session.AbstractSession
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

        private fun computeSessionTime(
            abstractSessionList: List<AbstractSession>
        ): Long {
            var sessionTime: Long = 0
            for (abstractSession in abstractSessionList) {
                sessionTime = sessionTime + abstractSession.sessionTime
            }
            return sessionTime
        }

        fun computeSpentHours(
            abstractSessionList: List<AbstractSession>
        ): Long {
            var sessionTime = computeSessionTime(abstractSessionList)
            var spentHours = sessionTime / 60L
            return spentHours
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
    }
}