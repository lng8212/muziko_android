package com.android.music.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.music.databinding.FragmentFavouriteBinding
import com.android.music.muziko.adapter.FavAdapter
import com.android.music.muziko.model.Song
import com.android.music.muziko.viewmodel.FavViewModel

class FavouriteFragment : Fragment() {
    private lateinit var binding: FragmentFavouriteBinding
    private lateinit var favSongsAdapter: FavAdapter
    private lateinit var listFavSong: ArrayList<*>

    companion object {
        var viewModel: FavViewModel? = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentFavouriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel?.updateData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(this).get(FavViewModel::class.java)
        activity?.runOnUiThread {
            context?.let { viewModel!!.sendDataToFragment() }
            listFavSong = viewModel!!.getDataset()
        }

        viewModel!!.dataset.observe(viewLifecycleOwner, favSongsObserver)
        favSongsAdapter = FavAdapter(listFavSong as ArrayList<Song>, requireContext())

        val recyclerView = binding.recyclerviewFavourite
        recyclerView.apply {
            adapter = favSongsAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }
    private val favSongsObserver = Observer<ArrayList<*>> {
        favSongsAdapter.listSong = it as ArrayList<Song>
        binding.recyclerviewFavourite.adapter = favSongsAdapter
    }

}