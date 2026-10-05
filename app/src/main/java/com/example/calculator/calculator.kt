package com.example.calculator

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val buttonList = listOf(
    "C", "AC","%","/",
    "7","8","9","*",
    "4","5","6","+",
    "1","2","3","-",
    "+/-","0",".","="
)
@Composable
fun Calculator(
    modifier: Modifier = Modifier, viewModel: CalculatorViewModel) {

    val equationText = viewModel.equationText.observeAsState()
    val resultText = viewModel.resultText.observeAsState()
    val history = viewModel.history.observeAsState(emptyList())
    var showHistory by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        if (showHistory) {
            HistoryScreen(
                history = history.value,
                onClear = { viewModel.clearHistory() },
                onBack = { showHistory = false }
            )
        } else {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.End
            ) {
                CalculatorTopBar(
                    title = "",
                    actions = {
                        IconButton(onClick = { showHistory = true }) {
                            Icon(imageVector = Icons.Default.History, contentDescription = "History")
                        }
                    }
                )

                Text(
                    text = equationText.value?:"",
                    style = TextStyle(
                        fontSize = 30.sp,
                        textAlign = TextAlign.End
                    ),
                    maxLines = 5,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = resultText.value ?: "",
                    style = TextStyle(
                        fontSize = 60.sp,
                        textAlign = TextAlign.End
                    ),
                    maxLines = 2,
                )

                Spacer(modifier = Modifier.height(10.dp))

                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                ) {
                    items(buttonList){
                        CalculatorButton(btn = it, onClick = {
                            viewModel.onButtonClick(it)
                        })
                    }
                }

            }
        }
    }
}

@Composable
fun CalculatorTopBar(
    title: String,
    navigationIcon: @Composable (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (navigationIcon != null) {
            navigationIcon()
        } else {
            Spacer(modifier = Modifier.width(48.dp))
        }
        Text(
            text = title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp)
        )
        actions()
    }
}

@Composable
fun HistoryScreen(history: List<String>, onClear: () -> Unit, onBack: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        CalculatorTopBar(
            title = "History",
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            },
            actions = {
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
                color = Color.Gray,
                modifier = Modifier.padding(16.dp)
            )
        } else {
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(history.asReversed()) { entry ->
                    Text(
                        text = entry,
                        fontSize = 20.sp,
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

@Composable
fun CalculatorButton(btn : String, onClick: ()-> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .background(getColor(btn))
            .border(1.dp, Color.LightGray)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(text = btn, fontSize = 30.sp, fontWeight = FontWeight.Bold, color = Color.Black)
    }
}

fun getColor(btn : String) : Color {
    if(btn == "C" || btn == "AC")
        return Color(0xFFE3E3E3)
    if(btn == "/" || btn == "*" || btn == "+" || btn == "-" || btn == "=" || btn == "%")
        return Color(0xFFF2F2F2)
    return Color(0xFFF1F1F1)
}
