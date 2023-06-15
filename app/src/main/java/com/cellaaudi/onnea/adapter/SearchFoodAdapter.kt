package com.cellaaudi.onnea.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cellaaudi.onnea.R
import com.cellaaudi.onnea.databinding.ItemFoodBinding
import com.cellaaudi.onnea.model.ResultsItem
import com.cellaaudi.onnea.model.SearchFoodResponse
import com.cellaaudi.onnea.ui.fooddetail.FoodDetailActivity

class SearchFoodAdapter(private val food: List<ResultsItem>): RecyclerView.Adapter<SearchFoodAdapter.ViewHolder>() {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private var txtFood: TextView = itemView.findViewById(R.id.txtSearchFood)
        private var imgFood: ImageView = itemView.findViewById(R.id.imgSearchFood)

        fun bind(foodItem: ResultsItem) {
            Glide.with(itemView.context)
                .load(foodItem.image)
                .into(imgFood)
            txtFood.text = foodItem.title

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, FoodDetailActivity::class.java)
                intent.putExtra(FoodDetailActivity.FOOD_ID, foodItem.id)
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_food, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(food[position])
    }

    override fun getItemCount(): Int = food.size
}