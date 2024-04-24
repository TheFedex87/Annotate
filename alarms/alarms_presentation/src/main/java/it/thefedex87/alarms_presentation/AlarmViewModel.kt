package it.thefedex87.alarms_presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlarmViewModel @Inject constructor(

): ViewModel() {
    private val _state = MutableStateFlow(AlarmState())
    val state = _state.asStateFlow()

    fun onEvent(event: AlarmEvent) {
        viewModelScope.launch {
            when(event) {
                is AlarmEvent.OnTimeClicked -> {
                    _state.update {
                        it.copy(
                            showTimePicker = true
                        )
                    }
                }
                is AlarmEvent.OnDateClicked -> {

                }
            }
        }
    }
}