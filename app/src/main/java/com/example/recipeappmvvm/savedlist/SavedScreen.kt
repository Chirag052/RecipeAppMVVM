package com.example.recipeappmvvm.savedlist

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltNavGraphViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import coil.request.ImageRequest
import com.example.recipeappmvvm.R
import com.example.recipeappmvvm.data.remote.responses.SavedDB
import com.example.recipeappmvvm.ui.theme.QuickSand
import com.google.accompanist.coil.CoilImage


@ExperimentalAnimationApi
@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
fun SavedScreen(
    navController: NavController,
    viewModel: SavedScreenViewModel = hiltNavGraphViewModel()
) {
    Box(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize()
    ) {
        Column(modifier = Modifier.padding(bottom = 56.dp)) {

            RecipeList(viewModel = viewModel,navController = navController)
        }

    }
}
@ExperimentalAnimationApi
@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
fun RecipeCard(
    rowIndex: Int,
    entry: SavedDB? = null,
    navController: NavController,
    viewModel: SavedScreenViewModel
) {
    var longClicked by remember {
        mutableStateOf(true)
    }

    val context = LocalContext.current
    AnimatedVisibility(visible = longClicked) {


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = {
                    navController.navigate(
                        "recipe_detail_screen/${entry!!.id}"
                    )
                },
                onLongClick = {

                    Log.i("deleted", "deled")
                    viewModel.deleteRecipe(entry!!.id, entry)
                    showMessage(context, message = "Sucessfully Deleted!")
                    longClicked=false
                }),
        elevation = 5.dp,
    ) {


        Column {
            CoilImage(
                request = ImageRequest.Builder(LocalContext.current)
                    .data(entry!!.featured_image)
                    .placeholder(R.drawable.empty_plate)
                    .build(),
                contentDescription = entry.title,
                modifier = Modifier
                    .height(225.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop,

                ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier
                        .scale(0.1f)
                )
            }

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 5.dp)
                    .padding(top = 10.dp, bottom = 10.dp)

            ) {
                Text(
                    text = entry!!.title ?: "",
                    fontFamily = QuickSand,
                    style = MaterialTheme.typography.h3,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth(0.8f)
                )
                Text(
                    text = entry.rating.toString(),
                    fontFamily = QuickSand,
                    style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

}

fun showMessage(context: Context, message:String){
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}



@ExperimentalAnimationApi
@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
fun RecipeRow(
    rowIndex: Int,
    entries: MutableList<SavedDB>?,
    navController: NavController,
    viewModel: SavedScreenViewModel
) {
    Column {
        Row{
            RecipeCard(rowIndex = rowIndex,viewModel = viewModel,entry = entries?.get(rowIndex), navController = navController)

        }
        Spacer(modifier = Modifier.height(20.dp))
    }

}
@ExperimentalAnimationApi
@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
fun RecipeList(
    navController: NavController,
    viewModel: SavedScreenViewModel
){
    LazyColumn {
        val itemCount = viewModel.getAllSavedRecipes()!!.size
        items(itemCount){
           val recipeList = viewModel.getAllSavedRecipes()
           RecipeRow(viewModel =viewModel,rowIndex = it, entries = recipeList, navController = navController)
        }


    }
}
