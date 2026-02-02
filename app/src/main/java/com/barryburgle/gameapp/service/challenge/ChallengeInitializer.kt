package com.barryburgle.gameapp.service.challenge

import com.barryburgle.gameapp.model.challenge.Challenge
import java.time.LocalDate

interface ChallengeInitializer {
    fun init(
        id: String?,
        name: String,
        description: String,
        startDate: LocalDate,
        duration: String,
        type: String,
        goal: String,
        tweetUrl: String
    ): Challenge
}