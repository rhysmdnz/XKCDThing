package nz.memes.xkcdthing

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.rememberImagePainter
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import nz.memes.xkcdthing.ui.theme.XKCDThingTheme
import kotlin.math.max
import kotlin.math.roundToInt

@ExperimentalPagerApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var analytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        analytics = Firebase.analytics
        FirebaseMessaging.getInstance().subscribeToTopic("new_xkcd_comic")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel
            val name = getString(R.string.new_comic_channel_name)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val mChannel = NotificationChannel("new_xkcd_comic", name, importance)
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }
        setContent {
            XKCDApp(hiltViewModel())
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

@ExperimentalPagerApi
@Composable
fun XKCDApp(xkcdViewModel: XKCDViewModel) {
    val TAG = "XKCDApp"
    val lazyComics: LazyPagingItems<XKCDResponse> = xkcdViewModel.comics.collectAsLazyPagingItems()
    val pagerState = rememberPagerState()
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()
    navController.addOnDestinationChangedListener { _, _, arguments ->
        val comicId = arguments!!.getInt("comicId", 1)
        scope.launch {
            Log.v(TAG, "are we changing? $comicId")
            pagerState.animateScrollToPage(comicId - 1)
        }
    }
    LaunchedEffect(pagerState) {
        snapshotFlow {
            pagerState.currentPage
        }.collect {
            Log.i("MY_APP", "We moved the page to $it")
            if (it+1 != navController.currentBackStackEntry!!.arguments!!.getInt("comicId")) {
                Log.v(TAG, "The if condition thing hit")
                navController.navigate("comic/${it+1}")
            }
        }
    }
    XKCDThingTheme {
        // A surface container using the 'background' color from the theme
        Scaffold(topBar = { XKCDTopBar(pagerState.currentPage) }, bottomBar = { XKCDBottomBar(navController, pagerState.currentPage+2, pagerState.currentPage) }) {
            NavHost(navController = navController, startDestination = "comic/{comicId}") {
                composable(
                    "comic/{comicId}",
                    arguments = listOf(navArgument("comicId") { type = NavType.IntType })
                ) {
                    Surface(color = MaterialTheme.colors.background) {
                        HorizontalPager(count = 2000, state=pagerState) { page ->
                            if (lazyComics.itemCount > 0) {
                                lazyComics[page]?.let { it1 -> MainImage(it1.img) }
                                    ?: MainImage("https://media.istockphoto.com/vectors/shiny-red-traditional-cricket-ball-vector-id181668903?k=20&m=181668903&s=612x612&w=0&h=gor7cqAp4aOdkqReZMXgynUrnoJ6y4W_GzOblKM1VPI=")
                            }
                        }
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
        painter = rememberImagePainter(url),
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