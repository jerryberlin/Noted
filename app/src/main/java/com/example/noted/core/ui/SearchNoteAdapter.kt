package com.example.noted.core.ui

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.noted.core.domain.model.Note
import com.example.noted.databinding.ItemListNoteBinding
import com.example.noted.ui.detail.DetailNoteActivity
import io.noties.markwon.AbstractMarkwonPlugin
import io.noties.markwon.Markwon
import io.noties.markwon.MarkwonVisitor
import io.noties.markwon.ext.strikethrough.StrikethroughPlugin
import io.noties.markwon.ext.tasklist.TaskListPlugin
import io.noties.markwon.movement.MovementMethodPlugin
import org.commonmark.node.SoftLineBreak

class SearchNoteAdapter(private var listNote: List<Note>) :
    RecyclerView.Adapter<SearchNoteAdapter.ListViewHolder>() {

    private lateinit var onItemLongClickCallback: OnItemLongClickCallback

    fun setOnItemLongClickCallback(onItemLongClickCallback: OnItemLongClickCallback) {
        this.onItemLongClickCallback = onItemLongClickCallback
    }

    class ListViewHolder(var binding: ItemListNoteBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding =
            ItemListNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val notes = listNote[position]
        val markWon = Markwon.builder(holder.itemView.context)
            .usePlugin(StrikethroughPlugin.create())
            .usePlugin(TaskListPlugin.create(holder.itemView.context))
            .usePlugin(MovementMethodPlugin.none())
            .usePlugin(object : AbstractMarkwonPlugin() {
                override fun configureVisitor(builder: MarkwonVisitor.Builder) {
                    super.configureVisitor(builder)
                    builder.on(
                        SoftLineBreak::class.java
                    ) { visitor, _ -> visitor.forceNewLine() }
                }
            }).build()
        holder.apply {
            binding.itemTitle.text = notes.noteTitle
            markWon.setMarkdown(binding.itemContent, notes.noteContent)
            binding.itemDate.text = notes.noteDate
            binding.itemCardView.setBackgroundColor(notes.noteColor)
            binding.itemCardView.setOnClickListener {
                val intent = Intent(itemView.context, DetailNoteActivity::class.java)
                intent.putExtra("noteId", notes.noteId)
                itemView.context.startActivity(intent)
            }
            binding.itemCardView.setOnLongClickListener {
                onItemLongClickCallback.onItemLongClicked(listNote[holder.adapterPosition])
                true
            }
        }
    }

    override fun getItemCount(): Int =
        listNote.size

    interface OnItemLongClickCallback {
        fun onItemLongClicked(data: Note)
    }
}