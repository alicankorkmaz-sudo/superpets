package fun.superpets.mobile.screens.detail

import androidx.lifecycle.ViewModel
import fun.superpets.mobile.data.MuseumObject
import fun.superpets.mobile.data.MuseumRepository
import kotlinx.coroutines.flow.Flow

class DetailViewModel(private val museumRepository: MuseumRepository) : ViewModel() {
    fun getObject(objectId: Int): Flow<MuseumObject?> =
        museumRepository.getObjectById(objectId)
}
