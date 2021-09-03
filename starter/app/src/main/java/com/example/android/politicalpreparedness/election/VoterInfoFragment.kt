package com.example.android.politicalpreparedness.election

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding
import com.google.android.material.snackbar.Snackbar

class VoterInfoFragment : Fragment() {

    private lateinit var viewModel: VoterInfoViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentVoterInfoBinding.inflate(inflater)

        val electionId = VoterInfoFragmentArgs.fromBundle(requireArguments()).argElectionId
        val division = VoterInfoFragmentArgs.fromBundle(requireArguments()).argDivision

        val address = "${division.country} ${division.state}"
        val electionDao = ElectionDatabase.getInstance(requireActivity().application).electionDao
        viewModel = ViewModelProvider(
            this,
            VoterInfoViewModelFactory(electionId, address, electionDao)
        ).get(VoterInfoViewModel::class.java)

        binding.also {
            it.viewModel = viewModel
            it.lifecycleOwner = this
        }

        viewModel.url.observe(viewLifecycleOwner, {
            openUrl(it)
        })

        viewModel.error.observe(viewLifecycleOwner, {
            it?.let {
                Snackbar.make(requireView(), it, Snackbar.LENGTH_LONG).show()
            }
        })

        return binding.root
    }

    private fun openUrl(url: String?) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browserIntent)
    }

}