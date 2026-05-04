package com.barryburgle.gameapp.event

import com.barryburgle.gameapp.model.challenge.AchievedChallenge
import com.barryburgle.gameapp.model.date.Date
import com.barryburgle.gameapp.model.lead.Lead
import com.barryburgle.gameapp.model.session.AbstractSession
import com.barryburgle.gameapp.model.set.SingleSet
import com.barryburgle.gameapp.model.setting.Setting

sealed interface ToolEvent : GenericEvent {
    data class SetExportSessionsFileName(val exportSessionsFileName: String) : ToolEvent
    data class SetImportSessionsFileName(val importSessionsFileName: String) : ToolEvent
    data class SetExportLeadsFileName(val exportLeadsFileName: String) : ToolEvent
    data class SetImportLeadsFileName(val importLeadsFileName: String) : ToolEvent
    data class SetExportDatesFileName(val exportDatesFileName: String) : ToolEvent
    data class SetImportDatesFileName(val importDatesFileName: String) : ToolEvent
    data class SetExportSetsFileName(val exportSetsFileName: String) : ToolEvent
    data class SetImportSetsFileName(val importSetsFileName: String) : ToolEvent
    data class SetExportChallengesFileName(val exportChallengesFileName: String) : ToolEvent
    data class SetImportChallengesFileName(val importChallengesFileName: String) : ToolEvent
    data class SetExportSettingsFileName(val exportSettingsFileName: String) : ToolEvent
    data class SetImportSettingsFileName(val importSettingsFileName: String) : ToolEvent
    data class SetExportFolder(val exportFolder: String) : ToolEvent
    data class SetImportFolder(val importFolder: String) : ToolEvent
    data class SetBackupFolder(val backupFolder: String) : ToolEvent
    data class SetAllSessions(val allSessions: List<AbstractSession>) : ToolEvent
    data class SetAllLeads(val allLeads: List<Lead>) : ToolEvent
    data class SetAllDates(val allDates: List<Date>) : ToolEvent
    data class SetAllSets(val allSets: List<SingleSet>) : ToolEvent
    data class SetAllChallenges(val allChallenges: List<AchievedChallenge>) : ToolEvent
    data class SetAllSettings(val allSettings: List<Setting>) : ToolEvent // TODO: complete
    data class SetLastSessionAverageQuantity(val lastSessionAverageQuantity: String) : ToolEvent
    data class SetLastSessionsShown(val lastSessionsShown: String) : ToolEvent
    data class SetLastWeeksShown(val lastWeeksShown: String) : ToolEvent
    data class SetLastMonthsShown(val lastMonthsShown: String) : ToolEvent
    data class SetNotificationTime(val notificationTime: String) : ToolEvent
    data class SetExportHeader(val exportHeader: Boolean) : ToolEvent
    data class SetImportHeader(val importHeader: Boolean) : ToolEvent
    object SwitchBackupActive : ToolEvent
    data class SetLastBackup(val lastBackup: String) : ToolEvent
    object SwitchShowChangelog : ToolEvent
    object SwitchBackupBeforeUpdate : ToolEvent
    object SwitchGenerateiDate : ToolEvent
    object SwitchFollowCount : ToolEvent
    object SwitchSuggestLeadsNationality : ToolEvent
    data class SetShownNationalities(val shownNationalities: String) : ToolEvent
    object SwitchArchiveBackupFolder : ToolEvent
    object SwitchDeleteSessions : ToolEvent
    object SwitchDeleteLeads : ToolEvent
    object SwitchDeleteDates : ToolEvent
    object SwitchDeleteSets : ToolEvent
    object SwitchDeleteChallenges : ToolEvent
    object SwitchDeleteSettings : ToolEvent
    object SwitchIsCleaning : ToolEvent
    object SwitchThemeSysFollow : ToolEvent
    object SwitchSimplePlusOneReport : ToolEvent
    object SwitchNeverShareLeadInfo : ToolEvent
    object SwitchCopyReportOnClipboard : ToolEvent
    object SwitchShowCurrentWeekSummary : ToolEvent
    object SwitchShowCurrentMonthSummary : ToolEvent
    object SwitchShowCurrentChallengeSummary : ToolEvent
    data class SetIncrementChallengeGoal(val incrementChallengeGoal: String) : ToolEvent
    data class SetDefaultChallengeGoal(val defaultChallengeGoal: String) : ToolEvent
    data class SetTheme(val themeId: String) : ToolEvent
    data class SetDeleteConfirmationPrompt(val deleteConfirmationPrompt: String) : ToolEvent
    object DeleteAllSessions : ToolEvent
    object DeleteAllLeads : ToolEvent
    object DeleteAllDates : ToolEvent
    object DeleteAllSets : ToolEvent
    object DeleteAllChallenges : ToolEvent
    object DeleteAllSettings : ToolEvent

    object SwitchLiveSessionNotification : ToolEvent
    object SwitchLiveSessionSittingReminder : ToolEvent
    class SetLiveSessionSittingReminderInterval(val interval: String) : ToolEvent
    object SwitchLiveSessionShare : ToolEvent
}