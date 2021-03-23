package com.nelson.branch_app


import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.nelson.branch_app.data.ToDoModel
import com.nelson.branch_app.utility.DialogUtilities
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*

/**
 * Adapter class for defining the To-do RecyclerView's layout behavior (i.e. search filtering)
 */

class ToDoAdapter(private val values: List<ToDoModel>,
                  private val frag: ToDoFragment) : RecyclerView.Adapter<ToDoAdapter.ViewHolder>() {

    private var filtered : Vector<ToDoModel> = Vector(values)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = if (viewType == 0) R.layout.todo_header else R.layout.todo_item
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = filtered[position]
        holder.title.text =  item.title

        when (holder.itemViewType) {
            0 -> {
                holder.addToDo?.setOnClickListener {
                    DialogUtilities.showCreateToDoDialog(frag, item)
                }
                holder.itemView.setOnLongClickListener {
                    holder.delete.visibility = if (holder.delete.visibility == View.VISIBLE) View.GONE else View.VISIBLE
                    holder.delete.setOnClickListener {
                        DialogUtilities.showDeleteListDialog(frag, item)
                    }
                    true
                }
            }
            else -> {
                holder.checkBox?.setOnClickListener(null)
                holder.checkBox?.isChecked = item.isChecked
                holder.checkBox?.setOnCheckedChangeListener { _, isChecked ->
                    item.isChecked = isChecked
                    frag.lifecycleScope.launch {
                        frag.todoListViewModel.addToDo(item)
                    }
                }
                holder.itemView.setOnLongClickListener {
                    holder.delete.visibility = if (holder.delete.visibility == View.VISIBLE) View.GONE else View.VISIBLE
                    holder.delete.setOnClickListener {
                        frag.lifecycleScope.launch {
                            frag.todoListViewModel.deleteToDo(item)
                        }
                    }
                    true
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = filtered[position]
        return item.type
    }

    /**
     * "Filters" values by checking if any matches the query
     */
    fun filter(filter: String) {
        filtered.clear()
        if (filter.trim().isEmpty()) {
            filtered = Vector(values)
        } else {
            values.forEach {
                if (it.isMatch(filter.toLowerCase())) filtered.add(it)
            }
        }
        notifyDataSetChanged()
    }

    /**
     * Extension function for checking if ToDoModel matches the query
     */
    private fun ToDoModel.isMatch(text: String) : Boolean {
        return this.title.toLowerCase().contains(text) || this.listId.contains(text)
    }

    override fun getItemCount() = filtered.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.todo_title)
        val delete: ImageView = view.findViewById(R.id.todo_delete)
        val addToDo: ImageView? = view.findViewById(R.id.todo_add)
        val checkBox: CheckBox? = view.findViewById(R.id.checkbox)
    }
}