package com.ryccoatika.imagetotext.core.data

import com.ryccoatika.imagetotext.core.data.local.AppPreferences
import com.ryccoatika.imagetotext.core.data.local.LocalDataSource
import com.ryccoatika.imagetotext.domain.model.TextScanned
import com.ryccoatika.imagetotext.domain.repository.TextScannedRepository
import com.ryccoatika.imagetotext.core.utils.toTextScannedDomain
import com.ryccoatika.imagetotext.core.utils.toTextScannedEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TextScannedRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val appPreferences: AppPreferences
) : TextScannedRepository {
    override suspend fun getIsUserFirstTime(): Boolean =
        appPreferences.isFirstTime.first()


    override suspend fun setUserFirstTime(isFirstTime: Boolean) {
        appPreferences.setFirstTime(isFirstTime)
    }

    override suspend fun saveTextScanned(textScanned: TextScanned) =
        localDataSource.saveTextScanned(textScanned.toTextScannedEntity())

    override suspend fun removeTextScanned(textScanned: TextScanned) =
        localDataSource.removeTextScanned(textScanned.toTextScannedEntity())

    override fun getAllTextScanned(query: String?): Flow<List<TextScanned>> {
        return localDataSource.getAllTextScanned(query)
            .map { value -> value.map { it.toTextScannedDomain() } }
    }
}