package it.thefedex87.notes_presentation.test

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import it.thefedex87.core_ui.LocalSpacing

@Composable
fun TestUiInModule(
    viewModel: TestUiInModuleViewModel = hiltViewModel<TestUiInModuleViewModel>()
) {
    Text(
        modifier = Modifier.padding(LocalSpacing.current.spaceMedium),
        text = "This is a component inside a module"
    )
}