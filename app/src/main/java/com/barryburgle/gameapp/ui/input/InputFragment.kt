package com.barryburgle.gameapp.ui.input

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.barryburgle.gameapp.databinding.FragmentInputBinding
import com.barryburgle.gameapp.model.session.BatchSession
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class InputFragment : Fragment() {

    private val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")
    private var _binding: FragmentInputBinding? = null

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

        // TODO: integrate model session creation inside following method call
        listenSessionInsert()
        return root
    }

    private fun listenSessionInsert() {
        binding.button.setOnClickListener {
            val batchSession = BatchSession(
                Instant.now(),
                LocalDate.parse(binding.date.text.toString(), dateFormatter),
                LocalTime.parse(binding.startHour.text.toString(), timeFormatter),
                LocalTime.parse(binding.endHour.text.toString(), timeFormatter),
                binding.sets.text.toString().toInt(),
                binding.convos.text.toString().toInt(),
                binding.contacts.text.toString().toInt(),
                binding.stickingPoints.text.toString()
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}