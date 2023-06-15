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
import com.cellaaudi.onnea.model.ResultsItem
import com.cellaaudi.onnea.ui.addfood.AddFoodDetailActivity

class AddFoodAdapter(private val food: List<ResultsItem>, private val day: Int, private val month: Int, private val type: String): RecyclerView.Adapter<AddFoodAdapter.ViewHolder>() {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private var txtFood: TextView = itemView.findViewById(R.id.txtSearchFood)
        private var imgFood: ImageView = itemView.findViewById(R.id.imgSearchFood)

        fun bind(foodItem: ResultsItem, day: Int, month: Int, type: String) {
            Glide.with(itemView.context)
                .load(foodItem.image)
                .into(imgFood)
            txtFood.text = foodItem.title

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, AddFoodDetailActivity::class.java)
                intent.putExtra(AddFoodDetailActivity.FOOD_ID, foodItem.id)
                intent.putExtra(AddFoodDetailActivity.DAY, day)
                intent.putExtra(AddFoodDetailActivity.MONTH, month)
                intent.putExtra(AddFoodDetailActivity.TYPE, type)
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_food, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(food[position], day, month, type)
    }

    override fun getItemCount(): Int = food.size
}