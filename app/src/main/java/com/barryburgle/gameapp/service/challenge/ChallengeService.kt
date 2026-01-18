package com.barryburgle.gameapp.service.challenge

import com.barryburgle.gameapp.model.challenge.Challenge
import com.barryburgle.gameapp.service.EntityService
import java.time.LocalDate
import java.time.OffsetDateTime

class ChallengeService : ChallengeInitializer, EntityService() {

    override fun init(
        id: String?,
        name: String,
        description: String,
        duration: String,
        type: String,
        goal: String,
        tweetUrl: String
    ): Challenge {
        val now = OffsetDateTime.now().toString()
        val startDate = LocalDate.parse(now.take(10))
        val endDate = startDate.plusDays(duration.toInt().toLong())
        return Challenge(
            getId(id),
            now,
            name,
            description,
            startDate.toString(),
            endDate.toString(),
            type,
            goal.toInt(),
            tweetUrl
        )
    }

    // TODO: in the ChallengeService do method that given the list of all the events (and challenges) enriches the challenge objects with completion metadata (completion perc, completion date and related events types&ids)
    // In this method there should be some switch case that, given the type of challenge, computes the completion of the challenge differently. Ie:
    // A simple 10/10 (100 sets in 10 days) challenge should have completion by the sum of all the sets done in sessions and single sets in the period between start and end date of the challenge
    // Instead, an "3.0 Index" challenge should be considered completed if most (decide from settings or hardcode) sessions in between start and end date of the challenge have index > 3.0 (so you cna see you can't complete the challenge summing up data from session, but counting a single session as a boolean)
    // Other kind of session could also have another completion logic defined later

    // TODO: after having a solid method as mentioned in the previous, consider the idea of a "challengeSuggester" method that, given past challenges, should propose to you some new ones that are more challenging (same type but with a lower goal - in case of past repetitive failures - or higher goals - in case of successes - or other types of challenges)
}