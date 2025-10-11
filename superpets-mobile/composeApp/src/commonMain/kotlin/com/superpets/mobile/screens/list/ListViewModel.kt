package com.superpets.mobile.screens.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.superpets.mobile.core.dispatchers.DispatcherProvider
import com.superpets.mobile.data.MuseumObject
import com.superpets.mobile.data.MuseumRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn

class ListViewModel(
    museumRepository: MuseumRepository,
    dispatchers: DispatcherProvider
) : ViewModel() {
    val objects: StateFlow<List<MuseumObject>> =
        museumRepository.getObjects()
            .flowOn(dispatchers.io)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}
