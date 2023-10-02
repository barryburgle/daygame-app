package com.barryburgle.gameapp.ui.output

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.barryburgle.gameapp.dao.session.AbstractSessionDao
import com.barryburgle.gameapp.databinding.FragmentOutputBinding
import com.barryburgle.gameapp.model.session.AbstractSession

class OutputFragment : Fragment() {

    private var _binding: FragmentOutputBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val outputViewModel =
            ViewModelProvider(this).get(OutputViewModel::class.java)

        _binding = FragmentOutputBinding.inflate(inflater, container, false)
        val root: View = binding.root

        populateAllOutput()
        return root
    }

    private fun populateAllOutput() {
        val abstractSessionList: List<AbstractSession> = AbstractSessionDao.selectLastSession()
        if (abstractSessionList.isNotEmpty()) {
            setColumnDescriptors()
            setRowDescriptors()
            populateSingleOutput(
                binding.firstSessionTime,
                binding.firstApproachTime,
                binding.firstRejectionRatio,
                binding.firstContactRatio,
                binding.firstConvoRatio,
                binding.firstIndex,
                abstractSessionList.get(0)
            )
            populateSingleOutput(
                binding.secondSessionTime,
                binding.secondApproachTime,
                binding.secondRejectionRatio,
                binding.secondContactRatio,
                binding.secondConvoRatio,
                binding.secondIndex,
                abstractSessionList.get(1)
            )
            populateSingleOutput(
                binding.thirdSessionTime,
                binding.thirdApproachTime,
                binding.thirdRejectionRatio,
                binding.thirdContactRatio,
                binding.thirdConvoRatio,
                binding.thirdIndex,
                abstractSessionList.get(2)
            )
            populateSingleOutput(
                binding.fourthSessionTime,
                binding.fourthApproachTime,
                binding.fourthRejectionRatio,
                binding.fourthContactRatio,
                binding.fourthConvoRatio,
                binding.fourthIndex,
                abstractSessionList.get(3)
            )
        }
    }

    private fun populateSingleOutput(
        sessionTime: TextView,
        approachTime: TextView,
        rejectionRatio: TextView,
        contactRatio: TextView,
        convoRatio: TextView,
        index: TextView,
        abstractSession: AbstractSession
    ) {
        sessionTime.text =
            (abstractSession.sessionTime / 3600).toString() // TODO: do this conversion in service layer
        approachTime.text =
            (abstractSession.approachTime / 60).toString()// TODO: do this conversion in service layer
        rejectionRatio.text = String.format("%.2f", abstractSession.rejectionRatio)
        contactRatio.text = String.format("%.2f", abstractSession.contactRatio)
        convoRatio.text = String.format("%.2f", abstractSession.convoRatio)
        index.text = String.format("%.2f", abstractSession.index)
    }

    private fun setColumnDescriptors() {
        binding.firstSessionColumn.text = "Last Session: "
        binding.secondSessionColumn.text = "Two Sessions Ago: "
        binding.thirdSessionColumn.text = "Three Sessions Ago: "
        binding.fourthSessionColumn.text = "Four Sessions Ago: "
    }

    private fun setRowDescriptors() {
        binding.sessionTimeDesc.text = "Session Time: "
        binding.approachTimeDesc.text = "Approach Time: "
        binding.rejectionRatioDesc.text = "Rejection Ratio: "
        binding.contactRatioDesc.text = "Contact Ratio: "
        binding.convoRatioDesc.text = "Convo Ratio: "
        binding.indexDesc.text = "Index: "
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}