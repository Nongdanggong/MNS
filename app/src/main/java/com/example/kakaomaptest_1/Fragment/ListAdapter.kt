package com.example.kakaomaptest_1.Fragment

import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.kakaomaptest_1.R
import com.example.kakaomaptest_1.model.Chat
import com.example.kakaomaptest_1.model.Post
import com.example.kakaomaptest_1.viewmodel.MNSViewModel
import kotlinx.android.synthetic.main.chat_row.view.*
import kotlinx.android.synthetic.main.fragment_post_read_footer.view.*
import kotlinx.android.synthetic.main.fragmet_post_read_header.view.*
import java.text.SimpleDateFormat
import java.util.*

class ListAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TYPE_HEADER = 0
    private val TYPE_ITEM = 1
    private val TYPE_FOOTER = 2
    private lateinit var editText: EditText
    private lateinit var sendBtn: ImageButton
    private lateinit var context: Context
    private var bundle = Bundle()
    private var chatLog = emptyList<Chat>()
    private lateinit var mMNSViewModel: MNSViewModel

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        mMNSViewModel = ViewModelProvider(context as FragmentActivity).get(MNSViewModel::class.java)
        return when (viewType) {
            TYPE_HEADER -> HeaderViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.fragmet_post_read_header, parent, false))
            TYPE_FOOTER -> FooterViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.fragment_post_read_footer, parent, false))
            else -> TaskViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.chat_row, parent, false))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> TYPE_HEADER
            itemCount - 1 -> TYPE_FOOTER
            else -> TYPE_ITEM }
    }

    override fun getItemCount(): Int {
        return chatLog.size + 2
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is HeaderViewHolder -> {
                var uri = bundle.getString("uri")
                holder.itemView.post_read_user.setText(bundle.getString("userid"))
                holder.itemView.post_read_title.setText(bundle.getString("title"))
                if(uri != "") {
                    Glide.with(context).load(uri).into(holder.itemView.post_read_photo)
                } else {
                    holder.itemView.post_read_photo.visibility = GONE
                }
                holder.itemView.post_read_text.setText(bundle.getString("text"))
            }

            is FooterViewHolder -> {
                editText = holder.itemView.bottom_comment_edt
                sendBtn = holder.itemView.bottom_comment_sendBtn

                sendBtn.setOnClickListener{
                    if(editText.text.toString().trim() != "") {
                        val postId = bundle.getInt("key")
                        val userId = bundle.getString("currUser")
                        val date = Date()
                        val temp = Chat(postId, date, userId!!, editText.text.toString().trim())
                        mMNSViewModel.addChat(temp)
                        editText.text.clear()
                    }
                }
            }

            is TaskViewHolder -> {
                var currentItem = chatLog[position - 1]
                var form = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

                holder.itemView.comment_user.text = currentItem.userId
                holder.itemView.comment_text.text = currentItem.log
                holder.itemView.comment_date.text = form.format(currentItem.date)
            }
        }
    }

    class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    class FooterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    fun setData(chatLog: ArrayList<Chat>, bundle: Bundle) {
        this.chatLog = chatLog
        this.bundle = bundle
        notifyDataSetChanged()
    }
}
