package com.dichotome.moviebrowser.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.dichotome.moviebrowser.R

class MainActivity : AppCompatActivity() {

    lateinit var navController: NavController
    private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navController = findNavController(R.id.host_fragment)
    }

    val logic get() = ViewModelProviders.of(this@MainActivity).get(MainLogic::class.java)

    override fun onSupportNavigateUp(): Boolean = findNavController(R.id.host_fragment).navigateUp()
}