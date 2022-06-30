package com.android.music.muziko.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.music.databinding.ItemAddPlaylistBinding
import com.android.music.muziko.model.Song
import com.android.music.muziko.utils.ImageUtils

class AddPlaylistAdapter(var context: Activity, var listSong: ArrayList<Song>) : RecyclerView.Adapter<AddPlaylistAdapter.AddPlaylistViewHolder> () {

    var dataset: ArrayList<Song> = listSong

    inner class AddPlaylistViewHolder(var binding: ItemAddPlaylistBinding): RecyclerView.ViewHolder(binding.root){
        var name = binding.txtNameAddPlaylist
        var artist = binding.txtArtistAddPlaylist
        var imageSong = binding.imgAddPlaylist
        fun bind(song: Song){
            name.text = song.title
            artist.text = song.artist
            song.image?.let {
                ImageUtils.loadImageToImageView(
                    context = context,
                    imageView = imageSong,
                    image = it
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddPlaylistViewHolder {
        val binding = ItemAddPlaylistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AddPlaylistViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AddPlaylistViewHolder, position: Int) {
        holder.bind(dataset[position])
    }

    override fun getItemCount(): Int {
        return dataset.size
    }
}