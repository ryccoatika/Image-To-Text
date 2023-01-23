package com.ryccoatika.imagetotext.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ryccoatika.imagetotext.domain.usecase.GetIsUserFirstTime
import com.ryccoatika.imagetotext.domain.usecase.SetUserFirstTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    getIsUserFirstTime: GetIsUserFirstTime,
    setUserFirstTime: SetUserFirstTime
) : ViewModel() {
    val state: StateFlow<MainViewState> = getIsUserFirstTime(Unit)
        .onEach { isUserFirstTime ->
            if (isUserFirstTime) {
                setUserFirstTime.executeSync(SetUserFirstTime.Params(false))
            }
        }
        .map { isUserFirstTime ->
            MainViewState(isUserFirstTime)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = MainViewState.Empty
        )
}