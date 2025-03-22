package com.zuzaser.dailypalette.viewmodel

import android.os.Build
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.ViewModel

class PaletteViewModel : ViewModel() {
    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun GetView(string: String) {
        val r = rememberSaveable  { mutableStateOf(string) }
        val c = LocalConfiguration.current
        AndroidView(factory = { context ->
            TextView(context).apply {
                // 15 seems to be the best magic number here, works (should) on most devices
                textSize = c.screenHeightDp.dp.value / 15f
            }
        },
            update = {
                it.text = r.value
                var clr = Color(android.graphics.Color.parseColor(r.value))
                it.setTextColor(android.graphics.Color.valueOf((255 - (clr.red * 255).toFloat()).coerceIn(0f, 255f), (255 - (clr.green * 255).toFloat()).coerceIn(0f, 255f), (255 - (clr.blue * 255).toFloat()).coerceIn(0f, 255f), clr.alpha).toArgb())
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(android.graphics.Color.parseColor(r.value)))
                .padding(12.dp)
        )

    }
}
