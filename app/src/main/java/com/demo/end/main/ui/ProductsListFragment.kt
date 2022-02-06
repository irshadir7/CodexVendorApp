package com.demo.end.main.ui

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.demo.end.R
import com.demo.end.foundatiion.mvi.BaseFragment
import com.demo.end.main.adapters.ProductsAdapter
import com.demo.end.main.intent.MainScreenIntent
import com.demo.end.main.model.EndProductsModel
import com.demo.end.main.model.Product
import com.demo.end.foundatiion.utilz.DataState
import com.demo.end.main.vm.ProductsListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.product_detail_fragment.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@AndroidEntryPoint
class ProductsListFragment : BaseFragment(R.layout.product_list_fragment) {

    @Inject
    lateinit var productsListViewModel: ProductsListViewModel
    private var generalProducts = ArrayList<Product>()
    private var sneakersProducts = ArrayList<Product>()
    private lateinit var generalProductsAdapter: ProductsAdapter
    private lateinit var sneakersProductsAdapter: ProductsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUi()
        fetchGeneralProducts()
        fetchSneakerProducts()
        subscribeObservers()
    }

    private fun setUi() {
        loading()
        generalProductsAdapter = ProductsAdapter(
            activityContext(),
            generalProducts
        )
        sneakersProductsAdapter = ProductsAdapter(
            activityContext(),
            sneakersProducts
        )
        generalProductsRV.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = generalProductsAdapter
        }
        sneakerProductsRV.apply {
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = sneakersProductsAdapter
        }

    }

    //As a result, the ViewModel updates the View with new states and is then displayed to the user.
    private fun subscribeObservers() {
        observeGeneralProducts()
        observeSneakerProducts()
    }

    private fun fetchGeneralProducts() {
        lifecycleScope.launch {
            productsListViewModel.userIntent.send(MainScreenIntent.GetProducts())
        }
    }

    private fun fetchSneakerProducts() {
        lifecycleScope.launch {
            productsListViewModel.userIntent.send(MainScreenIntent.GetMoreProducts())
        }
    }

    private fun observeGeneralProducts() {
        productsListViewModel.generalProductsRead.observe(
            viewLifecycleOwner,
            { productsResponse ->
                if (productsResponse is DataState.Success<EndProductsModel>) {
                    stopLoading()
                    if (productsResponse.data.products.isNotEmpty()) {
                        noItems.visibility = View.GONE
                        generalProductsAdapter.setProducts(productsResponse.data.products)
                    } else {
                        noItems.visibility = View.VISIBLE
                    }
                } else if (productsResponse is DataState.Error) {
                    stopLoading()
                }
            })
    }

    private fun observeSneakerProducts() {
        productsListViewModel.sneakerProductsRead.observe(
            viewLifecycleOwner,
            { sneakersProductsResponse ->
                if (sneakersProductsResponse is DataState.Success<EndProductsModel>) {
                    stopLoading()
                    if (sneakersProductsResponse.data.products.isNotEmpty()) {
                        noItems.visibility = View.GONE
                        sneakersProductsAdapter.setProducts(sneakersProductsResponse.data.products)
                    } else {
                        noItems.visibility = View.VISIBLE
                    }

                } else if (sneakersProductsResponse is DataState.Error) {
                    stopLoading()
                }
            })
    }
}