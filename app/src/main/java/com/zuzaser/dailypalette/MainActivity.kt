package com.zuzaser.dailypalette

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        var paletteModel : PaletteModel = PaletteModel(1, "#507255", "#488B49", "#4AAD52", "#6EB257", "#C5E063", "#40476D", "name")
        setContent() {
            val showDialog = remember { mutableStateOf(false) }
            DailyPaletteTheme {
                if (showDialog.value) {

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(25.dp)
                            ,
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        for (i in paletteModel.getColors()) {
                            PaletteViewModel().GetView(i)
                        }
                        Button(onClick = { showDialog.value = false; paletteModel = generateRandomPalette(); return@Button; }) { }

                    }
                    //
                }
                Button(onClick = { paletteModel = generateRandomPalette(); showDialog.value = true; },
                    shape = RoundedCornerShape(25.dp)) {
                    Text("Generate Palette", fontSize = 25.sp)
                }
            }
        }
    }
}

//DailyPaletteTheme {
//    println(" ::::: " + showDialog.color0)
//    if (showDialog != paletteModel) {
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(25.dp),
//            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            for (i in showDialog.getColors()) {
//                PaletteViewModel().GetView(i)
//            }
//            //Button(onClick = { showDialog.value = false; paletteModel = generateRandomPalette(); return@Button; showDialog.value = true; }) { }
//        }
//    }
//    Button(onClick = { showDialog = generateRandomPalette(); },
//        shape = RoundedCornerShape(25.dp)) {
//        Text("Generate Palette", fontSize = 25.sp)
//    }
//}

fun generateRandomPalette() : PaletteModel {
    var id = Random.nextInt(255)
    var name = String.format("%06X", Random.nextInt(0xFFFFFF + 1))
    var color0 = String.format("#%06X", Random.nextInt(0xFFFFFF + 1))
    var color1 = String.format("#%06X", Random.nextInt(0xFFFFFF + 1))
    var color2 = String.format("#%06X", Random.nextInt(0xFFFFFF + 1))
    var color3 = String.format("#%06X", Random.nextInt(0xFFFFFF + 1))
    var color4 = String.format("#%06X", Random.nextInt(0xFFFFFF + 1))
    var color5 = String.format("#%06X", Random.nextInt(0xFFFFFF + 1))
    return PaletteModel(id, color0, color1, color2, color3, color4, color5, name)
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