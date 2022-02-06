package com.demo.end.main.ui

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.demo.end.R
import com.demo.end.main.vm.WishListViewModel

class WishListFragment : Fragment() {

    companion object {
        fun newInstance() = WishListFragment()
    }

    private lateinit var viewModel: WishListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.wish_list_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(WishListViewModel::class.java)
        // TODO: Use the ViewModel
    }

}