package com.example.productviewerapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.productviewerapp.model.Product
import com.example.productviewerapp.network.RetrofitInstance.apiService
import com.example.productviewerapp.utlis.CommonValues.Companion.NETWORK_TIMEOUT
import com.example.productviewerapp.utlis.CommonValues.Companion.NO_NETWORK
import com.example.productviewerapp.utlis.CommonValues.Companion.SERVER_ERROR
import com.example.productviewerapp.utlis.CommonValues.Companion.UNEXPECTED_ERROR
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductViewModel() : ViewModel() {

    //Set product list value
    private var _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products

    //To show progressbar based on this value
    val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading = _isLoading

    private val _errorMessage = MutableStateFlow<String>("")
    val errorMessage: StateFlow<String> = _errorMessage

    //To fetch the product list item
    fun fetchProducts() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val productList = apiService.getProducts()
                _products.value = productList
                _isLoading.value = false
            } catch (e: Exception) {
                _isLoading.value = false
                when (e) {
                    is java.net.UnknownHostException -> {
                        _errorMessage.value = NO_NETWORK
                    }

                    is java.net.SocketTimeoutException -> {
                        _errorMessage.value = NETWORK_TIMEOUT
                    }

                    is retrofit2.HttpException -> {
                        _errorMessage.value = SERVER_ERROR
                    }

                    else -> {
                        _errorMessage.value = UNEXPECTED_ERROR + ": ${e.localizedMessage}"
                    }
                }
            }
        }
    }

    //search by title from the list item
    fun filterProductsByTitle(query: String): List<Product> {
        return _products.value.filter { it.title.contains(query, ignoreCase = true) }
    }


}


