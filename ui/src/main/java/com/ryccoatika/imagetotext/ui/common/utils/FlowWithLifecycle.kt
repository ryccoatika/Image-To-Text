package com.ryccoatika.imagetotext.ui.common.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.StateFlow

@Composable
fun <T> rememberStateWithLifecycle(
    stateFlow: StateFlow<T>,
    lifecycle: Lifecycle = LocalLifecycleOwner.current.lifecycle,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
): State<T> {
    val initialValue = remember(stateFlow) { stateFlow.value }
    return produceState(
        key1 = stateFlow,
        key2 = lifecycle,
        key3 = minActiveState,
        initialValue = initialValue,
    ) {
        lifecycle.repeatOnLifecycle(minActiveState) {
            stateFlow.collect {
                this@produceState.value = it
            }
        }
    }
}
