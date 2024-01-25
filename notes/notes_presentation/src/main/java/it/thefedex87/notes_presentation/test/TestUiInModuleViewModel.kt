package it.thefedex87.notes_presentation.test

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import it.thefedex87.utils.Consts
import javax.inject.Inject

@HiltViewModel
class TestUiInModuleViewModel @Inject constructor(): ViewModel() {
    init {
        Log.d(Consts.TAG, "Init TestUiInModuleViewModel")
    }
}