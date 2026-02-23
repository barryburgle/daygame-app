package com.barryburgle.gameapp.service.challenge

import com.barryburgle.gameapp.model.challenge.AchievedChallenge
import com.barryburgle.gameapp.model.enums.ChallengeMedalEnum
import com.barryburgle.gameapp.service.FormatService
import java.time.LocalDate

class ChallengeMedalService {

    companion object {

        fun getMedals(achievedChallengeList: List<AchievedChallenge>): List<ChallengeMedalEnum> {
            return achievedChallengeList.map { achievedChallenge -> getMedal(achievedChallenge) }
        }

        fun getMedal(achievedChallenge: AchievedChallenge): ChallengeMedalEnum {
            val completionPerc = achievedChallenge.getCompletionPerc()
            return when {
                completionPerc >= 1 -> ChallengeMedalEnum.GOLD
                completionPerc < 1 && completionPerc >= 0.9 -> ChallengeMedalEnum.SILVER
                completionPerc < 0.9 && completionPerc >= 0.7 -> ChallengeMedalEnum.BRONZE
                completionPerc < 0.7 && LocalDate.now() <= FormatService.parseDate(achievedChallenge.challenge.endDate) -> ChallengeMedalEnum.ONGOING
                else -> ChallengeMedalEnum.NONE
            }
        }
    }
}