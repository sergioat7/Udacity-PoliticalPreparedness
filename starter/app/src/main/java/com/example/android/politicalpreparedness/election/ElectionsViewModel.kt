package com.example.android.politicalpreparedness.election

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Election
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ElectionsViewModel(private val dataSource: ElectionDao) : ViewModel() {

    private val _upcomingElections = MutableLiveData<List<Election>>()
    val upcomingElections: LiveData<List<Election>> = _upcomingElections

    private val _savedElections = MutableLiveData<List<Election>>()
    val savedElections: LiveData<List<Election>> = _savedElections

    private val _error = MutableLiveData<Int?>(null)
    val error: LiveData<Int?> = _error

    init {
        refreshList()
    }

    fun refreshList() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _upcomingElections.postValue(CivicsApi.retrofitService.getElections().elections)
                _savedElections.postValue(dataSource.getAllElections())
            } catch (e: Exception) {
                _error.postValue(R.string.generic_error)
            }
        }
    }
}