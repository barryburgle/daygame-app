package com.barryburgle.gameapp.ui.output

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        populateOutput()
        return root
    }

    private fun populateOutput() {
        val abstractSession: AbstractSession = AbstractSessionDao.selectLastSession()
        binding.sessionTimeDesc.text = "Session Time: "
        binding.sessionTime.text = abstractSession.sessionTime.toString()
        binding.approachTimeDesc.text = "Approach Time: "
        binding.approachTime.text = abstractSession.approachTime.toString()
        binding.rejectionRatioDesc.text = "Rejection Ratio: "
        binding.rejectionRatio.text = abstractSession.rejectionRatio.toString()
        binding.contactRatioDesc.text = "Contact Ratio: "
        binding.contactRatio.text = abstractSession.contactRatio.toString()
        binding.convoRatioDesc.text = "Convo Ratio: "
        binding.convoRatio.text = abstractSession.convoRatio.toString()
        binding.indexDesc.text = "Index: "
        binding.index.text = abstractSession.index.toString()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}