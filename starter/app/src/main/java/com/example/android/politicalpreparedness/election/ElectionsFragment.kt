package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter

class ElectionsFragment : Fragment() {

    //TODO: Declare ViewModel
    private lateinit var upcomingAdapter: ElectionListAdapter
    private lateinit var savedAdapter: ElectionListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentElectionBinding.inflate(inflater)

        //TODO: Add ViewModel values and create ViewModel

        //TODO: Add binding values

        //TODO: Link elections to voter info

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

        //TODO: Populate recycler adapters

        return binding.root
    }

    //TODO: Refresh adapters when fragment loads

}