package com.android.music.ui.fragments

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.music.R
import com.android.music.databinding.FragmentSearchBinding
import com.android.music.muziko.appInterface.PassDataForSelectPlaylist
import com.android.music.muziko.appInterface.VoidCallback
import com.android.music.muziko.dialogs.AddSongFromSongToPlaylistDialog
import com.android.music.muziko.helper.AnimationHelper
import com.android.music.muziko.model.Playlist
import com.android.music.muziko.model.Song
import com.android.music.muziko.repository.RoomRepository
import com.android.music.ui.SongAdapter
import com.android.music.ui.SongViewModel
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SearchFragment : Fragment(), PassDataForSelectPlaylist {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var searchAdapter: SongAdapter
    private lateinit var listSong : ArrayList<*>
    lateinit var viewModel: SongViewModel

    companion object {
        private const val RECOGNIZER_CODE = 1
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(SongViewModel::class.java)
        activity?.runOnUiThread{
            context?.let { viewModel.sendDataToFragment(it) }
            listSong = viewModel.getDataset()
        }

        viewModel.dataset.observe(viewLifecycleOwner, updateListSong)

        searchAdapter = SongAdapter(listSong as ArrayList<Song>, requireContext())
        val rcv = binding.searchRv
        rcv.apply {
            adapter = searchAdapter
            layoutManager = LinearLayoutManager(context)
        }
        binding.search.setOnClickListener {
            AnimationHelper.scaleAnimation(it, object : VoidCallback {
                override fun execute() {
                    val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
                    intent.putExtra(
                        RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                    )
                    intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak Up")
                    startActivityForResult(intent, RECOGNIZER_CODE)
                }
            }, 0.95f)
        }
        searchAdapter.OnDataSend(
            object : SongAdapter.OnDataSend {
                override fun onSend(context: Context, song: Song) {
                    SongFragment.selectedSong = song

                    if (RoomRepository.cachedPlaylistArray.size > 0) {
                        createDialogToSelectPlaylist()
                    } else {
                        Toast.makeText(
                            requireActivity().baseContext,
                            getString(R.string.createPlaylist_error),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

            }
        )

        val searchView = binding.searchSongArtist
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchAdapter.getFilter().filter(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchAdapter.getFilter().filter(newText)
                return true
            }

        })

    }


    @SuppressLint("NotifyDataSetChanged")
    private val updateListSong = Observer<ArrayList<*>> {
        listSong = it
        searchAdapter.listSong = listSong as ArrayList<Song>

    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RECOGNIZER_CODE && resultCode == RESULT_OK) {
            val text = data!!.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            search_song_artist.setQuery(text!![0].toString(), true)
        }
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

    override fun passDataToInvokingFragment(playlist: ArrayList<Playlist>) {
        SongFragment.selectedPlaylists = playlist

        addSongToSelectedPlaylist()

        SongFragment.selectedPlaylists.clear()
    }

    private fun addSongToSelectedPlaylist() {

        for (playlist in SongFragment.selectedPlaylists) {
            addSongToPlaylist(playlist)
        }
    }

    private fun addSongToPlaylist(playlist: Playlist) {
        GlobalScope.launch {

            RoomRepository.addSongsToPlaylist(
                playlist.name,
                SongFragment.selectedSong.id.toString()
            )
        }
    }
}