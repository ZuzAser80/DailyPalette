package com.zuzaser.dailypalette.viewmodel

import android.view.View
import android.widget.TextView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.ViewModel

class PaletteViewModel : ViewModel() {
    @Composable
    fun GetView(string: String) {
        val r = remember { mutableStateOf(string) }
        //println("PIDER: $string : $r")
        AndroidView(factory = { context ->
            TextView(context).apply {
                setTextColor(Color(0xFF333399).toArgb())
            }
        },
            update = {
                it.text = "${r.value}"
                it.setTextColor(Color(0xFF000000).toArgb())
            },
            modifier = Modifier
                .padding(12.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(android.graphics.Color.parseColor(r.value)))
                .padding(16.dp)
        )

    }
}
