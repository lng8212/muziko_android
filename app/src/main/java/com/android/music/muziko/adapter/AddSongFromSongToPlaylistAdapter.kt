package com.android.music.muziko.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.music.databinding.ItemAddSongFromSongToPlaylistsDialogBinding
import com.android.music.muziko.appInterface.VoidCallback
import com.android.music.muziko.helper.AnimationHelper
import com.android.music.muziko.model.Playlist

class AddSongFromSongToPlaylistAdapter (var context: Activity, var list: ArrayList<Playlist>) : RecyclerView.Adapter<AddSongFromSongToPlaylistAdapter.AddSongFromSongToPlaylistViewHolder>(){

    companion object {
        var choices: ArrayList<Playlist> = arrayListOf()
    }

    inner class AddSongFromSongToPlaylistViewHolder(var binding: ItemAddSongFromSongToPlaylistsDialogBinding) : RecyclerView.ViewHolder(binding.root){
        var name = binding.textView2
        fun bind(playlist: Playlist){
            name.text = playlist.name
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AddSongFromSongToPlaylistViewHolder {
        val binding = ItemAddSongFromSongToPlaylistsDialogBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return  AddSongFromSongToPlaylistViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AddSongFromSongToPlaylistViewHolder, position: Int) {
        val pos = position
        holder.bind(list[pos])
        holder.binding.materialCheckBox.setOnClickListener {
            AnimationHelper.scaleAnimation(it, object : VoidCallback {
                override fun execute() {
                    if(holder.binding.materialCheckBox.isChecked){
                        choices.add(list[pos])
                    } else {
                        choices.remove(list[pos])
                    }
                }
            }, 0.95f)

        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}