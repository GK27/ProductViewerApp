package com.example.productviewerapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.productviewerapp.viewmodel.ProductViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Snackbar
import androidx.compose.runtime.LaunchedEffect
import com.example.productviewerapp.model.Product
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.productviewerapp.utlis.CommonValues.Companion.GRID_COUNT
import com.example.productviewerapp.utlis.ConnectionState
import com.example.productviewerapp.utlis.Helper
import com.example.productviewerapp.utlis.Helper.NoDataFound
import com.example.productviewerapp.utlis.Helper.PageHeader
import com.example.productviewerapp.utlis.connectivityState
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val productViewModel: ProductViewModel = viewModel()
            ProductListScreen(productViewModel = productViewModel)
        }

    }
}

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun ProductListScreen(productViewModel: ProductViewModel = viewModel()) {
    //Check network connetion
    val connection by connectivityState()
    val isConnected = connection === ConnectionState.Available

    var showNoDataMessage by remember { mutableStateOf(false) }
    var searchEnable by remember { mutableStateOf(true) }
    var searchQuery by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }

    //Get data from viewmodel
    val isLoading by productViewModel.isLoading.collectAsState()
    val products by productViewModel.products.collectAsState()


    val swipeRefreshState = remember { SwipeRefreshState(isRefreshing = isLoading) }

    //To filter the products based on the search query - based on title
    val filteredProducts = remember(searchQuery, products) {
        derivedStateOf {
            if (searchQuery.isEmpty()) {
                products
            } else {
                productViewModel.filterProductsByTitle(searchQuery)
            }
        }.value
    }

    Scaffold(snackbarHost = {
        SnackbarHost(
            snackbarHostState,
            snackbar = {
                Snackbar(
                    snackbarData = it,
                    modifier = Modifier.padding(16.dp),
                    containerColor = Color.Red,
                    contentColor = Color.White
                )
            }
        )
    }) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues))
    }
    Column(modifier = Modifier.fillMaxSize()) {
        //Header and search
        PageHeader(stringResource(R.string.explore_products))
        SearchBar(searchQuery, searchEnable) { searchQuery = it }
        //SwipeRefresh
        SwipeRefresh(
            state = swipeRefreshState, onRefresh = {
                productViewModel.fetchProducts()
            }, modifier = Modifier.fillMaxSize()
        ) {

            if (isConnected) {
                searchEnable = true
                ProductList(filteredProducts, searchQuery, showNoDataMessage)
            } else {
                searchEnable = false
                Helper.NetworkStatus(stringResource(R.string.no_network))
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(
                            Alignment.Center
                        )
                    )
                }
            }
        }
    }

    //To show the no data if search query value is empty
    LaunchedEffect(searchQuery) {
        showNoDataMessage = filteredProducts.isEmpty() && searchQuery.isNotBlank()
    }

    //To fetch products from API
    LaunchedEffect(isConnected) {
        if (isConnected) {
            productViewModel.fetchProducts()
        }
        //Show error message
        productViewModel.errorMessage.collectLatest { errorMessage ->
            if (errorMessage.isNotEmpty()) {
                snackbarHostState.showSnackbar(
                    message = errorMessage,
                    duration = SnackbarDuration.Short
                )
            }
            searchEnable = false
        }

    }

}


//Product list item -- Show the product list item - Initially
// if we search the item show the filter item based on the search query
@Composable
fun ProductList(filteredProducts: List<Product>, searchQuery: String, showNoDataMessage: Boolean) {
    if (showNoDataMessage) {
        NoDataFound(searchQuery)
    } else {
        LazyVerticalGrid(
            columns = GridCells.Fixed(GRID_COUNT), modifier = Modifier.fillMaxSize()
        ) {
            items(filteredProducts.size) { index ->
                ProductCard(product = filteredProducts[index])
            }
        }
    }
}

// Product item - Info
@Composable
fun ProductCard(product: Product) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            ImageWithLoading(product.image)
            Text(
                text = product.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = stringResource(R.string.price_symbol, product.price),
                style = MaterialTheme.typography.titleSmall,
                color = Color.Gray
            )
            Text(
                text = stringResource(R.string.category, product.category),
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
            RatingBar(rating = product.rating.rate)
        }
    }
}

//Product image loading
@Composable
fun ImageWithLoading(url: String) {
    var isLoading by remember { mutableStateOf(true) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp), contentAlignment = Alignment.Center
    ) {

        if (isLoading) {
            CircularProgressIndicator()
        }
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current).data(url).crossfade(true)
                .listener(onStart = {
                    isLoading = true
                }, onSuccess = { _, _ ->
                    isLoading = false
                }, onError = { _, _ -> isLoading = false }).build(),
            contentDescription = null,
            modifier = Modifier
                .height(160.dp)
                .fillMaxWidth()
                .padding(8.dp),
            placeholder = painterResource(R.drawable.placeholder_im),
            error = painterResource(R.drawable.error_im)
        )
    }
}

// Ratingbar showing based on the values
@Composable
fun RatingBar(rating: Float) {
    Row(modifier = Modifier.padding(4.dp)) {
        repeat(5) { index ->
            val filled = index < rating.toInt()
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = stringResource(R.string.rating_star),
                tint = if (filled) colorResource(R.color.purple_500) else Color.LightGray,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ProductListScreen()
}

//Customized Searchbar
@Composable
fun SearchBar(query: String, visibility: Boolean, onQueryChange: (String) -> Unit) {
    var isFocused by remember { mutableStateOf(false) }
    val purpleColor = colorResource(id = R.color.purple_500)

    val keyboardController = LocalSoftwareKeyboardController.current
    val backgroundColor = if (isFocused) Color(0xFFE0E0E0) else Color.White
    TextField(
        enabled = visibility,
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .border(
                width = 1.dp,
                color = if (visibility) purpleColor else Color.Gray,
                shape = RoundedCornerShape(12.dp)
            )
            .background(
                backgroundColor, RoundedCornerShape(12.dp)
            )
            .shadow(4.dp, shape = RoundedCornerShape(12.dp))
            .clickable { isFocused = true },
        placeholder = {
            Text(
                stringResource(R.string.search),
                color = Color.Gray,
                style = TextStyle(fontSize = 14.sp)
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = stringResource(R.string.search_icon),
                tint = Color.Gray
            )
        },
        textStyle = TextStyle(color = Color.Black, fontSize = 16.sp),
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        keyboardActions = KeyboardActions(onDone = {
            keyboardController?.hide()
        }),
    )
}

