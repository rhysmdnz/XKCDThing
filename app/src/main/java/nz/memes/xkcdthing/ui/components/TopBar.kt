package nz.memes.xkcdthing.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Feedback
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import nz.memes.xkcdthing.ui.theme.XKCDThingTheme

@Composable
fun OverflowMenu(content: @Composable () -> Unit) {
    var showMenu by remember { mutableStateOf(false) }
    IconButton(onClick = {
        showMenu = !showMenu
    }) {
        Icon(
            imageVector = Icons.Default.MoreVert,
            contentDescription = "More Options"
        )
    }
    DropdownMenu(
        expanded = showMenu,
        onDismissRequest = { showMenu = false }
    ) {
        content()
    }
}

@Composable
fun XKCDTopBar(comicId: Int) {
    var showMenu by remember { mutableStateOf(false) }
    val context = LocalContext.current

    TopAppBar(
        title = { Text("XKCD Thing") },
        actions = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(Icons.Outlined.Feedback, "Hint")
            }
            OverflowMenu {
                DropdownMenuItem(onClick = { }) {
                    Text("Save Comic Image")
                }
                DropdownMenuItem(onClick = {
                    val share = Intent.createChooser(
                        Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, "https://xkcd.com/$comicId/")
                            type = "text/plain"
                        },
                        null
                    )

                    ContextCompat.startActivity(context, share, null)
                    showMenu = false
                }) {
                    Text("Share Link")
                }
                DropdownMenuItem(onClick = {
                    val browserIntent =
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://xkcd.com/$comicId/")
                        )
                    ContextCompat.startActivity(context, browserIntent, null)
                    showMenu = false
                }) {
                    Text("Open on xkcd.com")
                }
                DropdownMenuItem(onClick = { }) {
                    Text("Settings")
                }
            }
        }
    )
}

@Preview
@Composable
fun PreviewOverflowMenu() {
    XKCDThingTheme {
        XKCDTopBar(0)
    }
}
