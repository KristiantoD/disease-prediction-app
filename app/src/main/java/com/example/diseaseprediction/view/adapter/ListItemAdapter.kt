package com.example.diseaseprediction.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.diseaseprediction.api.responses.ListItemResponse
import com.example.diseaseprediction.databinding.ItemRowBinding

class ListItemAdapter(private val listItem: List<ListItemResponse>) :
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
//            val intentStoryDetail = Intent(holder.itemView.context, DetailStoryActivity::class.java)
//            val imgPhoto: ImageView = holder.binding.imgItemPhoto
//            val tvName: TextView = holder.binding.tvItemName
//            intentStoryDetail.putExtra(EXTRA_PHOTO, story.photoUrl)
//            intentStoryDetail.putExtra(EXTRA_NAME, story.name)
//            intentStoryDetail.putExtra(EXTRA_DESCRIPTION, story.description)
//
//            val p1 = Pair<View, String>(imgPhoto, "photo")
//            val p2 = Pair<View, String>(tvName, "name")
//
//            val optionsCompat: ActivityOptionsCompat =
//                ActivityOptionsCompat.makeSceneTransitionAnimation(
//                    holder.itemView.context as Activity,
//                    p1,
//                    p2
//                )
//            holder.itemView.context.startActivity(intentStoryDetail, optionsCompat.toBundle())
        }
    }

    override fun getItemCount(): Int {
        return listItem.size
    }
}