package com.example.kakaomaptest_1.Fragment

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kakaomaptest_1.R
import com.example.kakaomaptest_1.model.Chat
import com.example.kakaomaptest_1.viewmodel.MNSViewModel
import kotlinx.android.synthetic.main.chat_row.view.*
import kotlinx.android.synthetic.main.fragment_post_create.view.*
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
    private lateinit var imgbtn_heart : ImageButton
    private lateinit var imgbtn_scrap : ImageButton
    private lateinit var img_pin : ImageView
    private lateinit var img_delete : ImageButton
    private lateinit var post_hour : TextView

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
                val uri = bundle.getString("uri")
                // 밑 두줄처럼 선언 시 java.lang.NullPointerException 에러 발생
                //val creatorid = bundle.getString("userid")!!
                //val title = bundle.getString("title")!!
                holder.itemView.post_read_user.text = bundle.getString("userid")
                holder.itemView.post_read_title.text = bundle.getString("title")
                val creatorid = holder.itemView.post_read_user.text.toString()
                val title = holder.itemView.post_read_title.text.toString()
                imgbtn_scrap = holder.itemView.imgbtn_scrap
                img_pin = holder.itemView.img_pin
                img_delete = holder.itemView.imgbtn_delete
                post_hour = holder.itemView.post_hour

                var colorArray = arrayOf(R.drawable.pinred, R.drawable.pinblue, R.drawable.pingreen, R.drawable.pin, R.drawable.pinyellow)

                if(uri != "") {
                    Glide.with(context).load(uri).into(holder.itemView.post_read_photo)
                } else {
                    holder.itemView.post_read_photo.visibility = GONE
                }
                holder.itemView.post_read_text.text = bundle.getString("text")

                // 알맞은 핀 색상 보여줌
                img_pin.setImageResource(colorArray[bundle.getInt("markerType")])

                // 핀 생성된 후 경과시간 보여주기
                var today = Calendar.getInstance()
                var p_date : Date
                var calcuDate : Long
                val form_2 = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA)

                var date_b = bundle.get("date")

                if(date_b != null) {
                    //Toast.makeText(context, date_b.toString(), Toast.LENGTH_SHORT).show()
                    p_date = form_2.parse(date_b.toString())

                    calcuDate = (today.time.time - p_date.time) / (60 * 60 * 1000)

                    // 1시간 이내
                    if (calcuDate == 0L) {
                        calcuDate = (today.time.time - p_date.time) / (60 * 1000)
                        post_hour.text = calcuDate.toString() + " minutes ago"
                    }
                    // 1시간
                    else if (calcuDate == 1L) {
                        post_hour.text = calcuDate.toString() + " hour ago"
                    }
                    // 하루 이상
                    else if (calcuDate > 24L) {
                        calcuDate = (today.time.time - p_date.time) / (60 * 60 * 24 * 1000)
                        post_hour.text = calcuDate.toString() + " days ago"
                    }
                    // 1시간 ~ 하루 사이
                    else {
                        post_hour.text = calcuDate.toString() + " hours ago"
                    }
                }
                var j = true
                // 스크랩 정보 저장하는 코드 필요
                imgbtn_scrap.setOnClickListener {
                    j = if(j){
                        imgbtn_scrap.setImageResource(R.drawable.bookmark_g)
                        false
                    } else {
                        imgbtn_scrap.setImageResource(R.drawable.bookmark_b)
                        true
                    }
                }

                if(creatorid == bundle.getString("currUser")) {
                    img_delete.visibility = VISIBLE
                } else {
                    img_delete.visibility = GONE
                }

                img_delete.setOnClickListener{
                    val aD = deletePostAlertDialog(creatorid, title, holder)
                    aD.show()
                }

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
                val currentItem = chatLog[position - 1]
                val form = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA)

                //Toast.makeText(context, currentItem.date.toString(), Toast.LENGTH_SHORT).show()
                holder.itemView.comment_user.text = currentItem.userId
                holder.itemView.comment_text.text = currentItem.log
                holder.itemView.comment_date.text = form.format(currentItem.date)
                val deleteBtn = holder.itemView.btn_delete

                if(currentItem.userId == bundle.getString("currUser")) {
                    deleteBtn.visibility = VISIBLE
                } else {
                    deleteBtn.visibility = GONE
                }
                deleteBtn.setOnClickListener{
                    val aD = deleteChatAlertDialog(currentItem)
                    aD.show()
                }
            }
        }
    }

    class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    class FooterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    fun setData(chatLog: ArrayList<Chat>, bundle: Bundle) {
        this.chatLog = chatLog
        this.bundle = bundle
    }

    // 댓글 삭제 fun
    fun deleteChatAlertDialog(currentItem: Chat): AlertDialog {
        val postid = currentItem.postId
        val userid = currentItem.userId
        val date = currentItem.date

        val builder = AlertDialog.Builder(context)
            .setTitle("댓글 삭제")
            .setMessage("삭제하시겠습니까?")
            .setPositiveButton("아니오"){ dialog, which ->
            }
            .setNegativeButton("네"){ dialog, which ->
                mMNSViewModel.deleteSingleChat(postid, date, userid)
            }
            .create()

        return builder
    }

    // 핀 삭제 fun
    fun deletePostAlertDialog(creatorId : String, title : String, holder: RecyclerView.ViewHolder): AlertDialog {

        val action = PostReadFragmentDirections.actionPostReadFragmentToMapFragment()
        val builder = AlertDialog.Builder(context)
            .setTitle("글 삭제")
            .setMessage("삭제하시겠습니까?")
            .setPositiveButton("아니오"){ dialog, which ->
            }
            .setNegativeButton("네"){ dialog, which ->
                mMNSViewModel.deleteSinglePost(creatorId, title)
                holder.itemView.findNavController().navigate(action)
            }
            .create()

        return builder
    }
}
