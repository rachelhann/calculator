package com.example.calculator

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/** Shows past calculations (newest first), lets the user clear them, and go back. */
@Composable
fun HistoryScreen(
    history: List<String>,
    onClear: () -> Unit,
    onBack: () -> Unit,
    darkTheme: Boolean,
    onToggleDarkTheme: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        CalculatorTopBar(
            title = "History",
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            },
            actions = {
                DarkModeToggle(darkTheme = darkTheme, onToggle = onToggleDarkTheme)
                if (history.isNotEmpty()) {
                    IconButton(onClick = onClear) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Clear History")
                    }
                }
            }
        )

        if (history.isEmpty()) {
            Text(
                text = "No calculations yet",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(16.dp)
            )
        } else {
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(history.asReversed()) { entry ->
                    Text(
                        text = entry,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    )
                    HorizontalDivider()
                }
            }
        }
    }
}
