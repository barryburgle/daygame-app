package com.barryburgle.gameapp.service

import android.content.ContentResolver
import android.provider.ContactsContract
import kotlin.math.min

class PhoneBookService {
    companion object {

        fun findSimilarContact(
            contentResolver: ContentResolver,
            leadName: String
        ): Pair<String, String>? {
            val cursor = contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                arrayOf(
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                    ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY
                ),
                null,
                null,
                null
            )
            cursor?.use {
                val nameIndex =
                    it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
                val lookupKeyIndex =
                    it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY)
                while (it.moveToNext()) {
                    val contactName = it.getString(nameIndex)
                    val contactLookupKey = it.getString(lookupKeyIndex)
                    if (isSimilar(leadName.lowercase(), contactName.lowercase())) {
                        return Pair(contactName, contactLookupKey)
                    }
                }
            }
            return null
        }

        private fun isSimilar(s1: String, s2: String): Boolean {
            if (s1.contains(s2) || s2.contains(s1)) return true
            val distance = levenshteinDistance(s1, s2)
            return distance <= 2
        }

        private fun levenshteinDistance(s1: String, s2: String): Int {
            val dp = Array(s1.length + 1) { IntArray(s2.length + 1) }
            for (i in 0..s1.length) dp[i][0] = i
            for (j in 0..s2.length) dp[0][j] = j
            for (i in 1..s1.length) {
                for (j in 1..s2.length) {
                    val cost = if (s1[i - 1] == s2[j - 1]) 0 else 1
                    dp[i][j] = min(min(dp[i - 1][j] + 1, dp[i][j - 1] + 1), dp[i - 1][j - 1] + cost)
                }
            }
            return dp[s1.length][s2.length]
        }
    }
}