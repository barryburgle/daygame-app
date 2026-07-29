package com.barryburgle.gameapp.service.exchange

import android.content.Context
import android.widget.Toast
import com.barryburgle.gameapp.event.ToolEvent
import com.barryburgle.gameapp.model.challenge.AchievedChallenge
import com.barryburgle.gameapp.model.date.Date
import com.barryburgle.gameapp.model.lead.Lead
import com.barryburgle.gameapp.model.session.AbstractSession
import com.barryburgle.gameapp.model.session.PinPoint
import com.barryburgle.gameapp.model.set.SingleSet
import com.barryburgle.gameapp.model.setting.Setting
import com.barryburgle.gameapp.service.csv.AbstractCsvService
import com.barryburgle.gameapp.service.csv.CSVFindService
import com.barryburgle.gameapp.service.csv.ChallengeCsvService
import com.barryburgle.gameapp.service.csv.DateCsvService
import com.barryburgle.gameapp.service.csv.LeadCsvService
import com.barryburgle.gameapp.service.csv.PinPointCsvService
import com.barryburgle.gameapp.service.csv.SessionCsvService
import com.barryburgle.gameapp.service.csv.SetCsvService
import com.barryburgle.gameapp.service.csv.SettingCsvService
import com.barryburgle.gameapp.ui.state.ExportState
import com.barryburgle.gameapp.ui.tool.state.ToolsState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.FileNotFoundException

class DataExchangeService {

    companion object {

        val sessionCsvService: SessionCsvService = SessionCsvService()
        val leadCsvService: LeadCsvService = LeadCsvService()
        val dateCsvService: DateCsvService = DateCsvService()
        val setCsvService: SetCsvService = SetCsvService()
        val challengeCsvService: ChallengeCsvService = ChallengeCsvService()
        val pinPointCsvService: PinPointCsvService = PinPointCsvService()
        val settingCsvService: SettingCsvService = SettingCsvService()

        suspend fun backup(
            state: ExportState
        ) {
            withContext(Dispatchers.IO) {
                backupAllAndClean(
                    state.allSessions,
                    sessionCsvService.getBackupFileName(),
                    state.allLeads,
                    leadCsvService.getBackupFileName(),
                    state.allDates,
                    dateCsvService.getBackupFileName(),
                    state.allSets,
                    setCsvService.getBackupFileName(),
                    state.allChallenges,
                    challengeCsvService.getBackupFileName(),
                    state.allPinPoints,
                    pinPointCsvService.getBackupFileName(),
                    state.allSettings,
                    settingCsvService.getBackupFileName(),
                    state.exportFolder + "/" + state.backupFolder,
                    true,
                    state.lastBackup
                )
            }
        }

        fun <T : Any> backupAndClean(
            service: AbstractCsvService<T>,
            objects: List<T>,
            exportFolder: String,
            fileName: String,
            exportHeader: Boolean,
            lastBackup: Int,
            incrementalBackup: Boolean,
            clean: Boolean,
        ) {
            fun exportValidAndClean() {
                val justExportedIsValid =
                    exportAndValidate(service, exportFolder, fileName, exportHeader)
                if (justExportedIsValid && clean) {
                    service.cleanBackupFolder(exportFolder, lastBackup)
                }
            }

            service.setExportObjects(objects)
            if (!incrementalBackup || !service.validateExportedFileAgainstExportState(
                    exportFolder,
                    true
                )
            ) {
                exportValidAndClean()
            }
        }

        fun <T : Any> exportAndValidate(
            service: AbstractCsvService<T>,
            exportFolder: String,
            fileName: String,
            exportHeader: Boolean,
            objects: List<T>? = null
        ): Boolean {
            if (objects != null) {
                service.setExportObjects(objects)
            }
            service.exportRows(exportFolder, fileName, exportHeader)
            val justExportedIsValid =
                service.validateExportedFileAgainstExportState(exportFolder, exportHeader)
            return justExportedIsValid
        }

        fun backupAllAndClean(
            allSessions: List<AbstractSession>,
            exportSessionsFileName: String,
            allLeads: List<Lead>,
            exportLeadsFileName: String,
            allDates: List<Date>,
            exportDatesFileName: String,
            allSets: List<SingleSet>,
            exportSetsFileName: String,
            allChallenges: List<AchievedChallenge>,
            exportChallengesFileName: String,
            allPinPoints: List<PinPoint>,
            exportPinPointsFileName: String,
            allSettings: List<Setting>,
            exportSettingsFileName: String,
            exportFolder: String,
            exportHeader: Boolean,
            lastBackup: Int
        ) {
            backupAndClean(
                sessionCsvService,
                allSessions,
                exportFolder,
                exportSessionsFileName,
                exportHeader,
                lastBackup,
                true,
                true
            )
            backupAndClean(
                leadCsvService,
                allLeads,
                exportFolder,
                exportLeadsFileName,
                exportHeader,
                lastBackup,
                true,
                true
            )
            backupAndClean(
                dateCsvService,
                allDates,
                exportFolder,
                exportDatesFileName,
                exportHeader,
                lastBackup,
                true,
                true
            )
            backupAndClean(
                setCsvService,
                allSets,
                exportFolder,
                exportSetsFileName,
                exportHeader,
                lastBackup,
                true,
                true
            )
            backupAndClean(
                challengeCsvService,
                allChallenges,
                exportFolder,
                exportChallengesFileName,
                exportHeader,
                lastBackup,
                true,
                true
            )
            backupAndClean(
                pinPointCsvService,
                allPinPoints,
                exportFolder,
                exportPinPointsFileName,
                exportHeader,
                lastBackup,
                true,
                true
            )
            backupAndClean(
                settingCsvService,
                allSettings,
                exportFolder,
                exportSettingsFileName,
                exportHeader,
                lastBackup,
                true,
                true
            )
        }

        fun export(
            allSessions: List<AbstractSession>,
            exportSessionsFileName: String,
            allLeads: List<Lead>,
            exportLeadsFileName: String,
            allDates: List<Date>,
            exportDatesFileName: String,
            allSets: List<SingleSet>,
            exportSetsFileName: String,
            allChallenges: List<AchievedChallenge>,
            exportChallengesFileName: String,
            allPinPoints: List<PinPoint>,
            exportPinPointsFileName: String,
            allSettings: List<Setting>,
            exportSettingsFileName: String,
            exportFolder: String,
            exportHeader: Boolean
        ): Boolean {
            val validSessionExport = exportAndValidate(
                sessionCsvService,
                exportFolder, exportSessionsFileName, exportHeader,
                allSessions
            )
            val validLeadExport = exportAndValidate(
                leadCsvService,
                exportFolder, exportLeadsFileName, exportHeader,
                allLeads
            )
            val validDateExport = exportAndValidate(
                dateCsvService,
                exportFolder, exportDatesFileName, exportHeader,
                allDates
            )
            val validSetExport = exportAndValidate(
                setCsvService,
                exportFolder, exportSetsFileName, exportHeader,
                allSets
            )
            val validChallengeExport = exportAndValidate(
                challengeCsvService,
                exportFolder, exportChallengesFileName, exportHeader,
                allChallenges
            )
            val validPinPointExport = exportAndValidate(
                pinPointCsvService,
                exportFolder, exportPinPointsFileName, exportHeader,
                allPinPoints
            )
            val validSettingExport = exportAndValidate(
                settingCsvService,
                exportFolder, exportSettingsFileName, exportHeader,
                allSettings
            )
            return validSessionExport && validLeadExport && validDateExport && validSetExport && validChallengeExport && validPinPointExport && validSettingExport
        }

        fun import(
            importSessionsFileName: String,
            importLeadsFileName: String,
            importDatesFileName: String,
            importSetsFileName: String,
            importChallengesFileName: String,
            importPinPointsFileName: String,
            importSettingsFileName: String,
            importFolder: String,
            importHeader: Boolean,
            onEvent: (ToolEvent) -> Unit
        ) {
            onEvent(
                ToolEvent.SetAllSessions(
                    sessionCsvService.importRows(
                        importFolder,
                        importSessionsFileName,
                        importHeader
                    )
                )
            )
            onEvent(
                ToolEvent.SetAllLeads(
                    leadCsvService.importRows(
                        importFolder,
                        importLeadsFileName,
                        importHeader
                    )
                )
            )
            onEvent(
                ToolEvent.SetAllDates(
                    dateCsvService.importRows(
                        importFolder,
                        importDatesFileName,
                        importHeader
                    )
                )
            )
            onEvent(
                ToolEvent.SetAllSets(
                    setCsvService.importRows(
                        importFolder,
                        importSetsFileName,
                        importHeader
                    )
                )
            )
            onEvent(
                ToolEvent.SetAllChallenges(
                    challengeCsvService.importRows(
                        importFolder,
                        importChallengesFileName,
                        importHeader
                    )
                )
            )
            onEvent(
                ToolEvent.SetAllPinPoints(
                    pinPointCsvService.importRows(
                        importFolder,
                        importPinPointsFileName,
                        importHeader
                    )
                )
            )
            onEvent(
                ToolEvent.SetAllSettings(
                    settingCsvService.importRows(
                        importFolder,
                        importSettingsFileName,
                        importHeader
                    )
                )
            )
        }

        fun exportAll(
            state: ExportState,
            exportHeader: Boolean,
            localContext: Context
        ) {
            val validExport = export(
                state.allSessions,
                state.exportSessionsFileName,
                state.allLeads,
                state.exportLeadsFileName,
                state.allDates,
                state.exportDatesFileName,
                state.allSets,
                state.exportSetsFileName,
                state.allChallenges,
                state.exportChallengesFileName,
                state.allPinPoints,
                state.exportPinPointsFileName,
                state.allSettings,
                state.exportSettingsFileName,
                state.exportFolder,
                exportHeader
            )
            if (validExport) {
                Toast.makeText(
                    localContext,
                    "Successfully exported all tables",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    localContext,
                    "Error: partially exported data",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        fun importAll(
            state: ToolsState,
            fromBackupFolder: Boolean,
            csvFindService: CSVFindService,
            localContext: Context,
            onEvent: (ToolEvent) -> Unit
        ) {
            try {
                val importSessionsFileName =
                    if (!fromBackupFolder) state.importSessionsFileName else csvFindService.getLastFilenameInFolder(
                        state.importFolder + "/" + state.backupFolder,
                        "session"
                    )
                val importLeadsFileName =
                    if (!fromBackupFolder) state.importLeadsFileName else csvFindService.getLastFilenameInFolder(
                        state.importFolder + "/" + state.backupFolder,
                        "lead"
                    )
                val importDatesFileName =
                    if (!fromBackupFolder) state.importDatesFileName else csvFindService.getLastFilenameInFolder(
                        state.importFolder + "/" + state.backupFolder,
                        "date"
                    )
                val importSetsFileName =
                    if (!fromBackupFolder) state.importSetsFileName else csvFindService.getLastFilenameInFolder(
                        state.importFolder + "/" + state.backupFolder,
                        "set"
                    )
                val importChallengesFileName =
                    if (!fromBackupFolder) state.importChallengesFileName else csvFindService.getLastFilenameInFolder(
                        state.importFolder + "/" + state.backupFolder,
                        "challenge"
                    )
                val importPinPointsFileName =
                    if (!fromBackupFolder) state.importPinPointsFileName else csvFindService.getLastFilenameInFolder(
                        state.importFolder + "/" + state.backupFolder,
                        "pinpoint"
                    )
                val importSettingsFileName =
                    if (!fromBackupFolder) state.importSettingsFileName else csvFindService.getLastFilenameInFolder(
                        state.importFolder + "/" + state.backupFolder,
                        "setting"
                    )
                val importFolder =
                    if (!fromBackupFolder) state.importFolder else state.importFolder + "/" + state.backupFolder
                import(
                    importSessionsFileName,
                    importLeadsFileName,
                    importDatesFileName,
                    importSetsFileName,
                    importChallengesFileName,
                    importPinPointsFileName,
                    importSettingsFileName,
                    importFolder,
                    true,
                    onEvent
                )
                Toast.makeText(
                    localContext,
                    "Successfully imported all tables",
                    Toast.LENGTH_SHORT
                ).show()
            } catch (fileNotFoundException: FileNotFoundException) {
                Toast.makeText(
                    localContext,
                    fileNotFoundException.message,
                    Toast.LENGTH_SHORT
                ).show()
            } catch (indexOutOfBoundsException: IndexOutOfBoundsException) {
                Toast.makeText(
                    localContext,
                    "Impossible to import backup: no files in backup folder",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}