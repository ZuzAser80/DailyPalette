package com.zuzaser.dailypalette

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zuzaser.dailypalette.model.PaletteModel
import com.zuzaser.dailypalette.ui.theme.DailyPaletteTheme
import com.zuzaser.dailypalette.viewmodel.PaletteViewModel
import kotlin.random.Random
//IMPORTANT
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.setValue

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent() {
            var showDialog by remember { mutableStateOf(generateRandomPalette()) }
            DailyPaletteTheme {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(25.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = { showDialog = generateRandomPalette() },
                        shape = RoundedCornerShape(25.dp),
                    ) {
                        Text("Generate Palette", fontSize = 25.sp)
                    }
                    for (i in showDialog.getColors()) {
                        key(i) {
                            PaletteViewModel().GetView(i)
                        }
                    }
                }
            }
        }
    }
}

fun generateRandomPalette() : PaletteModel {
    return PaletteModel(
        Random.nextInt(255),
        String.format("#%06X", Random.nextInt(0xFFFFFF + 1)),
        String.format("#%06X", Random.nextInt(0xFFFFFF + 1)),
        String.format("#%06X", Random.nextInt(0xFFFFFF + 1)),
        String.format("#%06X", Random.nextInt(0xFFFFFF + 1)),
        String.format("#%06X", Random.nextInt(0xFFFFFF + 1)),
        String.format("#%06X", Random.nextInt(0xFFFFFF + 1)),
        String.format("%06X", Random.nextInt(0xFFFFFF + 1))
    )
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DailyPaletteTheme {
        Greeting("Android")
    }
}