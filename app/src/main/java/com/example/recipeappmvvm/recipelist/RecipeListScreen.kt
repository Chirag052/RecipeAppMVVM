package com.example.recipeappmvvm.recipelist

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltNavGraphViewModel
import androidx.navigation.NavController
import com.example.recipeappmvvm.R
import coil.request.ImageRequest
import com.example.recipeappmvvm.data.models.RecipeListEntry
import com.google.accompanist.coil.CoilImage
import androidx.compose.material.Icon
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.navigation.compose.navigate
import com.example.recipeappmvvm.data.models.BottomMenuContent
import com.example.recipeappmvvm.ui.theme.QuickSand
import com.example.recipeappmvvm.ui.theme.TextWhite


@ExperimentalComposeUiApi
@Composable
fun RecipeListScreen(
    navController: NavController,
    viewModel: RecipeListViewModel = hiltNavGraphViewModel()
) {
            Box(
                modifier = Modifier
                    .background(Color.White)
                    .fillMaxSize()
            ){
                Column {
                    TopBar(
                        chips = listOf("Chicken", "Beef", "Soup","Dessert","Vegetarian","Milk","Vegan","Pizza","Donut"),
                        navController = navController,
                        viewModel = viewModel
                    )

                }

            }
        }



@ExperimentalComposeUiApi
@Composable
fun TopBar(
    chips: List<String>,
    navController: NavController,
    viewModel: RecipeListViewModel
){
    var textFieldState by remember {
        mutableStateOf("")
    }
    //Log.i("stringggg", text)
    val keyboardController = LocalSoftwareKeyboardController.current
    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        color = colorResource(id = R.color.offwhite),
        elevation = 8.dp,
    ){
        Column{
    Row(modifier = Modifier.fillMaxWidth()) {
        TextField(
            value = textFieldState,
            modifier = Modifier
                .fillMaxWidth(.9f)
                .padding(8.dp)
                .background(Color.Transparent),
            label ={
                Text(text = "Search")
            },
            onValueChange = {
                textFieldState =  it
            },
            leadingIcon = {
                Icon(Icons.Filled.Search, contentDescription =null)
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done,
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                    // do something here

                    viewModel.searchPokemonList(textFieldState)

                }
            ),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = colorResource(id = R.color.offwhite)
            ),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))
        IconButton(
            modifier = Modifier.align(Alignment.CenterVertically),
            onClick = { //00sccess
                 },
        ){
            Icon(Icons.Filled.MoreVert,null)
        }
    }
            //Chips Section
            var selectedChipIndex by remember {
                mutableStateOf(-1)
            }
            LazyRow(modifier = Modifier.background(colorResource(id = R.color.offwhite))) {
                items(chips.size) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .padding(start = 15.dp, top = 7.dp, bottom = 7.dp)
                            .clickable {
                                selectedChipIndex = it
                                textFieldState = chips[it]
                                Log.i("texttt selected", chips[it])
                                viewModel.searchPokemonList(textFieldState)
                               // RecipeList(textFieldState,navController = navController,viewModel = viewModel)
                            }
                            .clip(RoundedCornerShape(5.dp))
                            .background(
                                if (selectedChipIndex == it) colorResource(id = R.color.grey)
                                else colorResource(id = R.color.lightBlue)
                            )
                            .padding(horizontal = 10.dp, vertical = 7.dp)
                    ) {
                        Text(text = chips[it],fontSize = 15.sp, color = TextWhite)

                    }
                }
            }
        //ChipSection(chips = listOf("Chicken", "Beef", "Soup","Dessert","Vegetarian","Milk","Vegan","Pizza","Donut"))
            RecipeList(textFieldState,navController = navController,viewModel = viewModel)
        }
    }
}


@Composable
fun RecipeCard(
    entry: RecipeListEntry,
    navController: NavController
) {
    Column{
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    //success
                    navController.navigate(
                        "recipe_detail_screen/${entry.id}"
                    )
                },
            elevation = 5.dp,
        ) {
            Column {
                CoilImage(request = ImageRequest.Builder(LocalContext.current)
                    .data(entry.featured_image)
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

                Row(horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 5.dp)
                        .padding(top = 10.dp, bottom = 10.dp)

                ) {
                    Text(text = entry.title,
                        fontFamily = QuickSand,
                        style = MaterialTheme.typography.h3,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.fillMaxWidth(0.8f)
                    )
                    Text(text = entry.rating.toString(),
                        fontFamily = QuickSand,
                        style = MaterialTheme.typography.body1,
                        textAlign = TextAlign.Center
                    )
                }
            }



        }
    }
}
@Composable
fun RecipeRow(
    rowIndex: Int,
    entries: List<RecipeListEntry>,
    navController: NavController
) {
    Column {
        Row{
            RecipeCard(entry = entries[rowIndex], navController = navController)

        }
        Spacer(modifier = Modifier.height(20.dp))
    }

}

@Composable
fun RecipeList(
    searchText: String,
    navController: NavController,
    viewModel: RecipeListViewModel
){
    val recipeList by remember { viewModel.foodList }
    val endReached by remember { viewModel.endReached }
    val loadError by remember { viewModel.loadError }
    val isLoading by remember { viewModel.isLoading }
    val isSearching by remember { viewModel.isSearching }

    LazyColumn(modifier = Modifier.padding(bottom = 56.dp)){
        val itemCount = recipeList.size
        items(itemCount){
            if(it >= itemCount-1 && !endReached && !isLoading && !isSearching){
                viewModel.getAllFood()

            }

            RecipeRow(rowIndex = it, entries = recipeList, navController = navController)
        }
       
        
    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        if (isLoading) {
            CircularProgressIndicator(color = MaterialTheme.colors.primary)
        }
        if (loadError.isNotEmpty()) {
            RetrySection(error = loadError) {
                viewModel.getAllFood()
            }
        }
    }

}

@Composable
fun RetrySection(
    error: String,
    onRetry: () -> Unit
) {
    Column {
        Text(error, color = Color.Red, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { onRetry() },
            modifier = Modifier.align(CenterHorizontally)
        ) {
            Text(text = "Retry")
        }
    }
}


