package com.example.recipeappmvvm

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import com.example.recipeappmvvm.data.models.BottomMenuContent
import com.example.recipeappmvvm.data.remote.Screen
import com.example.recipeappmvvm.recipedetailscreen.RecipeDetailScreen
import com.example.recipeappmvvm.recipelist.RecipeListScreen
import com.example.recipeappmvvm.savedlist.SavedScreen
import com.example.recipeappmvvm.ui.theme.RecipeAppMVVMTheme
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @ExperimentalMaterialApi
    @ExperimentalFoundationApi
    @ExperimentalAnimationApi
    @ExperimentalComposeUiApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RecipeAppMVVMTheme {
                val navController = rememberNavController()
                Scaffold(
                    bottomBar = {
//                        BottomMenu(
//                            navController = navController,
//                            items = listOf(
//                                BottomMenuContent("recipe_list_screen","Home", R.drawable.home),
//                                BottomMenuContent("all_saved_recipies","Saved",R.drawable.heart_red)
//                            )
//                        )
                        BottomNavigationBar(navController = navController)
                    }
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = "recipe_list_screen"
                    ){
                        composable("recipe_list_screen"){
                            RecipeListScreen(navController = navController)
                        }
                        composable(
                            "recipe_detail_screen/{id}",
                            arguments = listOf(
                                navArgument("id"){
                                    type = NavType.IntType
                                }
                            )
                        ){

                            val id = remember{
                                it.arguments?.getInt("id")
                            }

                            RecipeDetailScreen(
                                id = id,
                                navController = navController
                            )
                        }
                        composable("all_saved_recipies"){
                            SavedScreen(navController = navController)

                        }

                    }
                }


            }
        }
    }
}
//@ExperimentalComposeUiApi
//@Composable
//fun Navigate(navController: NavController)
//{
//   // navController = rememberNavController()
//    NavHost(
//        navController = navController,
//        startDestination = "recipe_list_screen"
//    ){
//        composable("recipe_list_screen"){
//            RecipeListScreen(navController = navController)
//        }
//        composable(
//            "recipe_detail_screen/{id}",
//            arguments = listOf(
//                navArgument("id"){
//                    type = NavType.IntType
//                }
//            )
//        ){
//
//            val id = remember{
//                it.arguments?.getInt("id")
//            }
//
//            RecipeDetailScreen(
//                id = id,
//                navController = navController
//            )
//        }
//        composable("all_saved_recipies"){
//            SavedScreen(navController = navController)
//        }
//    }
//}


@Composable
fun BottomMenu(
    navController: NavController,
    items: List<BottomMenuContent>,
    modifier: Modifier = Modifier,
    activeHighlightColor: Color = colorResource(id = R.color.lightBlue),
    activeTextColor: Color = colorResource(id = R.color.lightBlue),
    inactiveTextColor: Color = colorResource(id = R.color.grey),
    initialSelectedItemIndex:Int = 0
){
    var selectedItemIndex by remember {
        mutableStateOf(initialSelectedItemIndex)
    }
    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(colorResource(id = R.color.offwhite))
            .padding(5.dp)
    ) {
        items.forEachIndexed { index, item ->
            BottomMenuItem(
                item = item,
                isSelected = index == selectedItemIndex,
                activeHighlightColor = activeHighlightColor,
                activeTextColor = activeTextColor,
                inactiveTextColor = inactiveTextColor,
                navController = navController
            ) {
                selectedItemIndex = index
               navController.navigate(item.route)
                
            }
        }
    }
}

@Composable
fun BottomMenuItem(
    navController: NavController,
    item: BottomMenuContent,
    isSelected: Boolean =false,
    activeHighlightColor: Color = colorResource(id = R.color.lightBlue),
    activeTextColor: Color = colorResource(id = R.color.white),
    inactiveTextColor: Color = colorResource(id = R.color.black),
    onItemClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.clickable {
            onItemClick()

        }
    ) {

        Icon(
            painter = painterResource(id = item.iconId),
            contentDescription = item.title,
            tint = if (isSelected) activeHighlightColor else inactiveTextColor,
            modifier = Modifier.size(20.dp)
        )
        Text(
            text = item.title,
            color = if(isSelected) activeTextColor else inactiveTextColor,
            fontSize = 15.sp
        )
    }


}


@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(Screen.RecipeList, Screen.RecipieInfo)
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.arguments?.getString(KEY_ROUTE)
    if(currentRoute !="recipe_detail_screen/{id}"){

    BottomNavigation {
        items.forEach {
            BottomNavigationItem(
                modifier = Modifier
                    .background(colorResource(id = R.color.offwhite))
                ,
                unselectedContentColor = colorResource(id = R.color.grey),
                selectedContentColor = colorResource(id = R.color.lightBlue),
                icon = { Icon(it.icon,null) },
                selected = currentRoute == it.route,
                label = { Text(text = it.label) },
                onClick = {
                    navController.popBackStack(
                        navController.graph.startDestination, false)
                    Log.i("cuurent route",currentRoute.toString())
                    if (currentRoute != it.route && currentRoute!="recipe_detail_screen/{id}") {
                        navController.navigate(it.route)
                    }
                })
        }
            }
    }
}

