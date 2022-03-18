package nz.memes.xkcdthing.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.BottomAppBar
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NavigateBefore
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import nz.memes.xkcdthing.ui.theme.XKCDThingTheme

@Composable
fun XKCDBottomBar(navController: NavController, nextId: Int, prevId: Int) {
    BottomAppBar {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            TextButton(onClick = { navController.navigate("comic/1") }, enabled = prevId != 0) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.SkipPrevious, "First")
                    Text(text = "First")
                }
            }
            TextButton(
                onClick = { navController.navigate("comic/$prevId") },
                enabled = prevId != 0
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.NavigateBefore, "Previous")
                    Text(text = "Prev")
                }
            }
            TextButton(onClick = { /*TODO*/ }) {
                Text(text = "Rand")
            }
            TextButton(onClick = { navController.navigate("comic/$nextId") }) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Next")
                    Icon(Icons.Default.NavigateNext, "Next")
                }
            }
            TextButton(onClick = { navController.navigate("comic/0") }) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Last")
                    Icon(Icons.Default.SkipNext, "Last")
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewBottomBar() {
    val navController = rememberNavController()
    XKCDThingTheme {
        XKCDBottomBar(navController, 1, 1)
    }
}
