package com.project.VeterinaryManager.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.VeterinaryManager.databinding.ServicesItemBinding
import com.project.VeterinaryManager.model.Services

class ServicesAdapter(
    private val context: Context,
    private val ServicesList: MutableList<Services>
) :
    RecyclerView.Adapter<ServicesAdapter.ServicesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServicesViewHolder {
        val itemList = ServicesItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return ServicesViewHolder(itemList)
    }

    override fun getItemCount() = ServicesList.size

    override fun onBindViewHolder(holder: ServicesViewHolder, position: Int) {
        holder.imgServices.setImageResource(ServicesList[position].img!!)
        holder.txtServices.text = ServicesList[position].nome
    }

    inner class ServicesViewHolder(binding: ServicesItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val imgServices = binding.imgServices
        val txtServices = binding.txtServices
    }

}