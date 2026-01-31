package com.barryburgle.gameapp.service.csv

import com.barryburgle.gameapp.model.challenge.Challenge

class ChallengeCsvService : AbstractCsvService<Challenge>() {

    companion object {
        private const val CHALLENGES_BACKUP_FILENAME: String = "challenges_backup"
    }

    public override fun getBackupFileName(): String {
        return CHALLENGES_BACKUP_FILENAME
    }

    override fun exportSingleRow(challenge: Challenge): Array<String> {
        val challengeFieldList = mutableListOf<String>()
        challengeFieldList.add(challenge.id.toString())
        challengeFieldList.add(challenge.insertTime)
        challengeFieldList.add(cleaEmptyField(challenge.name))
        challengeFieldList.add(cleaEmptyField(challenge.description))
        challengeFieldList.add(challenge.startDate)
        challengeFieldList.add(challenge.endDate)
        challengeFieldList.add(challenge.type)
        challengeFieldList.add(challenge.goal.toString())
        challengeFieldList.add(cleaEmptyField(challenge.tweetUrl))
        challengeFieldList.add(challenge.dayOfWeek.toString())
        return challengeFieldList.toTypedArray()
    }


    override fun generateHeader(): Array<String> {
        val leadFieldList = mutableListOf<String>()
        leadFieldList.add("id")
        leadFieldList.add("insert_time")
        leadFieldList.add("name")
        leadFieldList.add("description")
        leadFieldList.add("start_date")
        leadFieldList.add("end_date")
        leadFieldList.add("challenge_type")
        leadFieldList.add("goal")
        leadFieldList.add("tweet_url")
        leadFieldList.add("day_of_week")
        return leadFieldList.toTypedArray()
    }

    override fun mapImportRow(fields: Array<String>): Challenge {
        return Challenge(
            importLong(fields[0]),
            fields[1],
            fields[2],
            fields[3],
            fields[4],
            fields[5],
            fields[6],
            importLong(fields[7])!!.toInt(),
            fields[8],
            importLong(fields[9])!!.toInt(),
        )
    }

    override fun isEntityValid(challenge: Challenge): Boolean {
        // TODO: do better check on data validity on most of the fields
        if (challenge.id == 0L || challenge.id == null || challenge.insertTime.isEmpty()) {
            return false
        }
        return true
    }
}