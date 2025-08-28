package com.faridev.gameradar.presentation.activity.main

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.faridev.gameradar.R
import com.faridev.gameradar.core.util.showShortToast
import com.faridev.gameradar.presentation.common.theme.GameRadarTheme
import com.faridev.gameradar.presentation.feature.NavRoutes
import com.faridev.gameradar.presentation.feature.detail.GameDetailScreen
import com.faridev.gameradar.presentation.feature.home.HomeScreen
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.setValue

class MainActivity : ComponentActivity() {

    private val navDrawerItems = listOf(
        "Platforms",
        "Publishers",
        "Developers",
        "Genres",
        "Stores"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GameRadarTheme {
                var showExitToast by remember { mutableStateOf(false) }

                DoubleBackPressToExit(
                    onFirstBackPress = {
                        showExitToast = true
                    },
                    onExit = {
                        finish()
                    }
                )

                if (showExitToast) {
                    showShortToast(R.string.double_tap_to_exit)
                    showExitToast = false
                }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainNavigationDrawer(
                        modifier = Modifier.padding(innerPadding),
                        drawerItemsList = navDrawerItems
                    )
                }
            }
        }
    }
}

@Composable
private fun MainNavigationDrawer(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
    drawerItemsList: List<String>
) {
    ModalNavigationDrawer(
        modifier = modifier,
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.width(280.dp),
                drawerContainerColor = MaterialTheme.colorScheme.secondaryContainer
            ) {
                MainNavigationDrawerContent(drawerItemsList)
            }
        }
    ) {
        MainScreenContent(navController = navController)
    }
}

@Composable
private fun MainNavigationDrawerContent(
    drawerItemsList: List<String>
) {
    val context = LocalContext.current
    Column {
        Card(
            modifier = Modifier.padding(10.dp),
            shape = RoundedCornerShape(15.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
        ) {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(2.2f),
                painter = painterResource(R.drawable.banner),
                contentScale = ContentScale.Crop,
                contentDescription = "Banner"
            )
        }

        LazyColumn(modifier = Modifier.padding(top = 10.dp)) {
            items(drawerItemsList) { itemTitle ->
                DrawerItem(itemTitle) {
                    context.showShortToast(itemTitle)
                }
            }
        }
    }
}

@Composable
private fun DrawerItem(itemTitle: String, onClick: () -> Unit = {}) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(8.dp),
        text = itemTitle,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.titleLarge
    )
}

@Composable
private fun MainScreenContent(navController: NavHostController) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        NavHost(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            navController = navController,
            startDestination = NavRoutes.Home
        ) {
            composable<NavRoutes.Home> {
                HomeScreen { gameId ->
                    navController.navigate(NavRoutes.Detail(gameId))
                }
            }
            composable<NavRoutes.Detail> { backStackEntry ->
                val detail = backStackEntry.toRoute<NavRoutes.Detail>()
                GameDetailScreen(gameId = detail.gameId)
            }
        }
    }
}

@Composable
fun DoubleBackPressToExit(
    exitTimeout: Long = 2000L,
    onExit: () -> Unit,
    onFirstBackPress: () -> Unit
) {
    var lastBackPressTime by remember { mutableLongStateOf(0L) }

    BackHandler {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastBackPressTime < exitTimeout) {
            onExit()
        } else {
            onFirstBackPress()
            lastBackPressTime = currentTime
        }
    }
}