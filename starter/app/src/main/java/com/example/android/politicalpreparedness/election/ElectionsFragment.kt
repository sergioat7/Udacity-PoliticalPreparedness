package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.google.android.material.snackbar.Snackbar

class ElectionsFragment : Fragment() {

    private lateinit var viewModel: ElectionsViewModel
    private lateinit var upcomingAdapter: ElectionListAdapter
    private lateinit var savedAdapter: ElectionListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentElectionBinding.inflate(inflater)

        val electionDao = ElectionDatabase.getInstance(requireActivity().application).electionDao
        viewModel = ViewModelProvider(
            this,
            ElectionsViewModelFactory(electionDao)
        ).get(ElectionsViewModel::class.java)

        upcomingAdapter = ElectionListAdapter(ElectionListAdapter.ElectionListener {
            findNavController().navigate(
                ElectionsFragmentDirections.toVoterInfo(
                    it.id,
                    it.division
                )
            )
        })
        binding.rvUpcomingElections.adapter = upcomingAdapter

        savedAdapter = ElectionListAdapter(ElectionListAdapter.ElectionListener {
            findNavController().navigate(
                ElectionsFragmentDirections.toVoterInfo(
                    it.id,
                    it.division
                )
            )
        })
        binding.rvSavedElection.adapter = savedAdapter

        viewModel.upcomingElections.observe(viewLifecycleOwner, {
            upcomingAdapter.submitList(it)
        })

        viewModel.savedElections.observe(viewLifecycleOwner, {
            savedAdapter.submitList(it)
        })

        viewModel.error.observe(viewLifecycleOwner, {
            it?.let {
                Snackbar.make(requireView(), it, Snackbar.LENGTH_LONG).show()
            }
        })

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshList()
    }
}