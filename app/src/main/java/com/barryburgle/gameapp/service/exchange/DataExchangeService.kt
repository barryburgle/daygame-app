package com.barryburgle.gameapp.service.exchange

import android.content.Context
import android.widget.Toast
import com.barryburgle.gameapp.model.date.Date
import com.barryburgle.gameapp.model.lead.Lead
import com.barryburgle.gameapp.model.session.AbstractSession
import com.barryburgle.gameapp.service.csv.DateCsvService
import com.barryburgle.gameapp.service.csv.LeadCsvService
import com.barryburgle.gameapp.service.csv.SessionCsvService
import com.barryburgle.gameapp.ui.state.OrderState

class DataExchangeService {

    companion object {

        val sessionCsvService: SessionCsvService = SessionCsvService()
        val leadCsvService: LeadCsvService = LeadCsvService()
        val dateCsvService: DateCsvService = DateCsvService()

        fun backup(
            state: OrderState,
            localContext: Context
        ) {
            backupAll(state, localContext)
            validateAll(state)
            cleanAllBackups(state)
        }

        fun cleanAllBackups(
            state: OrderState
        ) {
            sessionCsvService.cleanBackupFolder(
                state.exportFolder + "/" + state.backupFolder
            )
            leadCsvService.cleanBackupFolder(
                state.exportFolder + "/" + state.backupFolder
            )
            dateCsvService.cleanBackupFolder(
                state.exportFolder + "/" + state.backupFolder
            )
        }

        fun validateAll(
            state: OrderState
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
        }

        fun export(
            allSessions: List<AbstractSession>,
            exportSessionsFileName: String,
            allLeads: List<Lead>,
            exportLeadsFileName: String,
            allDates: List<Date>,
            exportDatesFileName: String,
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
        }

        fun backupAll(
            state: OrderState,
            localContext: Context,
        ) {
            export(
                state.allSessions,
                sessionCsvService.getBackupFileName(),
                state.allLeads,
                leadCsvService.getBackupFileName(),
                state.allDates,
                dateCsvService.getBackupFileName(),
                state.exportFolder + "/" + state.backupFolder
            )
        }

        fun exportAll(
            state: OrderState,
            localContext: Context
        ) {
            export(
                state.allSessions,
                state.exportSessionsFileName,
                state.allLeads,
                state.exportLeadsFileName,
                state.allDates,
                state.exportDatesFileName,
                state.exportFolder
            )
            Toast.makeText(
                localContext,
                "Successfully exported all tables",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}