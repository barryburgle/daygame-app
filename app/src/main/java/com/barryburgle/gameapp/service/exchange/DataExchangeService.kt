package com.barryburgle.gameapp.service.exchange

import android.content.Context
import android.widget.Toast
import com.barryburgle.gameapp.event.ToolEvent
import com.barryburgle.gameapp.model.date.Date
import com.barryburgle.gameapp.model.lead.Lead
import com.barryburgle.gameapp.model.session.AbstractSession
import com.barryburgle.gameapp.model.set.SingleSet
import com.barryburgle.gameapp.service.csv.CSVFindService
import com.barryburgle.gameapp.service.csv.DateCsvService
import com.barryburgle.gameapp.service.csv.LeadCsvService
import com.barryburgle.gameapp.service.csv.SessionCsvService
import com.barryburgle.gameapp.service.csv.SetCsvService
import com.barryburgle.gameapp.ui.state.ExportState
import com.barryburgle.gameapp.ui.tool.state.ToolsState
import java.io.FileNotFoundException

class DataExchangeService {

    companion object {

        val sessionCsvService: SessionCsvService = SessionCsvService()
        val leadCsvService: LeadCsvService = LeadCsvService()
        val dateCsvService: DateCsvService = DateCsvService()
        val setCsvService: SetCsvService = SetCsvService()

        fun backup(
            state: ExportState
        ) {
            backupAll(state)
            validateAll(state)
            cleanAllBackups(state)
        }

        fun cleanAllBackups(
            state: ExportState
        ) {
            sessionCsvService.cleanBackupFolder(
                state.exportFolder + "/" + state.backupFolder,
                state.lastBackup
            )
            leadCsvService.cleanBackupFolder(
                state.exportFolder + "/" + state.backupFolder,
                state.lastBackup
            )
            dateCsvService.cleanBackupFolder(
                state.exportFolder + "/" + state.backupFolder,
                state.lastBackup
            )
            setCsvService.cleanBackupFolder(
                state.exportFolder + "/" + state.backupFolder,
                state.lastBackup
            )
        }

        fun validateAll(
            state: ExportState
        ) {
            sessionCsvService.validateExport(
                state.exportFolder + "/" + state.backupFolder
            )
            leadCsvService.validateExport(
                state.exportFolder + "/" + state.backupFolder
            )
            dateCsvService.validateExport(
                state.exportFolder + "/" + state.backupFolder
            )
            setCsvService.validateExport(
                state.exportFolder + "/" + state.backupFolder
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
            exportFolder: String
        ) {
            sessionCsvService.setExportObjects(allSessions)
            sessionCsvService.exportRows(
                exportFolder,
                exportSessionsFileName,
                true
            )
            leadCsvService.setExportObjects(allLeads)
            leadCsvService.exportRows(
                exportFolder,
                exportLeadsFileName,
                true
            )
            dateCsvService.setExportObjects(allDates)
            dateCsvService.exportRows(
                exportFolder,
                exportDatesFileName,
                true
            )
            setCsvService.setExportObjects(allSets)
            setCsvService.exportRows(
                exportFolder,
                exportSetsFileName,
                true
            )
        }

        fun import(
            importSessionsFileName: String,
            importLeadsFileName: String,
            importDatesFileName: String,
            importSetsFileName: String,
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
        }

        fun backupAll(
            state: ExportState
        ) {
            export(
                state.allSessions,
                sessionCsvService.getBackupFileName(),
                state.allLeads,
                leadCsvService.getBackupFileName(),
                state.allDates,
                dateCsvService.getBackupFileName(),
                state.allSets,
                setCsvService.getBackupFileName(),
                state.exportFolder + "/" + state.backupFolder
            )
        }

        fun exportAll(
            state: ExportState,
            localContext: Context
        ) {
            export(
                state.allSessions,
                state.exportSessionsFileName,
                state.allLeads,
                state.exportLeadsFileName,
                state.allDates,
                state.exportDatesFileName,
                state.allSets,
                state.exportSetsFileName,
                state.exportFolder
            )
            Toast.makeText(
                localContext,
                "Successfully exported all tables",
                Toast.LENGTH_SHORT
            ).show()
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
                val importFolder =
                    if (!fromBackupFolder) state.importFolder else state.importFolder + "/" + state.backupFolder
                import(
                    importSessionsFileName,
                    importLeadsFileName,
                    importDatesFileName,
                    importSetsFileName,
                    importFolder,
                    true,
                    onEvent
                )
            } catch (fileNotFoundException: FileNotFoundException) {
                Toast.makeText(
                    localContext,
                    fileNotFoundException.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
            Toast.makeText(
                localContext,
                "Successfully imported all tables",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}