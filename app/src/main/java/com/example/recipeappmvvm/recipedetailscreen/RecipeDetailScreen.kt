package com.example.recipeappmvvm.recipedetailscreen

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltNavGraphViewModel
import androidx.navigation.NavController
import coil.request.ImageRequest
import com.example.recipeappmvvm.R
import com.example.recipeappmvvm.data.remote.responses.Result
import com.example.recipeappmvvm.data.remote.responses.SavedDB
import com.example.recipeappmvvm.recipelist.RecipeListViewModel
import com.example.recipeappmvvm.ui.theme.QuickSand
import com.example.recipeappmvvm.ui.theme.RobotoCondensed
import com.example.recipeappmvvm.utils.Resource
import com.google.accompanist.coil.CoilImage
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun RecipeDetailScreen(
    id: Int?,
    navController: NavController,
    viewModel: RecipeViewModel = hiltNavGraphViewModel(),

) {
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    Scaffold(modifier = Modifier.fillMaxSize(),scaffoldState = scaffoldState) {


        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 10.dp)
        ) {
            //TopSection(id)
            Log.i("TAGG", id.toString())
            val recipeInfo = produceState<Resource<Result>>(initialValue = Resource.Loading()) {
                value = viewModel.getRecipeInfo(id!!)
            }.value
            RecipeDetailStateWrapper(
                recipeInfo = recipeInfo,
                loadingModifier = Modifier.align(Alignment.Center)
            )

            when(recipeInfo){
                is Resource.Success -> {
                    FloatingActionButton(
                        onClick = {
                            viewModel.addToDB(recipeInfo)
                            scope.launch {
                                scaffoldState.snackbarHostState.showSnackbar("Successfully Saved")
                            }
                            // Toast.makeText(LocalContext.current, "Success", Toast.LENGTH_SHORT).show()
                        }, modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(20.dp)
                    )
                    {
                        Icon(Icons.Filled.Save, contentDescription = null)

                    }
                }
            }

        }
    }
}

@Composable
fun RecipeDetailStateWrapper(
    recipeInfo:Resource<Result>,
    modifier: Modifier = Modifier,
    loadingModifier: Modifier = Modifier
) {

    when(recipeInfo){
        is Resource.Success -> {
            RecipeDetailScreen(recipeInfo = recipeInfo.data!!)
        }
        is Resource.Error ->{
            Text(
                text = recipeInfo.message!!,
                color = Color.Red,
                modifier = modifier
            )
        }
        is Resource.Loading -> {
            CircularProgressIndicator(
                color = MaterialTheme.colors.primary,
                modifier = loadingModifier
            )

        }
    }
}

@Composable
fun RecipeDetailScreen(
    recipeInfo:Result,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)) {
        CoilImage(request = ImageRequest.Builder(LocalContext.current)
            .data(recipeInfo.featured_image)
            .placeholder(R.drawable.empty_plate)
            .build(),
            contentDescription = recipeInfo.title,
            modifier = modifier
                .height(250.dp)
                .fillMaxWidth(),
            contentScale = ContentScale.Crop,
        ){
            CircularProgressIndicator(
                color = MaterialTheme.colors.primary,
                modifier = Modifier
                    .scale(0.1f)
            )
        }

        Row(horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
                .padding(top = 10.dp, bottom = 3.dp)
        ) {
            Text(text = recipeInfo.title ?: "",
                fontFamily = QuickSand,
                style = MaterialTheme.typography.h3,
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth(0.9f)
            )
            Text(text = recipeInfo.rating.toString(),
                fontFamily = QuickSand,
                style = MaterialTheme.typography.body1,
                color = colorResource(id = R.color.lightblack),
                textAlign = TextAlign.Center
            )

        }
        Text(text = "Updated "+recipeInfo.date_updated+" by "+recipeInfo.publisher,
            fontFamily = QuickSand,
            style = MaterialTheme.typography.body2,
            fontSize = 13.sp,
            color = colorResource(id = R.color.lightblack),
            textAlign = TextAlign.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
                .padding(bottom = 10.dp)
        )
        recipeInfo.ingredients?.forEachIndexed{index,item ->
            Text(text = item,
                fontFamily = QuickSand,
                style = MaterialTheme.typography.body1,
                color = colorResource(id = R.color.lightblack),
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
                    .padding(3.dp)
            )
        }

    }
}