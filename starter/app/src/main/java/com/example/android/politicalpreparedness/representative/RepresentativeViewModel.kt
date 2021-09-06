package com.example.android.politicalpreparedness.representative

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.representative.model.Representative
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RepresentativeViewModel : ViewModel() {

    private val _address = MutableLiveData<Address?>(null)
    val address: LiveData<Address?> = _address

    private val _representatives = MutableLiveData<List<Representative>>()
    val representatives: LiveData<List<Representative>> = _representatives

    private val _error = MutableLiveData<Int?>(null)
    val error: LiveData<Int?> = _error

    fun getRepresentatives() {

        val formattedAddress = address.value?.toFormattedString() ?: return
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val representativeResponse =
                    CivicsApi.retrofitService.getRepresentatives(formattedAddress)
                val officials = representativeResponse.officials
                _representatives.postValue(representativeResponse.offices.flatMap {
                    it.getRepresentatives(
                        officials
                    )
                })
            } catch (e: Exception) {
                _error.postValue(R.string.generic_error)
            }
        }
    }

    fun setAddress(value: Address) {
        _address.value = value
    }

    fun setAddress(line1: String, line2: String?, city: String, state: String, zip: String) {
        _address.value = null
        if (line1.isEmpty() && city.isEmpty() && state.isEmpty() && zip.isEmpty()) return
        _address.value = Address(line1, line2, city, state, zip)
    }
}
