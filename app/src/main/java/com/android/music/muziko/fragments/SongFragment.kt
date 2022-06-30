package com.android.music.ui.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.music.R
import com.android.music.databinding.FragmentSongBinding
import com.android.music.muziko.appInterface.PassDataForSelectPlaylist
import com.android.music.muziko.dialogs.AddSongFromSongToPlaylistDialog
import com.android.music.muziko.model.Playlist
import com.android.music.muziko.model.Song
import com.android.music.muziko.repository.RoomRepository
import com.android.music.ui.SongAdapter
import com.android.music.ui.SongViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SongFragment : Fragment(), PassDataForSelectPlaylist {
    private lateinit var binding: FragmentSongBinding
    private lateinit var songAdapter: SongAdapter
    private lateinit var listSong: ArrayList<*>
    companion object {
        lateinit var mactivity: FragmentActivity
        lateinit var selectedSong: Song
        lateinit var selectedPlaylists: ArrayList<Playlist>
    }

    private lateinit var viewModel: SongViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSongBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel = ViewModelProvider(this).get(SongViewModel::class.java)

        activity?.runOnUiThread{
            context?.let { viewModel.sendDataToFragment(it) }
            listSong = viewModel.getDataset()
        }
        viewModel.dataset.observe(viewLifecycleOwner, updateListSong)
        songAdapter = SongAdapter(listSong as ArrayList<Song>, requireContext())
        val rcv = binding.songsRv
        rcv.apply {
            adapter = songAdapter
            layoutManager = LinearLayoutManager(context)
        }


        songAdapter.OnDataSend(
            object : SongAdapter.OnDataSend {
                override fun onSend(context: Context, song: Song) {
                    selectedSong = song

                    if (RoomRepository.cachedPlaylistArray.size > 0) {
                        createDialogToSelectPlaylist()
                    } else {
                        val i = RoomRepository.cachedPlaylistArray
                        Toast.makeText(
                            requireActivity().baseContext,
                            getString(R.string.createPlaylist_error),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

            }
        )
    }

    @SuppressLint("NotifyDataSetChanged")
    private val updateListSong = Observer<ArrayList<*>> {
        listSong = it
        songAdapter.listSong = listSong as ArrayList<Song>

    }

    fun createDialogToSelectPlaylist() {

        RoomRepository.updateCachedPlaylist()

        val addSongToPlaylistDialog = RoomRepository.cachedPlaylistArray.let {
            AddSongFromSongToPlaylistDialog(
                it
            )
        }


        addSongToPlaylistDialog.setTargetFragment(this, 0)
        this.fragmentManager?.let { it1 -> addSongToPlaylistDialog.show(it1, "pl") }

    }

    override fun onResume() {
        super.onResume()
        activity?.runOnUiThread {
            viewModel.updateData()
            mactivity = requireActivity()
        }

    }

    override fun passDataToInvokingFragment(playlist: ArrayList<Playlist>) {
        selectedPlaylists = playlist

        addSongToSelectedPlaylist()

        selectedPlaylists.clear()
    }

    private fun addSongToSelectedPlaylist() {

        for (playlist in selectedPlaylists) {
            addSongToPlaylist(playlist)
        }
    }

    private fun addSongToPlaylist(playlist: Playlist) {
        GlobalScope.launch {

            RoomRepository.addSongsToPlaylist(
                playlist.name,
                selectedSong.id.toString()
            )
        }
    }
}