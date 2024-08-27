package de.filtik.gronkhtv.chat

import android.view.*
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.filtik.gronkhtv.R

class ChatAdapter(private val chatList: MutableList<String>) :
    RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val messageTextView: TextView = itemView.findViewById(R.id.message_textview)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.chat_message, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val chatMessage = chatList[position]
        holder.messageTextView.text = chatMessage
    }

    override fun getItemCount(): Int {
        return chatList.size
    }
}