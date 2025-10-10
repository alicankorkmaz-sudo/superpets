package fun.superpets.mobile.data

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MuseumRepository(
    private val museumApi: MuseumApi,
    private val museumDao: MuseumDao,
) {
    private val scope = CoroutineScope(SupervisorJob())

    fun initialize() {
        scope.launch {
            refresh()
        }
    }

    suspend fun refresh() {
        museumDao.insertObjects(museumApi.getData())
    }

    fun getObjects(): Flow<List<MuseumObject>> = museumDao.getObjects()

    fun getObjectById(objectId: Int): Flow<MuseumObject?> = museumDao.getObjectById(objectId)
}
