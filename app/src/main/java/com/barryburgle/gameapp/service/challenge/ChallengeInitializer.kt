package com.barryburgle.gameapp.service.challenge

import com.barryburgle.gameapp.model.challenge.Challenge

interface ChallengeInitializer {
    fun init(
        id: String?,
        name: String,
        description: String,
        duration: String,
        type: String,
        goal: String,
        tweetUrl: String
    ): Challenge
}