package com.codex.ventorapp.main.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.codex.ventorapp.R

import com.codex.ventorapp.foundatiion.mvi.BaseActivity
import com.codex.ventorapp.foundatiion.utilz.DataState
import com.codex.ventorapp.foundatiion.utilz.SessionManager
import com.codex.ventorapp.foundatiion.utilz.Utils
import com.codex.ventorapp.main.accessToken.intent.AccessTokenIntents
import com.codex.ventorapp.main.accessToken.model.AccessTokenModel
import com.codex.ventorapp.main.accessToken.vm.AccessTokenViewModels
import com.codex.ventorapp.main.adapters.PhotoAdapter
import com.codex.ventorapp.main.login.intent.LoginIntent
import com.codex.ventorapp.main.login.model.LoginSuccessData
import com.codex.ventorapp.main.login.vm.LoginViewModel
import com.codex.ventorapp.main.model.DataModel
import com.codex.ventorapp.main.onboarding.DatabaseChooseActivity
import com.codex.ventorapp.main.onboarding.OnBoardingActivity
import com.codex.ventorapp.main.signup.intent.SignUpIntent
import com.codex.ventorapp.main.signup.model.SignupSuccessModel
import com.codex.ventorapp.main.signup.vm.SignUpViewModel
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@AndroidEntryPoint
class MainActivity : BaseActivity() {
    @Inject
    lateinit var accessTokenViewModel: AccessTokenViewModels

    @Inject
    lateinit var loginViewModel: LoginViewModel

    @Inject
    lateinit var signUpViewModel: SignUpViewModel
    private var dataList = mutableListOf<DataModel>()
    private lateinit var photoAdapter: PhotoAdapter
    private lateinit var session: SessionManager
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var actionBarToggle: ActionBarDrawerToggle
    private lateinit var navView: NavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        session = SessionManager(this)
        init()

        drawerLayout = findViewById(R.id.drawer_layout)

        // Pass the ActionBarToggle action into the drawerListener
        actionBarToggle = ActionBarDrawerToggle(this, drawerLayout, 0, 0)
        drawerLayout.addDrawerListener(actionBarToggle)

        // Display the hamburger icon to launch the drawer
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        // Call syncState() on the action bar so it'll automatically change to the back button when the drawer layout is open
        actionBarToggle.syncState()


        // Call findViewById on the NavigationView
        navView = findViewById(R.id.navView)

        // Call setNavigationItemSelectedListener on the NavigationView to detect when items are clicked
        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.feedback -> {
                    Toast.makeText(this, "My feedback", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.settings -> {
                    Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.about -> {
                    Toast.makeText(this, "About", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.logout -> {
                    session.clearSession()
                    val intent = Intent(this@MainActivity, OnBoardingActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    true
                }
                else -> {
                    false
                }
            }
        }

        drawerLayout.isFocusableInTouchMode = false
    }


    private fun init() {
        photoAdapter = PhotoAdapter(applicationContext)

        recyclerview.setHasFixedSize(true)
        recyclerview.apply {
            layoutManager = GridLayoutManager(
                context,
                3
            )
            adapter = photoAdapter
        }
        prepareItems()
        authRefresh()
    }

    private fun prepareItems() {
        dataList.add(DataModel(1, "Purchase", R.drawable.page_wh__state_default))
        dataList.add(DataModel(2, "Receipts", R.drawable.page_wh__state_default))
        dataList.add(DataModel(3, "Delivery Orders", R.drawable.page_wh__state_default))
        dataList.add(DataModel(4, "Returns", R.drawable.page_wh__state_default))
        dataList.add(DataModel(5, "POS Orders", R.drawable.page_wh__state_default))
        dataList.add(DataModel(6, "Batch picking", R.drawable.page_batch__state_default))
        dataList.add(DataModel(7, "Internal transfers", R.drawable.page_internal__state_default))
        dataList.add(DataModel(8, "Quick info", R.drawable.page_quick__state_default))
        dataList.add(DataModel(9, "Putaway", R.drawable.page_putaway))
        dataList.add(DataModel(10, "Instant inventory", R.drawable.page_ii__state_default))
        dataList.add(
            DataModel(
                11,
                "Inventory adjustment",
                R.drawable.page_inventory__state_default
            )
        )
        dataList.add(DataModel(12, "External transfers", R.drawable.page_external__state_default))
        dataList.add(DataModel(13, "MO and WO management", R.drawable.page_manufacturing))
        dataList.add(DataModel(14, "Create SO", R.drawable.page_sales__state_default))
        dataList.add(DataModel(15, "Create PO", R.drawable.page_purchase__state_default))
        photoAdapter.setDataList(dataList)
    }


    private fun authRefresh() {
        lifecycleScope.launch {
            accessTokenViewModel.userIntent.send(
                AccessTokenIntents.GetAccessToken(
                    Utils.CLIENT_ID,
                    Utils.CLIENT_SECRET,
                    Utils.GRANT_TYPE
                )
            )
        }

        subscribeObservers()
    }


    private fun authSignUp(token: String) {
        lifecycleScope.launch {
            signUpViewModel.userIntent.send(
                SignUpIntent.GetSignUP(
                    token,
                    "mpirshu06@gmail.com",
                    "Irshad",
                    "Irshad123"
                )
            )
        }

        subscribeObserversForSignUp()
    }

    private fun subscribeObservers() {
        accessTokenViewModel.accessTokenRead.observe(
            this
        ) { accessTokenRead ->
            when (accessTokenRead) {
                is DataState.Success<AccessTokenModel> -> {
                    Log.d("AccessToken", accessTokenRead.data.toString())
                    accessTokenViewModel.accessTokenRead.removeObservers(this)
                    authSignUp(accessTokenRead.data.access_token)
                }
                is DataState.Error -> {
                    Log.d("AccessToken", accessTokenRead.exception.toString())
                    accessTokenViewModel.accessTokenRead.removeObservers(this)
                }
                is DataState.Loading -> {

                }
                else -> {
                    accessTokenViewModel.accessTokenRead.removeObservers(this)
                }
            }
        }
    }




    private fun subscribeObserversForSignUp() {
        signUpViewModel.signUpDataRead.observe(
            this
        ) { signUpDataRead ->
            when (signUpDataRead) {
                is DataState.Success<SignupSuccessModel> -> {
                    Log.d("SignUpFlow", signUpDataRead.data.toString())
                    signUpViewModel.signUpDataRead.removeObservers(this)

                }
                is DataState.ServerError -> {
                    Log.d("SignUpFlow", signUpDataRead.response.toString())
                    signUpViewModel.signUpDataRead.removeObservers(this)
                }
                is DataState.Error -> {
                    Log.d("SignUpFlow", signUpDataRead.exception.toString())
                    signUpViewModel.signUpDataRead.removeObservers(this)
                }
                is DataState.Loading -> {

                }
                else -> {
                    signUpViewModel.signUpDataRead.removeObservers(this)
                }
            }
        }
    }

    // override the onSupportNavigateUp() function to launch the Drawer when the hamburger icon is clicked
    override fun onSupportNavigateUp(): Boolean {
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            drawerLayout.openDrawer(navView)
        }
        return true
    }

}