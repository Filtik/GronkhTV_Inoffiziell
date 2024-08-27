package de.filtik.gronkhtv.chat

import android.graphics.drawable.Drawable
import android.os.Build
import android.text.Spanned
import android.text.style.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import de.filtik.gronkhtv.R
import de.filtik.gronkhtv.classes.MovieManager.requireContext
import pl.droidsonroids.gif.GifDrawable
import pl.droidsonroids.gif.GifTextView

class MessageAdapter(private val messages: List<Spanned>) :
    RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    private var even = false

    class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val messageText: TextView = itemView.findViewById(R.id.message_textview)
        val imageView: GifTextView = itemView.findViewById(R.id.message_imageview)
        val linearLayout: LinearLayout = itemView.findViewById(R.id.message_layout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.chat_message, parent, false)
        return MessageViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages[position]

        holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.even_background_color))
//        even = if (even) {
//            holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.even_background_color))
//            false
//        } else {
////            holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.odd_background_color))
//            true
//        }

//        val drawables = message.getSpans(0, message.length, ImageSpan::class.java)
//        val spans = message.getSpans(0, message.length, Any::class.java)
//
//        for (span in spans) {
//            val start = message.getSpanStart(span)
//            val end = message.getSpanEnd(span)
//
//            if (span::class.java == ImageSpan::class.java && start > 5) {
//                val url = MyChatFragment.emoteList[message.subSequence(start, end).toString()]
//                val imageView = ImageView(holder.linearLayout.context)
//
//                val imageLoader = GifDownloader()
//                imageLoader.execute(url)
//
//                val layoutParams = LinearLayout.LayoutParams(28, 28) // Hier musst du die gewünschte Breite und Höhe angeben
//                imageView.layoutParams = layoutParams
//
//                val drawable: Drawable? = imageLoader.get()
//                if (drawable != null) {
//                    drawable.setBounds(0, 0, 28, 28)
//
//                    imageView.setImageDrawable(drawable)
//                    holder.linearLayout.addView(imageView)
//                }
//            }
//            else if (span::class.java == StyleSpan::class.java) {
//                val textView = TextView(holder.linearLayout.context)
//                textView.text = span.toString()
//
//                holder.linearLayout.addView(textView)
//            }
//        }

        holder.messageText.text = message
//        holder.imageView.text = message

    }

    override fun getItemCount() = messages.size

    companion object {
        private val TAG = "MessageAdapter"
    }
}
