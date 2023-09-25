package com.barryburgle.gameapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.barryburgle.gameapp.databinding.FragmentHomeBinding

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
            val sets: Int = binding.sets.text.toString().toInt()
            val convos: Int = binding.convos.text.toString().toInt()
            val contacts: Int = binding.contacts.text.toString().toInt()
            val convoRatio: Double = computePercentage(convos, sets)
            val contactRatio: Double = computePercentage(contacts, sets)
            binding.textHome.text = "Convo Ratio = $convoRatio\nContact Ratio = $contactRatio"
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