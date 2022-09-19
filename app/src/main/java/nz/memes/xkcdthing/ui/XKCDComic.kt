package nz.memes.xkcdthing.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import coil.compose.rememberAsyncImagePainter
import kotlin.math.max

@Composable
fun MainImage(url: String) {
    var scale by remember { mutableStateOf(1f) }
    var size by remember { mutableStateOf(Size(0f, 0f)) }
    var width by remember {
        mutableStateOf(0.dp)
    }
    var height by remember {
        mutableStateOf(0.dp)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .onGloballyPositioned { coordinates ->
                size = coordinates.size.toSize()
            }
    ) {
        with(LocalDensity.current) {
            width = (size.width * scale).toDp()
            height = (size.height * scale).toDp()
        }

        Image(
            painter = rememberAsyncImagePainter(url),
            contentDescription = "The XKCD Image",
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .horizontalScroll(rememberScrollState())
                .size(width = width, height = height)
                .align(Alignment.Center)
                // .border(BorderStroke(2.dp, Color.Red))
                .pointerInput(Unit) {
                    forEachGesture {
                        awaitPointerEventScope {
                            awaitFirstDown()
                            do {
                                val event = awaitPointerEvent()
                                scale = max(scale * event.calculateZoom(), 1f)
                            } while (event.changes.any { it.pressed })
                        }
                    }
                }
        )
    }
}
