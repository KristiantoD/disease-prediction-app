package com.example.diseaseprediction.view.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.diseaseprediction.api.responses.ListItemResponse
import com.example.diseaseprediction.databinding.ItemRowBinding
import com.example.diseaseprediction.view.detail.DetailDiseaseActivity
import com.example.diseaseprediction.view.detail.DetailMedicineActivity
import com.example.diseaseprediction.view.navigation.NavigationActivity.Companion.EXTRA_REFRESH
import com.example.diseaseprediction.view.navigation.NavigationActivity.Companion.EXTRA_TOKEN

class ListItemAdapter(
    private val listItem: List<ListItemResponse>,
    private val isDisease: Boolean,
    private val accessToken: String,
    private val refreshToken: String
) :
    RecyclerView.Adapter<ListItemAdapter.ListViewHolder>() {
    class ListViewHolder(var binding: ItemRowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListViewHolder {
        val binding =
            ItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val item = listItem[position]
        Glide.with(holder.itemView.context)
            .load(item.img)
            .into(holder.binding.imgItemPhoto)
        holder.binding.tvItemName.text = item.name
        holder.binding.tvItemSummary.text = item.excerpt

        holder.itemView.setOnClickListener {
            val intent = if (isDisease) {
                Intent(holder.itemView.context, DetailDiseaseActivity::class.java)
            } else {
                Intent(holder.itemView.context, DetailMedicineActivity::class.java)
            }

            val imgPhoto: ImageView = holder.binding.imgItemPhoto
            val tvName: TextView = holder.binding.tvItemName
            intent.putExtra(EXTRA_TOKEN, accessToken)
            intent.putExtra(EXTRA_REFRESH, refreshToken)
            intent.putExtra(EXTRA_ITEM, item)

            val p1 = Pair<View, String>(imgPhoto, "photo")
            val p2 = Pair<View, String>(tvName, "name")

            val optionsCompat: ActivityOptionsCompat =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                    holder.itemView.context as Activity,
                    p1,
                    p2
                )
            holder.itemView.context.startActivity(intent, optionsCompat.toBundle())
        }
    }

    override fun getItemCount(): Int {
        return listItem.size
    }

    companion object {
        const val EXTRA_ITEM = "extra_item"
    }
}