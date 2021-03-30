package com.example.prototype

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.w3c.dom.Text

class ProductAdapter(val products: ArrayList<ProductModel>) : RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val productName: TextView = view.findViewById(R.id.product_name)
        val energy: TextView = view.findViewById(R.id.energy)
        val protein: TextView = view.findViewById(R.id.protein)
        val carbs: TextView = view.findViewById(R.id.carbs)
        val owsugar: TextView = view.findViewById(R.id.owsugar)
        val addedSugar: TextView = view.findViewById(R.id.added_sugar)
        val dietaryFibre: TextView = view.findViewById(R.id.dietary_fibre)
        val fats: TextView = view.findViewById(R.id.fats)
        val sfa: TextView = view.findViewById(R.id.sfa)
        val mfa: TextView = view.findViewById(R.id.mfa)
        val pfa: TextView = view.findViewById(R.id.pfa)
        val tfa: TextView = view.findViewById(R.id.tfa)
        val cholestrol: TextView = view.findViewById(R.id.cholestrol)
        val iron: TextView = view.findViewById(R.id.iron)
        val vitb1: TextView = view.findViewById(R.id.vitb1)
        val sodium: TextView = view.findViewById(R.id.sodium)
        val calcium: TextView = view.findViewById(R.id.calcium)
        val phosphorus: TextView = view.findViewById(R.id.phosphorus)
        val vita: TextView = view.findViewById(R.id.vita)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_product, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.productName.text = products[position].productName
        holder.energy.text = products[position].energy
        holder.protein.text = products[position].protein
        holder.carbs.text = products[position].carb
        holder.owsugar.text = products[position].owSugar
        holder.addedSugar.text = products[position].addedSugar
        holder.dietaryFibre.text = products[position].dietaryFibre
        holder.fats.text = products[position].fats
        holder.sfa.text = products[position].sfa
        holder.mfa.text = products[position].mfa
        holder.pfa.text = products[position].pfa
        holder.tfa.text = products[position].tfa
        holder.cholestrol.text = products[position].cholestrol
        holder.iron.text = products[position].iron
        holder.vitb1.text = products[position].vitb1
        holder.sodium.text = products[position].sodium
        holder.calcium.text = products[position].calcium
        holder.phosphorus.text = products[position].phosphorus
        holder.vita.text = products[position].vita
    }

    override fun getItemCount(): Int {
        return products.size
    }

}
