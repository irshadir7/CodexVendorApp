package com.demo.end.main.intent

sealed class MainScreenIntent {
    class GetProducts() : MainScreenIntent()
    class GetMoreProducts() : MainScreenIntent()

}