package nz.memes.xkcdthing.ui

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import nz.memes.xkcdthing.data.XKCDComic
import nz.memes.xkcdthing.ui.components.XKCDBottomBar
import nz.memes.xkcdthing.ui.components.XKCDTopBar
import nz.memes.xkcdthing.ui.theme.XKCDThingTheme

@ExperimentalPagerApi
@Composable
fun XKCDApp(xkcdViewModel: XKCDViewModel) {
    val navController = rememberNavController()
    XKCDThingTheme {
        // A surface container using the 'background' color from the theme {
        NavHost(navController = navController, startDestination = "latestComic") {
            composable("latestComic") {
                val latestComicId by xkcdViewModel.getLatestComicId().observeAsState(0)
                if (latestComicId != 0) {
                    navController.popBackStack()
                    navController.navigate("comic/$latestComicId")
                }
                Surface(color = MaterialTheme.colors.background) {}
            }
            composable(
                "comic/{comicId}",
                arguments = listOf(navArgument("comicId") { type = NavType.IntType })
            ) { backStackEntry ->
                PagingComicView(
                    xkcdViewModel = xkcdViewModel, navController = navController,
                    // backStackEntry.arguments?.getInt("comicId") ?: 0
                )
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun PagingComicView(xkcdViewModel: XKCDViewModel, navController: NavHostController) {
    val scope = rememberCoroutineScope()
    val comicId = navController.currentBackStackEntry!!.arguments!!.getInt("comicId")
    val lazyComics: LazyPagingItems<XKCDComic> = xkcdViewModel.comics.collectAsLazyPagingItems()
    val pagerState = rememberPagerState()
    // val scope = rememberCoroutineScope()
    DisposableEffect(navController) {
        val callback = NavController.OnDestinationChangedListener { _, _, arguments ->
            val newComicId = arguments!!.getInt("comicId", 1)
            // scope.launch {
            //     Timber.v("are we changing? $newComicId")
            //     pagerState.animateScrollToPage(newComicId - 1)
            // }
        }
        navController.addOnDestinationChangedListener(callback)
        onDispose {
            navController.removeOnDestinationChangedListener(callback)
        }
    }
    // LaunchedEffect(pagerState) {
    //     snapshotFlow {
    //         pagerState.currentPage
    //     }.collect {
    //         Timber.i("We moved the page to $it")
    //         if (it + 1 != navController.currentBackStackEntry!!.arguments!!.getInt("comicId")) {
    //             Timber.v("The if condition thing hit")
    //             navController.navigate("comic/${it + 1}")
    //         }
    //     }
    // }
    Scaffold(
        topBar = {
            XKCDTopBar(
                if (pagerState.currentPage < lazyComics.itemCount) lazyComics[pagerState.currentPage]?.num // ktlint-disable max-line-length
                    ?: 0 else 0
            )
        },
        bottomBar = {
            XKCDBottomBar(
                navController,
                pagerState.currentPage + 2,
                pagerState.currentPage
            )
        }
    ) {
        Surface(color = MaterialTheme.colors.background) {
            HorizontalPager(count = 3000, state = pagerState) { page ->
                // if (lazyComics.itemCount > 0) {
                //     lazyComics[page]?.let { it1 -> MainImage(it1.img) }
                //         ?: MainImage(stringResource(R.string.mainTmpImg))
                // }
            }
        }
    }
}
