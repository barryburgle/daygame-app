package com.barryburgle.gameapp.service.challenge

import com.barryburgle.gameapp.model.challenge.Challenge
import com.barryburgle.gameapp.service.ServiceTestData
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import org.junit.Test

class ChallengeServiceTest : ServiceTestData() {

    val challengeService: ChallengeService = ChallengeService()

    @Test
    fun initTest() {
        val challenge: Challenge = challengeService.init(
            null,
            CHALLENGE_NAME,
            CHALLENGE_DESC,
            CHALLENGE_DURATION,
            CHALLENGE_TYPE,
            CHALLENGE_GOAL,
            TWEET_URL
        )
        assertNotNull(challenge.insertTime)
        assertEquals(CHALLENGE_NAME, challenge.name)
        assertEquals(CHALLENGE_DESC, challenge.description)
        assertNotNull(challenge.startDate)
        assertNotNull(challenge.endDate)
        assertEquals(CHALLENGE_TYPE, challenge.type)
        assertEquals(CHALLENGE_GOAL, challenge.goal.toString())
        assertEquals(TWEET_URL, challenge.tweetUrl.toString())
    }
}