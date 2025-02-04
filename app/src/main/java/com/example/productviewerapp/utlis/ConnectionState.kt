package com.example.productviewerapp.utlis

sealed class ConnectionState {
    object Available : ConnectionState()
    object Unavailable : ConnectionState()
}
