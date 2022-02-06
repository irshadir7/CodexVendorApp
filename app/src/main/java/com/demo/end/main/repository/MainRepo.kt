package com.demo.end.main.repository

import android.util.Log
import com.demo.end.main.model.EndProductsModel
import com.demo.end.main.model.Product
import com.demo.end.main.network.EndEcommerceApi
import com.demo.end.foundatiion.utilz.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

import javax.inject.Inject

class MainRepo
@Inject
constructor(private val endEcommerceApi: EndEcommerceApi) {


    fun getGeneralProducts(): Flow<DataState<EndProductsModel>> = flow {
        emit(DataState.Loading)
        try {
            val response: EndProductsModel = endEcommerceApi.getProducts()
            if (response.products.isNullOrEmpty()) emit(DataState.Error(Exception("No Data Found")))

            response.products = response.products.sortProducts()
            emit(DataState.Success(response))
        } catch (e: Exception) {
            Log.i("Excep",e.toString())
            emit(DataState.Error(e))
        }
    }

    fun getSneakersProducts(): Flow<DataState<EndProductsModel>> = flow {
        emit(DataState.Loading)
        try {
            val response: EndProductsModel = endEcommerceApi.getMoreProducts()
            if (response.products.isNullOrEmpty()) emit(DataState.Error(Exception("No Data Found")))
            response.products = response.products.sortProducts()
            emit(DataState.Success(response))
        } catch (e: Exception) {
            Log.i("Excep",e.toString())
            emit(DataState.Error(e))
        }
    }

  private fun  List<Product>.sortProducts(): ArrayList<Product>{
      val sortedProducts = ArrayList<Product>()
      this.apply {
          val hoodiesList = filter {it.id == "2"   }
          val sneakersList = filter {it.id == "1"   }
          sortedProducts.addAll(hoodiesList)
          sortedProducts.addAll(sneakersList)
      }
      return sortedProducts
    }
}