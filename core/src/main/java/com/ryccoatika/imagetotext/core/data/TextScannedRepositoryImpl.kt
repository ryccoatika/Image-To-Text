package com.ryccoatika.imagetotext.core.data

import android.net.Uri
import android.util.Size
import com.ryccoatika.imagetotext.core.data.local.AppPreferences
import com.ryccoatika.imagetotext.core.data.local.LocalDataSource
import com.ryccoatika.imagetotext.core.data.local.entity.TextScannedEntity
import com.ryccoatika.imagetotext.core.utils.toTextScannedDomain
import com.ryccoatika.imagetotext.core.utils.toTextScannedEntity
import com.ryccoatika.imagetotext.domain.model.TextRecognized
import com.ryccoatika.imagetotext.domain.model.TextScanned
import com.ryccoatika.imagetotext.domain.repository.TextScannedRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

@Singleton
class TextScannedRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val appPreferences: AppPreferences,
) : TextScannedRepository {
    override suspend fun getIsUserFirstTime(): Boolean =
        appPreferences.isFirstTime.first()

    override suspend fun setUserFirstTime(isFirstTime: Boolean) {
        appPreferences.setFirstTime(isFirstTime)
    }

    override suspend fun saveTextScanned(
        imageUri: Uri,
        imageSize: Size,
        textRecognized: TextRecognized,
        text: String,
    ): TextScanned =
        localDataSource.saveTextScanned(
            TextScannedEntity(
                imageUri = imageUri,
                imageSize = imageSize,
                textRecognized = textRecognized,
                text = text,
            ),
        ).toTextScannedDomain()

    override suspend fun removeTextScanned(textScanned: TextScanned) =
        localDataSource.removeTextScanned(textScanned.toTextScannedEntity())

    override fun getAllTextScanned(query: String?): Flow<List<TextScanned>> {
        return localDataSource.getAllTextScanned(query)
            .map { value -> value.map { it.toTextScannedDomain() } }
    }

    override suspend fun getTextScanned(id: Long): TextScanned {
        return localDataSource.getTextScanned(id).toTextScannedDomain()
    }
}
