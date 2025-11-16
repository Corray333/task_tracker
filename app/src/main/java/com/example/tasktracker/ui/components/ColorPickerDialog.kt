package com.example.tasktracker.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.tasktracker.R
import kotlin.math.roundToInt

@Composable
fun ColorPickerDialog(
    initialColor: String,
    onColorSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val initialColorObj = parseColorString(initialColor)
    var hue by remember { mutableFloatStateOf(initialColorObj.hue) }
    var saturation by remember { mutableFloatStateOf(initialColorObj.saturation) }
    var value by remember { mutableFloatStateOf(initialColorObj.value) }

    val selectedColor = hsvToColor(hue, saturation, value)

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.select_color),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                // Color preview
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .background(selectedColor, CircleShape)
                            .border(2.dp, MaterialTheme.colorScheme.outline, CircleShape)
                    )

                    Text(
                        text = colorToHex(selectedColor),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium
                    )
                }

                // Saturation-Value picker
                SaturationValuePicker(
                    hue = hue,
                    saturation = saturation,
                    value = value,
                    onSaturationValueChange = { newSat, newVal ->
                        saturation = newSat
                        value = newVal
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )

                // Hue slider
                Text(
                    text = stringResource(R.string.hue),
                    style = MaterialTheme.typography.labelMedium
                )
                HueSlider(
                    hue = hue,
                    onHueChange = { hue = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                )

                // Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onDismiss) {
                        Text(stringResource(R.string.cancel))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = {
                        onColorSelected(colorToHex(selectedColor))
                        onDismiss()
                    }) {
                        Text(stringResource(R.string.select))
                    }
                }
            }
        }
    }
}

@Composable
fun SaturationValuePicker(
    hue: Float,
    saturation: Float,
    value: Float,
    onSaturationValueChange: (Float, Float) -> Unit,
    modifier: Modifier = Modifier
) {
    var position by remember { mutableStateOf(Offset(saturation, 1f - value)) }

    Box(
        modifier = modifier
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(8.dp))
    ) {
        Canvas(
            modifier = Modifier
                .matchParentSize()
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        val newSat = (offset.x / size.width).coerceIn(0f, 1f)
                        val newVal = 1f - (offset.y / size.height).coerceIn(0f, 1f)
                        onSaturationValueChange(newSat, newVal)
                        position = Offset(newSat, 1f - newVal)
                    }
                }
                .pointerInput(Unit) {
                    detectDragGestures { change, _ ->
                        change.consume()
                        val newSat = (change.position.x / size.width).coerceIn(0f, 1f)
                        val newVal = 1f - (change.position.y / size.height).coerceIn(0f, 1f)
                        onSaturationValueChange(newSat, newVal)
                        position = Offset(newSat, 1f - newVal)
                    }
                }
        ) {
            // Draw saturation-value gradient
            val hueColor = hsvToColor(hue, 1f, 1f)

            // Horizontal gradient (saturation)
            drawRect(
                brush = Brush.horizontalGradient(
                    colors = listOf(Color.White, hueColor)
                )
            )

            // Vertical gradient (value/brightness)
            drawRect(
                brush = Brush.verticalGradient(
                    colors = listOf(Color.Transparent, Color.Black)
                )
            )

            // Draw selector circle
            val x = position.x * size.width
            val y = position.y * size.height
            drawCircle(
                color = Color.White,
                radius = 12f,
                center = Offset(x, y),
                style = Stroke(width = 3f)
            )
            drawCircle(
                color = Color.Black,
                radius = 12f,
                center = Offset(x, y),
                style = Stroke(width = 1f)
            )
        }
    }
}

@Composable
fun HueSlider(
    hue: Float,
    onHueChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        Canvas(
            modifier = Modifier
                .matchParentSize()
                .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(8.dp))
        ) {
            val colors = listOf(
                Color.Red,
                Color.Magenta,
                Color.Blue,
                Color.Cyan,
                Color.Green,
                Color.Yellow,
                Color.Red
            )

            drawRect(
                brush = Brush.horizontalGradient(colors = colors)
            )
        }

        Slider(
            value = hue,
            onValueChange = onHueChange,
            valueRange = 0f..360f,
            modifier = Modifier.matchParentSize()
        )
    }
}

data class HSVColor(val hue: Float, val saturation: Float, val value: Float)

fun parseColorString(colorHex: String): HSVColor {
    val color = try {
        Color(android.graphics.Color.parseColor(colorHex))
    } catch (e: Exception) {
        Color.Magenta
    }

    val hsv = FloatArray(3)
    android.graphics.Color.colorToHSV(
        android.graphics.Color.rgb(
            (color.red * 255).toInt(),
            (color.green * 255).toInt(),
            (color.blue * 255).toInt()
        ),
        hsv
    )

    return HSVColor(hsv[0], hsv[1], hsv[2])
}

fun hsvToColor(hue: Float, saturation: Float, value: Float): Color {
    val rgb = android.graphics.Color.HSVToColor(floatArrayOf(hue, saturation, value))
    return Color(rgb)
}

fun colorToHex(color: Color): String {
    val red = (color.red * 255).roundToInt()
    val green = (color.green * 255).roundToInt()
    val blue = (color.blue * 255).roundToInt()
    return "#%02X%02X%02X".format(red, green, blue)
}
