package nz.memes.xkcdthing

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Feedback
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.coil.rememberCoilPainter
import dagger.hilt.android.AndroidEntryPoint
import nz.memes.xkcdthing.ui.theme.XKCDThingTheme
import kotlin.math.max
import kotlin.math.roundToInt

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val xkcdViewModel: XKCDViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            navController.addOnDestinationChangedListener { _, destination, arguments ->
                val comicId = arguments?.getInt("comicId")
                xkcdViewModel.changeComic(comicId!!)
            }

            XKCDApp(xkcdViewModel, navController = navController)
        }
    }
}

@Preview
@Composable
fun PreviewOverflowMenu() {
    XKCDThingTheme() {
        XKCDTopBar(0)
    }
}

@Composable
fun XKCDApp(xkcdViewModel: XKCDViewModel, navController: NavHostController) {
    XKCDThingTheme {
        // A surface container using the 'background' color from the theme
        Scaffold(topBar = { XKCDTopBar(xkcdViewModel.comicId) }, bottomBar = { XKCDBottomBar(navController, xkcdViewModel.nextId, xkcdViewModel.prevId) }) {
            NavHost(navController = navController, startDestination = "comic/{comicId}") {
                composable(
                    "comic/{comicId}",
                    arguments = listOf(navArgument("comicId") { type = NavType.IntType })
                ) {
                    Surface(color = MaterialTheme.colors.background) {
                        MainImage(xkcdViewModel.currentImageUrl)
                    }
                }
            }
        }
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
            IconButton(onClick = { /*TODO*/ }) {
                Icon(Icons.Outlined.Search, "Search")
            }
            Box {
                IconButton(onClick = { showMenu = true }) {
                    Icon(Icons.Default.MoreVert, "More Options")
                }
                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false })
                {
                    DropdownMenuItem(onClick = { }) {
                        Text("List all Comics")
                    }
                    DropdownMenuItem(onClick = { }) {
                        Text("Add to Favourites")
                    }
                    DropdownMenuItem(onClick = { }) {
                        Text("Preferences")
                    }
                    DropdownMenuItem(onClick = { }) {
                        Text("Download All")
                    }
                    DropdownMenuItem(onClick = { }) {
                        Text("Reload Comic")
                    }
                    DropdownMenuItem(onClick = { }) {
                        Text("Save Comic Image")
                    }
                    DropdownMenuItem(onClick = {
                        val share = Intent.createChooser(Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, "https://xkcd.com/${comicId}/")
                            type = "text/plain"
                        }, null)

                        startActivity(context, share, null)
                        showMenu = false
                    }) {
                        Text("Share Link")
                    }
                    DropdownMenuItem(onClick = {
                        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://xkcd.com/${comicId}/"))
                        startActivity(context, browserIntent, null)
                        showMenu = false
                    }) {
                        Text("Open on xkcd.com")
                    }
                }
            }
        }
    )
}

@Composable
fun XKCDBottomBar(navController: NavController, nextId: Int, prevId: Int) {
    BottomAppBar() {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
            TextButton(onClick = { navController.navigate("comic/1") }, enabled = prevId != 0) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.SkipPrevious, "First")
                    Text(text = "First")
                }
            }
            TextButton(onClick = { navController.navigate("comic/${prevId}") }, enabled = prevId != 0) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.NavigateBefore, "Previous")
                    Text(text = "Prev")
                }
            }
            TextButton(onClick = { /*TODO*/ }) {
                Text(text = "Rand")
            }
            TextButton(onClick = { navController.navigate("comic/${nextId}") }) {
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

@Composable
fun MainImage(url: String) {
    var zoom by remember { mutableStateOf(1f) }
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }
    Image(
        painter = rememberCoilPainter(url),
        contentDescription = "The XKCD Image",
        modifier = Modifier
            .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
            .graphicsLayer(
                scaleX = zoom,
                scaleY = zoom,
            )
            .pointerInput(Unit) {
                detectTransformGestures(
                    onGesture = { _, pan, gestureZoom, _ ->
                        zoom = max(zoom * gestureZoom, 1f)
                        offsetX += pan.x * zoom
                        offsetY += pan.y * zoom
                    }
                )
            }
            .fillMaxSize()
    )
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    XKCDThingTheme {
        Greeting("Android")
    }
}