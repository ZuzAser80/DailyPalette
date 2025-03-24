package com.zuzaser.dailypalette

//IMPORTANT
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.zuzaser.dailypalette.model.PaletteModel
import com.zuzaser.dailypalette.repository.PaletteRepository
import com.zuzaser.dailypalette.room.PaletteDatabase
import com.zuzaser.dailypalette.viewmodel.PaletteViewModel
import kotlin.random.Random

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
        var paletteList : LiveData<List<PaletteModel>> = repository.paletteList
        repository.paletteList.observe(this, Observer { palettes ->
            palettes?.let {}})
        setContent {
            var currentPalette by remember { mutableStateOf(generateRandomPalette()) }
            var isSavedOpened by remember { mutableStateOf(false) }
            if (isSavedOpened) {
                //TODO: later figure out how to update ts dynamically
                Column (
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                    ,
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (paletteList.value!!.isNotEmpty()) {
                        for (pal in paletteList.value!!) {
                            PaletteRow(pal, repository)
                        }
                    }
                }
                Row(verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Button(
                        onClick = { isSavedOpened = false },
                        shape = RoundedCornerShape(25.dp),

                        ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                    Button(
                        onClick = { repository.clearTable(); isSavedOpened = false },
                        shape = RoundedCornerShape(25.dp),

                        ) {
                        Icon(Icons.Filled.Delete, contentDescription = "Удалить все")
                    }
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
                                    sharePaletteIntent(currentPalette, context)
                                },
                                shape = RoundedCornerShape(25.dp),

                                ) {
                                Icon(Icons.Filled.Share, contentDescription = "Поделиться")
                            }
                            //Generate new Palette Button
                            Button(
                                onClick = { currentPalette = generateRandomPalette() },
                                shape = RoundedCornerShape(50.dp)
                            ) {
                                Icon(Icons.Filled.Refresh, contentDescription = "")
                            }
                            //Save button
                            Button(
                                onClick = { repository.addPalette(currentPalette) },
                                shape = RoundedCornerShape(25.dp),
                            ) {
                                Icon(Icons.Filled.Add, contentDescription = "")
                            }
                            //Open saved palettes

                            Button(
                                onClick = { isSavedOpened = true; },
                                shape = RoundedCornerShape(25.dp),
                            ) {
                                Icon(Icons.Filled.Email, contentDescription = "")
                            }
                        }
                    }
                }
            }
        }
    }
}

fun sharePaletteIntent(paletteModel: PaletteModel, context : Context)
{
    val type = "text/plain"
    val subject = "Check out this palette!"
    val extraText =
        paletteModel.getColors().joinToString(separator = ", ")
    val shareWith = "ShareWith"
    val intent = Intent(Intent.ACTION_SEND)
    intent.type = type
    intent.putExtra(Intent.EXTRA_SUBJECT, subject)
    intent.putExtra(Intent.EXTRA_TEXT, extraText)
    context.startActivity(
        Intent.createChooser(intent, shareWith),
        null
    )
}

@Composable
fun PaletteRow(paletteModel: PaletteModel, repository: PaletteRepository) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        val context = LocalContext.current
        for (color in paletteModel.getColors()) {
            Box(modifier = Modifier
                .background(
                    Color(android.graphics.Color.parseColor(color)),
                    shape = RoundedCornerShape(20.dp)
                )
                .height(35.dp)
                .weight(1f)

            )

        }
        Button(onClick = { sharePaletteIntent(paletteModel, context) }, shape = RoundedCornerShape(25.dp)) {
            Icon(Icons.Filled.Share, contentDescription = "Поделиться")
        }
        Button(onClick = { repository.removePaletteById(paletteModel) }, shape = RoundedCornerShape(25.dp)) {
            Icon(Icons.Filled.Delete, contentDescription = "Удалить")
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