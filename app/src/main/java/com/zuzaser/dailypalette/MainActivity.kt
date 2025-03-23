package com.zuzaser.dailypalette

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zuzaser.dailypalette.model.PaletteModel
import com.zuzaser.dailypalette.viewmodel.PaletteViewModel
import kotlin.random.Random
//IMPORTANT
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.zuzaser.dailypalette.repository.PaletteRepository
import com.zuzaser.dailypalette.room.PaletteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import java.util.UUID
import kotlin.random.nextInt

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        //Creating database - related things
        val paletteDb = PaletteDatabase.getInstance(application)
        val paletteDao = paletteDb.loginDao()
        val repository: PaletteRepository = PaletteRepository(paletteDao)
        var paletteList : List<PaletteModel> = emptyList()
        repository.paletteList.observe(this, Observer { palettes ->
            palettes?.let {
                // Update the UI with the list of palettes
                paletteList = palettes;
            }})
        setContent {
            var currentPalette by remember { mutableStateOf(generateRandomPalette()) }
            var isSavedOpened by remember { mutableStateOf(false) }
            if (isSavedOpened) {
                Column (
                    modifier = Modifier
                        .fillMaxSize()
                        //.verticalScroll(rememberScrollState())
                    ,
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    PaletteRow(currentPalette, repository)
                }
                Button(
                    onClick = { isSavedOpened = false },
                    shape = RoundedCornerShape(25.dp),
                ) {
                    Text("Back", fontSize = 15.sp)
                }
            } else {
                Scaffold {
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceEvenly,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        for (i in currentPalette.getColors()) {
                            key(i) {
                                PaletteViewModel().GetView(i)
                            }
                        }
                        Row {
                            //Share button
                            val context = LocalContext.current
                            Button(
                                onClick = {
                                    val type = "text/plain"
                                    val subject = "Check out this palette!"
                                    val extraText =
                                        currentPalette.getColors().joinToString(separator = ", ")
                                    val shareWith = "ShareWith"
                                    val intent = Intent(Intent.ACTION_SEND)
                                    intent.type = type
                                    intent.putExtra(Intent.EXTRA_SUBJECT, subject)
                                    intent.putExtra(Intent.EXTRA_TEXT, extraText)
                                    ContextCompat.startActivity(
                                        context,
                                        Intent.createChooser(intent, shareWith),
                                        null
                                    )
                                },
                                shape = RoundedCornerShape(25.dp),

                                ) {
                                Text("Share", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                            //Generate new Palette Button
                            Button(
                                onClick = { currentPalette = generateRandomPalette() },
                                shape = RoundedCornerShape(50.dp)
                            ) {
                                Text("Generate Palette", fontSize = 8.sp)
                            }
                            //Save button
                            Button(
                                onClick = { repository.addPalette(currentPalette) },
                                shape = RoundedCornerShape(25.dp),
                            ) {
                                Text("Save", fontSize = 12.sp)
                            }
                            //Open saved palettes

                            Button(
                                onClick = { println("NIGGA: " + paletteList.count()); isSavedOpened = true; },
                                shape = RoundedCornerShape(25.dp),
                            ) {
                                Text("See saved", fontSize = 8.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PaletteRow(paletteModel: PaletteModel, repository: PaletteRepository) {

    Row{
        for (color in paletteModel.getColors()) {
            Box(modifier = Modifier.background(Color(android.graphics.Color.parseColor(color)),
                shape = RoundedCornerShape(20.dp)).height(50.dp)
                .weight(1f))
        }
        Button(onClick = {}, shape = RoundedCornerShape(25.dp)) {
            Text("Open", fontSize = 15.sp)
        }
        Button(onClick = { repository.removePaletteById(paletteModel) }, shape = RoundedCornerShape(25.dp)) {
            Text("X", fontSize = 15.sp)
        }
    }
}

fun generateRandomPalette() : PaletteModel {
    return PaletteModel(
        Random(Random.nextInt(225)).nextInt(255),
        String.format("#%06X", Random.nextInt(0xFFFFFF + 1)),
        String.format("#%06X", Random.nextInt(0xFFFFFF + 1)),
        String.format("#%06X", Random.nextInt(0xFFFFFF + 1)),
        String.format("#%06X", Random.nextInt(0xFFFFFF + 1)),
        String.format("#%06X", Random.nextInt(0xFFFFFF + 1)),
        String.format("#%06X", Random.nextInt(0xFFFFFF + 1)),
        String.format("%06X", Random.nextInt(0xFFFFFF + 1))
    )
}