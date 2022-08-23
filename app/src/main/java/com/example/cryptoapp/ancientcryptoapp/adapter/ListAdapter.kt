package com.example.cryptoapp.ancientcryptoapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.cryptoapp.databinding.ListItemTeamMemberBinding
import com.example.cryptoapp.ancientcryptoapp.domain.ListItemTeamMemberModel

class ListAdapter(context: Context, private val items: List<ListItemTeamMemberModel>) :
    ArrayAdapter<ListItemTeamMemberModel>(context, 0, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding = ListItemTeamMemberBinding.inflate(LayoutInflater.from(context), parent, false)
        binding.tvTeamLeader.text = items[position].name
        binding.tvTeamRole.text = items[position].position
        return binding.root
    }
}