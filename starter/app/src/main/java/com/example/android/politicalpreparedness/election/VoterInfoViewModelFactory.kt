package com.example.android.politicalpreparedness.election

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.database.ElectionDao

class VoterInfoViewModelFactory(
    private val electionId: Int,
    private val address: String,
    private val dataSource: ElectionDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VoterInfoViewModel::class.java)) {
            return VoterInfoViewModel(electionId, address, dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}