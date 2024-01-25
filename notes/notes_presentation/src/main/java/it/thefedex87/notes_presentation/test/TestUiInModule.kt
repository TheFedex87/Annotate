package it.thefedex87.notes_presentation.test

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun TestUiInModule(
    viewModel: TestUiInModuleViewModel = hiltViewModel<TestUiInModuleViewModel>()
) {
    Text(text = "This is a component inside a module")
}