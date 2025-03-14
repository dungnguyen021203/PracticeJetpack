package com.example.learntogether

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.learntogether.ui.theme.LearntogetherTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LearntogetherTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    ComposeQuadrant()
                }
            }
        }
    }
}

@Composable
fun ComposeQuadrant() {
    Column(Modifier.fillMaxWidth()) {
        Row(Modifier.weight(1f)) {
            AQuadrant(title = "1", description = "Hello", backgroundColor = Color(0xFFEADDFF), modifier = Modifier.weight(1f))
            AQuadrant(title = "2", description = "Helloo", backgroundColor = Color(0xFFD0BCFF), modifier = Modifier.weight(2f))
        }

        Row(Modifier.weight(2f)) {
            AQuadrant(title = "3", description = "Hellooo", backgroundColor = Color(0xFFB69DF8), modifier = Modifier.weight(1f))
            AQuadrant(title = "4", description = "Helloooo", backgroundColor = Color(0xFFF6EDFF), modifier = Modifier.weight(3f))
        }
    }
}


@Composable
private fun AQuadrant(title: String, description: String, modifier: Modifier = Modifier, backgroundColor: Color) {
    Column (modifier = modifier
        .fillMaxSize()
        .background(backgroundColor)
        .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally){
        Text(text = title, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 16.dp))
        Text(
            text = description, textAlign = TextAlign.Justify
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
    LearntogetherTheme {
        ComposeQuadrant()
    }
}