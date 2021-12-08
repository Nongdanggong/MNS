package com.example.kakaomaptest_1.Fragment

import android.app.AlertDialog
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kakaomaptest_1.R
import com.example.kakaomaptest_1.model.Chat
import com.example.kakaomaptest_1.model.Post
import com.example.kakaomaptest_1.model.Scrap
import com.example.kakaomaptest_1.viewmodel.MNSViewModel
import kotlinx.android.synthetic.main.adapter_chat_row.view.*
import kotlinx.android.synthetic.main.fragment_post_read_footer.view.*
import kotlinx.android.synthetic.main.fragmet_post_read_header.view.*
import java.text.SimpleDateFormat
import java.util.*


class PostReadListAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TYPE_HEADER = 0
    private val TYPE_ITEM = 1
    private val TYPE_FOOTER = 2
    private lateinit var editText: EditText
    private lateinit var sendBtn: ImageButton
    private lateinit var context: Context
    private lateinit var imgbtn_scrap : ImageButton
    private lateinit var imgbtn_delete : ImageButton
    private lateinit var text_scrap : TextView
    private var chatLog = emptyList<Chat>()
    private lateinit var mMNSViewModel: MNSViewModel
    private var pinArray = arrayOf("실시간 상황", "프로모션", "나만의 관광지", "질문", "사진 핫스팟")
    private var pinImg = arrayOf(R.drawable.pinred, R.drawable.pinblue, R.drawable.pingreen, R.drawable.pin, R.drawable.pinyellow)
    private var isScrap = false
    private lateinit var currUser: String
    private lateinit var postNickname: String
    private lateinit var postUserImg: String
    private lateinit var post: Post

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        mMNSViewModel = ViewModelProvider(context as FragmentActivity).get(MNSViewModel::class.java)
        return when (viewType) {
            TYPE_HEADER -> HeaderViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.fragmet_post_read_header, parent, false))
            TYPE_FOOTER -> FooterViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.fragment_post_read_footer, parent, false))
            else -> TaskViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.adapter_chat_row, parent, false))
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
                holder.itemView.post_read_user.text = postNickname
                holder.itemView.post_read_title.text = post.title

                //유저 프로필 사진
                if(postUserImg != "") {
                    val uri = Uri.parse(postUserImg)
                    Glide.with(context).load(uri).circleCrop().into(holder.itemView.post_read_userImg)
                } else {
                    holder.itemView.post_read_userImg.setImageResource(R.drawable.account)
                }
                //본문사진
                val uri = post.photoUri
                if(uri != "") {
                    Glide.with(context).load(uri).into(holder.itemView.post_read_photo)
                } else {
                    holder.itemView.post_read_photo.visibility = GONE
                }
                //작성자 이름 밑에 현재 경과시간 보여줌
                val date_b = post.date
                val today = Calendar.getInstance()
                //p_date = form_2.parse(date_b.toString())
                var calcuDate = (today.time.time - date_b.time) / (60 * 60 * 1000)

                // 1시간 이내
                if (calcuDate == 0L) {
                    calcuDate = (today.time.time - date_b.time) / (60 * 1000)
                    holder.itemView.post_read_date.text = calcuDate.toString() + " minutes ago"
                }
                // 1시간
                else if (calcuDate == 1L) {
                    holder.itemView.post_read_date.text = calcuDate.toString() + " hour ago"
                }
                // 하루 이상
                else if (calcuDate > 24L) {
                    calcuDate = (today.time.time - date_b.time) / (60 * 60 * 24 * 1000)
                    holder.itemView.post_read_date.text = calcuDate.toString() + " days ago"
                }
                // 1시간 ~ 하루 사이
                else {
                    holder.itemView.post_read_date.text = calcuDate.toString() + " hours ago"
                }

                holder.itemView.post_read_text.text = post.text
                val pinType = post.pinType
                holder.itemView.tV_post_read_pin.text = pinArray[pinType]
                holder.itemView.iV_post_read_pin.setImageResource(pinImg[pinType])

                imgbtn_scrap = holder.itemView.imgbtn_scrap

                if(post.userCreatorId == currUser) {
                    imgbtn_scrap.visibility = GONE
                }

                isScrap = mMNSViewModel.isPostScrapped(currUser, post.key)
                if(isScrap) {
                    imgbtn_scrap.setImageResource(R.drawable.bookmark_g)
                }

                imgbtn_scrap.setOnClickListener {
                    if(!isScrap) {
                        isScrap = true
                        imgbtn_scrap.setImageResource(R.drawable.bookmark_g)
                    } else {
                        isScrap = false
                        imgbtn_scrap.setImageResource(R.drawable.bookmark_b)
                    }
                }

                imgbtn_delete = holder.itemView.imgbtn_delete
                if(post.userCreatorId == currUser) {
                    imgbtn_delete.visibility = VISIBLE
                } else {
                    imgbtn_delete.visibility = GONE
                }

                imgbtn_delete.setOnClickListener{
                    val aD = deletePostAlertDialog(post.key, holder)
                    aD.show()
                }
            }

            is FooterViewHolder -> {
                editText = holder.itemView.bottom_comment_edt
                sendBtn = holder.itemView.bottom_comment_sendBtn
                val imm = context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                sendBtn.setOnClickListener{
                    if(editText.text.toString().trim() != "") {
                        imm.hideSoftInputFromWindow(holder.itemView.windowToken, 0)
                        val postId = post.key
                        val userId = currUser
                        val date = Date()
                        val temp = Chat(postId, date, userId, editText.text.toString().trim())
                        mMNSViewModel.addChat(temp)
                        editText.text.clear()
                    }
                }
            }

            is TaskViewHolder -> {
                val currentItem = chatLog[position - 1]
                val form = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA)
                val tempUser = mMNSViewModel.getUser(currentItem.userId)
                //댓글유저들 사진 달기
                if(tempUser.photoUri != "") {
                    val uri = Uri.parse(tempUser.photoUri)
                    Glide.with(context).load(uri).circleCrop().into(holder.itemView.comment_img)
                } else {
                    holder.itemView.comment_img.setImageResource(R.drawable.account)
                }
                holder.itemView.comment_user.text = tempUser.nickname
                holder.itemView.comment_text.text = currentItem.log
                holder.itemView.comment_date.text = form.format(currentItem.date)
                val deleteBtn = holder.itemView.btn_delete

                if(currentItem.userId == currUser) {
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

    fun setData(chatLog: ArrayList<Chat>) {
        this.chatLog = chatLog
    }

    fun setFixedData(post: Post, currUser: String, nickname: String, userImg: String) {
        this.post = post
        this.currUser = currUser
        this.postNickname = nickname
        this.postUserImg = userImg
    }

    fun deleteChatAlertDialog(currentItem: Chat): AlertDialog {
        val postid = currentItem.postId
        val userid = currentItem.userId
        val date = currentItem.date

        val builder = AlertDialog.Builder(context)
            .setTitle("채팅 삭제")
            .setMessage("삭제하시겠습니까?")
            .setPositiveButton("아니오"){ dialog, which ->
            }
            .setNegativeButton("네"){ dialog, which ->
                mMNSViewModel.deleteSingleChat(postid, date, userid)
            }
            .create()

        return builder
    }

    fun deletePostAlertDialog(postId: Int, holder: RecyclerView.ViewHolder): AlertDialog {
        val action = PostReadFragmentDirections.actionPostReadFragmentToMapFragment()
        val builder = AlertDialog.Builder(context)
            .setTitle("글 삭제")
            .setMessage("삭제하시겠습니까?")
            .setPositiveButton("아니오"){ dialog, which ->
            }
            .setNegativeButton("네"){ dialog, which ->
                mMNSViewModel.deleteSinglePost(postId)
                mMNSViewModel.deletePostChatLog(postId)
                mMNSViewModel.deleteConnectedScrap(postId)
                holder.itemView.findNavController().navigate(action)
            }
            .create()

        return builder
    }

    fun getIsScrap(): Boolean {
        return this.isScrap
    }
}
