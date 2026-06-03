package com.example.funnyweather.Base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<vb: ViewBinding>(val inflate:(LayoutInflater)->vb): AppCompatActivity() {

    protected lateinit var binding: vb

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = inflate(layoutInflater)
        setContentView(binding.root)
    }


}