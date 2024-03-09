package com.example.aviatickets.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.aviatickets.R
import com.example.aviatickets.adapter.OfferListAdapter
import com.example.aviatickets.databinding.FragmentOfferListBinding
import com.example.aviatickets.model.entity.Offer
import com.example.aviatickets.model.network.ApiClient
import com.example.aviatickets.model.network.ApiService
import com.example.aviatickets.model.service.FakeService
//import com.example.aviatickets.model.service.FakeService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class OfferListFragment : Fragment() {

    companion object {
        fun newInstance() = OfferListFragment()
    }

    private var _binding: FragmentOfferListBinding? = null
    private val binding
        get() = _binding!!

    private val adapter: OfferListAdapter by lazy {
        OfferListAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOfferListBinding.inflate(layoutInflater, container, false)



        val call: Call<List<Offer>> = ApiClient.apiService.getOfferList()
        val client = ApiClient.apiService
        val response = client.getOfferList()

        call.enqueue(object : Callback<List<Offer>> {
            override fun onResponse(call: Call<List<Offer>>, response: Response<List<Offer>>) {
                if (response.isSuccessful) {
                    val offerList: List<Offer>? = response.body()
                    if (offerList != null) {
                        Log.d("API_CALL", "Received data from API: $offerList")
                    }
                } else {
                    Log.e("API_CALL", "Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Offer>>, t: Throwable) {
                Log.e("API_CALL", "Network error: ${t.message}")
            }
        })
            return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        adapter.setItems(FakeService.offerList)
//        adapter.setItems(ApiClient.apiService.getOfferList())
    }

    private fun setupUI() {
        with(binding) {
            offerList.adapter = adapter

            sortRadioGroup.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    R.id.sort_by_price -> {
                        adapter.sortByPrice()
                    }

                    R.id.sort_by_duration -> {
                        adapter.sortByDuration()
                    }
                }
            }
        }
    }
}