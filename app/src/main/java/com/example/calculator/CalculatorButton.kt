package com.example.calculator

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Button labels in keypad order. "+/-" (sign change) and "%" (percentage,
 * our one bonus feature beyond the required set) are handled the same way
 * as any operator by [CalculatorViewModel.onButtonClick].
 */
val buttonList = listOf(
    "C", "AC", "%", "/",
    "7", "8", "9", "*",
    "4", "5", "6", "+",
    "1", "2", "3", "-",
    "+/-", "0", ".", "="
)

private const val GRID_COLUMNS = 4
private const val GRID_ROWS = 5

/**
 * Lays the keypad out in a [GRID_COLUMNS] x [GRID_ROWS] grid sized to
 * exactly fill the space given by [modifier] on any screen size, with no
 * empty strip left over and no scrolling needed. Buttons are square in
 * portrait; in short landscape layouts they become wide rather than
 * shrinking to stay square, which keeps the whole keypad on screen.
 */
@Composable
fun ButtonGrid(viewModel: CalculatorViewModel, modifier: Modifier = Modifier) {
    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val buttonWidth = maxWidth / GRID_COLUMNS
        val buttonHeight = maxHeight / GRID_ROWS
        LazyVerticalGrid(
            columns = GridCells.Fixed(GRID_COLUMNS),
            modifier = Modifier.fillMaxSize()
        ) {
            items(buttonList) { label ->
                CalculatorButton(
                    btn = label,
                    modifier = Modifier.width(buttonWidth).height(buttonHeight),
                    onClick = { viewModel.onButtonClick(label) }
                )
            }
        }
    }
}

@Composable
fun CalculatorButton(btn: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = modifier.border(1.dp, MaterialTheme.colorScheme.outlineVariant),
        shape = RectangleShape,
        colors = colorsFor(btn),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp, pressedElevation = 0.dp),
        contentPadding = PaddingValues(0.dp)
    ) {
        Text(text = btn, fontSize = 26.sp, fontWeight = FontWeight.Bold)
    }
}

/**
 * Colors buttons by role for visual hierarchy: clear/backspace/sign-change
 * are muted, operators and percent stand out, digits stay neutral, and
 * equals gets the strongest accent as the primary action.
 */
@Composable
private fun colorsFor(btn: String): ButtonColors {
    val scheme = MaterialTheme.colorScheme
    return when (btn) {
        "C", "AC", "+/-" -> ButtonDefaults.buttonColors(
            containerColor = scheme.secondaryContainer,
            contentColor = scheme.onSecondaryContainer
        )
        "/", "*", "+", "-", "%" -> ButtonDefaults.buttonColors(
            containerColor = scheme.tertiaryContainer,
            contentColor = scheme.onTertiaryContainer
        )
        "=" -> ButtonDefaults.buttonColors(
            containerColor = scheme.primary,
            contentColor = scheme.onPrimary
        )
        else -> ButtonDefaults.buttonColors(
            containerColor = scheme.surfaceVariant,
            contentColor = scheme.onSurfaceVariant
        )
    }
}
