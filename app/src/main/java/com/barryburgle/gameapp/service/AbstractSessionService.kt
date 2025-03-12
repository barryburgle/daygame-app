package com.barryburgle.gameapp.service

open class AbstractSessionService : EntityService() {
    companion object {
        fun computeApproachTime(
            sessionTime: Long,
            sets: Int
        ): Long {
            if (sets == 0) {
                return 0
            }
            return sessionTime / sets
        }

        fun computeConvoRatio(
            convos: Int,
            sets: Int
        ): Double {
            if (sets == 0) {
                return 0.0
            }
            val convoRatio = convos.toDouble() / sets.toDouble()
            return round(convoRatio)
        }

        fun computeRejectionRatio(
            convos: Int,
            sets: Int
        ): Double {
            if (sets == 0) {
                return 1.0
            }
            val rejectionRatio = 1 - convos.toDouble() / sets.toDouble()
            return round(rejectionRatio)
        }

        fun computeContactRatio(
            contacts: Int,
            sets: Int
        ): Double {
            if (sets == 0) {
                return 0.0
            }
            val contactRatio = contacts.toDouble() / sets.toDouble()
            return round(contactRatio)
        }

        fun computeIndex(sets: Int, convos: Int, contacts: Int, sessionTime: Long): Double {
            // TODO: create method for formula change
            val index = if (sessionTime == 0L) 0.0 else
                (sets.toDouble() * (12 * sets + 20 * convos + 30 * contacts).toDouble() / sessionTime.toDouble())
            return round(index)
        }
    }
}