package com.barryburgle.gameapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.barryburgle.gameapp.databinding.FragmentHomeBinding
import com.barryburgle.gameapp.model.session.BatchSession
import com.barryburgle.gameapp.model.session.LiveSession
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        // TODO: integrate model session creation inside following method call
        listenSessionInsert()
        return root
    }

    private fun listenSessionInsert() {
        binding.button.setOnClickListener {
            val batchSession = BatchSession(
                Instant.now(),
                LocalDate.now(),
                LocalTime.of(6, 30),
                LocalTime.of(7, 30),
                binding.sets.text.toString().toInt(),
                binding.convos.text.toString().toInt(),
                binding.contacts.text.toString().toInt(),
                "stiking-points"
            )
            binding.textHome.text =
                "Approach Time Spent = ${batchSession.approachTime}\nContact Ratio = ${batchSession.contactRatio}"
        }
    }

    private fun computePercentage(numerator: Int, denominator: Int): Double {
        val result = numerator.toDouble() / denominator.toDouble()
        val percentage = result * 100
        return percentage
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}