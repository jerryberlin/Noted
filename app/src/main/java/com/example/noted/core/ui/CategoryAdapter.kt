package com.example.noted.core.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.noted.R
import com.example.noted.core.domain.model.Category
import com.example.noted.databinding.ItemListCategoryBinding

class CategoryAdapter(
    private var listCategory: List<Category>
) : RecyclerView.Adapter<CategoryAdapter.ListViewHolder>() {

    private var selectedChipPosition: Int = 0

    private lateinit var onItemClickCallback: OnItemClickCallback
    private lateinit var onItemLongClickCallback: OnItemLongClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun setOnItemLongClickCallback(onItemLongClickCallback: OnItemLongClickCallback) {
        this.onItemLongClickCallback = onItemLongClickCallback
    }

    class ListViewHolder(var binding: ItemListCategoryBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding =
            ItemListCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val category = listCategory[position]
        holder.apply {
            binding.itemChip.text = category.categoryName
            val context = itemView.context

            val isSelected = holder.adapterPosition == selectedChipPosition
            binding.itemChip.also {
                it.setTextColor(
                    ContextCompat.getColor(
                        context,
                        if (isSelected) R.color.black else R.color.light_grey
                    )
                )
                it.setChipStrokeColorResource(
                    if(isSelected) R.color.black else R.color.light_grey
                )
            }


            binding.itemChip.setOnClickListener {
                val previousSelectedPosition = selectedChipPosition
                selectedChipPosition = holder.adapterPosition
                notifyItemChanged(previousSelectedPosition)
                notifyItemChanged(selectedChipPosition)

                onItemClickCallback.onItemClicked(listCategory[holder.adapterPosition])
            }

            binding.itemChip.setOnLongClickListener {
                if (holder.adapterPosition != 0) {
                    onItemLongClickCallback.onItemLongClicked(listCategory[holder.adapterPosition])
                }
                true
            }
        }
    }


    override fun getItemCount(): Int =
        listCategory.size


    interface OnItemClickCallback {
        fun onItemClicked(data: Category)
    }

    interface OnItemLongClickCallback {
        fun onItemLongClicked(data: Category)
    }
}