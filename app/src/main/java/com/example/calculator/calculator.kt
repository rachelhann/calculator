package com.example.calculator

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

/**
 * Top-level screen: toggles between the keypad and the history list, and
 * switches between a stacked (portrait) and side-by-side (landscape) layout
 * so the keypad always fits on screen without scrolling.
 */
@Composable
fun Calculator(
    modifier: Modifier = Modifier,
    viewModel: CalculatorViewModel,
    darkTheme: Boolean,
    onToggleDarkTheme: () -> Unit
) {
    val equationText by viewModel.equationText.observeAsState("")
    val resultText by viewModel.resultText.observeAsState("0")
    val history by viewModel.history.observeAsState(emptyList())
    var showHistory by remember { mutableStateOf(false) }

    val topBarActions: @Composable RowScope.() -> Unit = {
        DarkModeToggle(darkTheme = darkTheme, onToggle = onToggleDarkTheme)
        HistoryButton(onClick = { showHistory = true })
    }

    Box(modifier = modifier) {
        if (showHistory) {
            HistoryScreen(
                history = history,
                onClear = { viewModel.clearHistory() },
                onBack = { showHistory = false },
                darkTheme = darkTheme,
                onToggleDarkTheme = onToggleDarkTheme
            )
        } else if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Row(modifier = Modifier.fillMaxSize()) {
                Column(modifier = Modifier.weight(1f).fillMaxHeight()) {
                    CalculatorTopBar(title = "", actions = topBarActions)
                    DisplaySection(
                        equation = equationText,
                        result = resultText,
                        modifier = Modifier.weight(1f)
                    )
                }
                ButtonGrid(
                    viewModel = viewModel,
                    modifier = Modifier.weight(1f).fillMaxHeight()
                )
            }
        } else {
            Column(modifier = Modifier.fillMaxSize()) {
                CalculatorTopBar(title = "", actions = topBarActions)
                DisplaySection(
                    equation = equationText,
                    result = resultText,
                    modifier = Modifier.weight(1f)
                )
                ButtonGrid(
                    viewModel = viewModel,
                    modifier = Modifier.fillMaxWidth().weight(1.6f)
                )
            }
        }
    }
}

@Composable
private fun HistoryButton(onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(imageVector = Icons.Default.History, contentDescription = "History")
    }
}

/** Lets the user override the system light/dark setting from within the app. */
@Composable
fun DarkModeToggle(darkTheme: Boolean, onToggle: () -> Unit) {
    IconButton(onClick = onToggle) {
        Icon(
            imageVector = if (darkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
            contentDescription = if (darkTheme) "Switch to light mode" else "Switch to dark mode"
        )
    }
}

/** The expression being typed (secondary emphasis) above the result (primary emphasis). */
@Composable
fun DisplaySection(equation: String, result: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.Bottom
    ) {
        Text(
            text = equation,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.End,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = result,
            style = MaterialTheme.typography.displayMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.End,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp, bottom = 12.dp)
        )
    }
}

/** Shared app bar so the calculator screen and history screen line up exactly. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculatorTopBar(
    title: String,
    navigationIcon: @Composable (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {}
) {
    TopAppBar(
        title = {
            if (title.isNotEmpty()) {
                Text(text = title, fontWeight = FontWeight.Bold)
            }
        },
        navigationIcon = { navigationIcon?.invoke() },
        actions = actions
    )
}
