package com.android.music.muziko.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.music.R
import com.android.music.databinding.AddSongFromSongToPlaylistBinding
import com.android.music.muziko.adapter.AddSongFromSongToPlaylistAdapter
import com.android.music.muziko.appInterface.PassDataForSelectPlaylist
import com.android.music.muziko.model.Playlist
import com.android.music.muziko.utils.ScreenSizeUtils
import kotlinx.android.synthetic.main.add_song_from_song_to_playlist.view.*

class AddSongFromSongToPlaylistDialog (val array: ArrayList<Playlist>) : DialogFragment() {

    lateinit var binding: AddSongFromSongToPlaylistBinding
    var playlistAdapter: AddSongFromSongToPlaylistAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = AddSongFromSongToPlaylistBinding.inflate(inflater, container, false)
        dialog!!.window?.setBackgroundDrawableResource(R.drawable.round_corner_bg)

        playlistAdapter = activity?.let {
            AddSongFromSongToPlaylistAdapter(
                it,
                array
            )
        }

        binding.recyclerviewAddSongFromSongDialog.layoutManager = LinearLayoutManager(context)
        binding.recyclerviewAddSongFromSongDialog.adapter = playlistAdapter

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.addSongFromSongToPlaylistLayout.layoutParams.width =
            ScreenSizeUtils.getScreenWidth() * 6 / 10
        binding.addSongFromSongToPlaylistLayout.layoutParams.height =
            ScreenSizeUtils.getScreenHeight() * 6 / 10
        binding.addSongFromSongToPlaylistLayout.requestLayout()

        binding.addSongFromSongToPlaylistLayout.recyclerview_add_song_from_song_dialog.layoutParams.width =
            binding.addSongFromSongToPlaylistLayout.layoutParams.width * 10 / 10
        binding.addSongFromSongToPlaylistLayout.recyclerview_add_song_from_song_dialog.layoutParams.height =
            (binding.addSongFromSongToPlaylistLayout.layoutParams.height* 6.5 / 10).toInt()
        binding.addSongFromSongToPlaylistLayout.recyclerview_add_song_from_song_dialog.requestLayout()

        binding.addSongFromSongToPlaylistLayout.btn_ok_add_song_dialog.layoutParams.width =
            binding.addSongFromSongToPlaylistLayout.layoutParams.width * 4 / 10
        binding.addSongFromSongToPlaylistLayout.btn_ok_add_song_dialog.layoutParams.height =
            (binding.addSongFromSongToPlaylistLayout.layoutParams.height* 1.2 / 10).toInt()
        binding.addSongFromSongToPlaylistLayout.btn_ok_add_song_dialog.requestLayout()

    }

    override fun onResume() {

        super.onResume()

        binding.btnOkAddSongDialog.setOnClickListener {

            val targetFragment = targetFragment
            val passData: PassDataForSelectPlaylist = targetFragment as PassDataForSelectPlaylist
            passData.passDataToInvokingFragment(AddSongFromSongToPlaylistAdapter.choices)

            this.dismiss()
        }
    }

}