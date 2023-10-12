package com.barryburgle.gameapp.ui.input

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.barryburgle.gameapp.database.session.GameAppDatabase
import com.barryburgle.gameapp.databinding.FragmentInputBinding
import com.barryburgle.gameapp.model.session.AbstractSession
import com.barryburgle.gameapp.service.batch.BatchSessionService
import com.barryburgle.gameapp.worker.DatabaseWorker
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class InputFragment : Fragment() {

    private val INPUT_THREAD_NAME: String = "input_thread"
    private val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")
    private val batchSessionService = BatchSessionService()
    private var _binding: FragmentInputBinding? = null
    private var database: GameAppDatabase? = null
    private lateinit var databaseWorker: DatabaseWorker
    private val handler: Handler = Handler(Looper.getMainLooper())

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val inputViewModel =
            ViewModelProvider(this).get(InputViewModel::class.java)

        _binding = FragmentInputBinding.inflate(inflater, container, false)
        val root: View = binding.root

        startDatabaseWorker()
        database = activity?.applicationContext?.let { GameAppDatabase.getInstance(it) }

        listenInputClickEvent()
        return root
    }

    private fun startDatabaseWorker() {
        databaseWorker = DatabaseWorker(INPUT_THREAD_NAME)
        databaseWorker.start()
    }

    private fun listenInputClickEvent() {
        binding.button.setOnClickListener {
            val batchSession = batchSessionService.init(
                Instant.now(),
                LocalDate.parse(binding.date.text.toString(), dateFormatter),
                LocalTime.parse(binding.startHour.text.toString(), timeFormatter),
                LocalTime.parse(binding.endHour.text.toString(), timeFormatter),
                binding.sets.text.toString().toInt(),
                binding.convos.text.toString().toInt(),
                binding.contacts.text.toString().toInt(),
                binding.stickingPoints.text.toString()
            )
            insertSession(batchSession)
        }
    }

    private fun insertSession(abstractSession: AbstractSession) {
        val task = Runnable {
            database?.abstractSessionDao()?.insert(abstractSession)
            handler.post(
                { showToast("Recorded") }
            )
        }
        databaseWorker.postTask(task)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        GameAppDatabase.destroyInstance()
        databaseWorker.quit()
    }

    private fun showToast(message: String) {
        Toast.makeText(
            getActivity(), message,
            Toast.LENGTH_LONG
        ).show()
    }
}