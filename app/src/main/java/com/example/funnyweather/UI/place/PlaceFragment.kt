package com.example.funnyweather.UI.place

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.funnyweather.Base.BaseFragment
import com.example.funnyweather.R
import com.example.funnyweather.databinding.FragmentPlaceBinding

class PlaceFragment: BaseFragment<FragmentPlaceBinding>(FragmentPlaceBinding::inflate) {

    val viewModel by lazy { ViewModelProvider(this).get(PlaceViewModel::class.java) }
    private lateinit var adapter: PlaceAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.layoutManager= LinearLayoutManager(activity)
        adapter= PlaceAdapter(this,viewModel.placeList)
        binding.recyclerView.adapter=adapter

        binding.searchPlaceEdit.addTextChangedListener{
            editable ->
            val content=editable.toString()
            if(content.isNotEmpty()){
                viewModel.searchPlaces(content)
            }else{
                binding.recyclerView.visibility= View.GONE
                binding.bgImageView.visibility= View.VISIBLE
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
                Toast.makeText(activity,"查询不到地点,检查网络.", Toast.LENGTH_SHORT).show()
                result.exceptionOrNull()?.printStackTrace()
            }
        })
    }


}