package com.barryburgle.gameapp.service.exchange

import android.content.Context
import android.widget.Toast
import com.barryburgle.gameapp.service.csv.DateCsvService
import com.barryburgle.gameapp.service.csv.LeadCsvService
import com.barryburgle.gameapp.service.csv.SessionCsvService
import com.barryburgle.gameapp.ui.state.OrderState

class DataExchangeService {

    companion object {
        val sessionCsvService: SessionCsvService = SessionCsvService()
        val leadCsvService: LeadCsvService = LeadCsvService()
        val dateCsvService: DateCsvService = DateCsvService()

        fun exportAll(
            state: OrderState,
            localContext: Context
        ) {
            sessionCsvService.setExportObjects(state.abstractSessions)
            sessionCsvService.exportRows(
                state.exportFolder,
                state.exportSessionsFileName,
                true
            )
            leadCsvService.setExportObjects(state.leads)
            leadCsvService.exportRows(
                state.exportFolder,
                state.exportLeadsFileName,
                true
            )
            dateCsvService.setExportObjects(state.dates)
            dateCsvService.exportRows(
                state.exportFolder,
                state.exportDatesFileName,
                true
            )
            Toast.makeText(
                localContext,
                "Successfully exported all tables",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}