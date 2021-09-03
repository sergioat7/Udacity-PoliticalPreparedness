package com.example.android.politicalpreparedness.election

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class VoterInfoViewModel(
    private val electionId: Int,
    private val address: String,
    private val dataSource: ElectionDao
) : ViewModel() {

    private val _voterInfo = MutableLiveData<VoterInfoResponse>()
    val voterInfo: LiveData<VoterInfoResponse> = _voterInfo

    private val _isElectionFollowed = MutableLiveData<Boolean>()
    val isElectionFollowed: LiveData<Boolean> = _isElectionFollowed

    private val _url = MutableLiveData<String?>()
    val url: LiveData<String?> = _url

    private val _error = MutableLiveData<Int?>(null)
    val error: LiveData<Int?> = _error

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _isElectionFollowed.postValue(dataSource.getSingleElection(electionId) != null)
            try {
                val voterInfo = CivicsApi.retrofitService.getVoterInfo(address, electionId)
                _voterInfo.postValue(voterInfo)
            } catch (e: Exception) {
                _error.postValue(R.string.generic_error)
            }
        }
    }

    fun setUrl(value: String?) {
        _url.value = value
    }

    fun followUnfollowElection() {
        val election = voterInfo.value?.election ?: return
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (_isElectionFollowed.value == true) {
                    dataSource.deleteElection(voterInfo.value?.election)
                    _isElectionFollowed.postValue(false)
                } else {
                    dataSource.insertElection(election)
                    _isElectionFollowed.postValue(true)
                }
            } catch (e: Exception) {
                _error.postValue(R.string.generic_error)
            }
        }
    }
}