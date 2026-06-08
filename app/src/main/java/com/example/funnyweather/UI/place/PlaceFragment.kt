package com.example.funnyweather.UI.place

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.lifecycle.lifecycleScope
import com.example.funnyweather.Base.BaseFragment
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.example.funnyweather.R
import com.example.funnyweather.WeatherActivity
import com.example.funnyweather.databinding.FragmentPlaceBinding

class PlaceFragment: BaseFragment<FragmentPlaceBinding>(FragmentPlaceBinding::inflate) {

    val viewModel by lazy { ViewModelProvider(this).get(PlaceViewModel::class.java) }
    private lateinit var adapter: PlaceAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.layoutManager= LinearLayoutManager(activity)
        adapter= PlaceAdapter(this,viewModel.placeList)
        binding.recyclerView.adapter=adapter

        var searchJob: Job? = null
        binding.searchPlaceEdit.addTextChangedListener { editable ->
            val content = editable.toString()
            searchJob?.cancel()
            if (content.isNotEmpty()) {
                searchJob = lifecycleScope.launch {
                    delay(500)
                    viewModel.searchPlaces(content)
                }
            } else {
                binding.recyclerView.visibility = View.GONE
                binding.bgImageView.visibility = View.VISIBLE
                viewModel.placeList.clear()
                adapter.notifyDataSetChanged()
            }
        }

        viewModel.placeLiveData.observe(viewLifecycleOwner, Observer{result ->
            val places = result.getOrNull()
            if(places!=null){
                binding.recyclerView.visibility= View.VISIBLE
                binding.bgImageView.visibility= View.VISIBLE
                viewModel.placeList.clear()
                viewModel.placeList.addAll(places)
                adapter.notifyDataSetChanged()
            }else{
                val exception = result.exceptionOrNull()
                Log.e("PlaceFragment", "Search places failed: ${exception?.message}")
                Toast.makeText(activity,"查询不到地点，请查看日志", Toast.LENGTH_SHORT).show()
                exception?.printStackTrace()
            }
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if(viewModel.isPlaceSaved()){
            val place=viewModel.getSavedPlace()
            val intent= Intent(context, WeatherActivity::class.java).apply {
                putExtra("location_lng",place.location.lng)
                putExtra("location_lat",place.location.lat)
                putExtra("place_name",place.name)
            }
            startActivity(intent)
            activity?.finish()
            return
        }
    }


}