package com.jmr.medhealth.ui.snellen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class SnellenLine(
    val acuity: String,
    val letters: String,
    val fontSize: Int
)

val snellenChartLines = listOf(
    SnellenLine("20/200", "E", 96),
    SnellenLine("20/100", "F P", 48),
    SnellenLine("20/80", "T O Z", 38),
    SnellenLine("20/60", "L P E D", 28),
    SnellenLine("20/40", "P E C F D", 19),
    SnellenLine("20/30", "E D F C Z P", 14),
    SnellenLine("20/25", "F E L O P Z D", 12),
    SnellenLine("20/20", "D E F P O T E C", 10)
)

@Composable
fun SnellenChartScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        snellenChartLines.forEach { line ->
            Text(
                text = line.letters,
                fontSize = line.fontSize.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                letterSpacing = (line.fontSize / 2).sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
        Text(
            text = "Stand 20 feet (6 meters) away from the screen for an accurate assessment.",
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 24.dp)
        )
    }
}