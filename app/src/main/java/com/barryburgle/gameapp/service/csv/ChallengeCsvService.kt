package com.barryburgle.gameapp.service.csv

import com.barryburgle.gameapp.model.challenge.AchievedChallenge
import com.barryburgle.gameapp.model.challenge.Challenge

class ChallengeCsvService : AbstractCsvService<AchievedChallenge>() {

    companion object {
        private const val CHALLENGES_BACKUP_FILENAME: String = "challenges_backup"
    }

    public override fun getBackupFileName(): String {
        return CHALLENGES_BACKUP_FILENAME
    }

    override fun exportSingleRow(achievedChallenge: AchievedChallenge): Array<String> {
        val challengeFieldList = mutableListOf<String>()
        challengeFieldList.add(achievedChallenge.challenge.id.toString())
        challengeFieldList.add(achievedChallenge.challenge.insertTime)
        challengeFieldList.add(cleaEmptyField(achievedChallenge.challenge.name))
        challengeFieldList.add(cleaEmptyField(achievedChallenge.challenge.description))
        challengeFieldList.add(achievedChallenge.challenge.startDate)
        challengeFieldList.add(achievedChallenge.challenge.endDate)
        challengeFieldList.add(achievedChallenge.challenge.type)
        challengeFieldList.add(achievedChallenge.challenge.goal.toString())
        challengeFieldList.add(achievedChallenge.achieved.toString())
        challengeFieldList.add(cleaEmptyField(achievedChallenge.challenge.tweetUrl))
        challengeFieldList.add(achievedChallenge.challenge.dayOfWeek.toString())
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
        leadFieldList.add("achieved")
        leadFieldList.add("tweet_url")
        leadFieldList.add("day_of_week")
        return leadFieldList.toTypedArray()
    }

    override fun mapImportRow(fields: Array<String>): AchievedChallenge {
        return AchievedChallenge(
            Challenge(
                importLong(fields[0]),
                fields[1],
                fields[2],
                fields[3],
                fields[4],
                fields[5],
                fields[6],
                importLong(fields[7])!!.toInt(),
                fields[9],
                importLong(fields[10])!!.toInt()
            )
        )
    }

    override fun isEntityValid(achievedChallenge: AchievedChallenge): Boolean {
        // TODO: do better check on data validity on most of the fields
        if (achievedChallenge.challenge.id == 0L || achievedChallenge.challenge.id == null || achievedChallenge.challenge.insertTime.isEmpty()) {
            return false
        }
        return true
    }
}