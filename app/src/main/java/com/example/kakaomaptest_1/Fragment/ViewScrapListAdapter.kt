package com.example.kakaomaptest_1.Fragment

import android.app.AlertDialog
import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kakaomaptest_1.R
import com.example.kakaomaptest_1.model.Chat
import com.example.kakaomaptest_1.model.Scrap
import com.example.kakaomaptest_1.model.User
import com.example.kakaomaptest_1.viewmodel.MNSViewModel
import kotlinx.android.synthetic.main.adapter_chat_row.view.*
import kotlinx.android.synthetic.main.fragment_post_read_footer.view.*
import kotlinx.android.synthetic.main.fragmet_post_read_header.view.*
import kotlinx.android.synthetic.main.fragment_view_scrap_adapter.view.*
import java.text.SimpleDateFormat
import java.util.*

class ViewScrapListAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var context: Context
    private var scrapLog = emptyList<Scrap>()
    private lateinit var mMNSViewModel: MNSViewModel
    private var dateForm = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA)
    private var pinArray = arrayOf("실시간 상황", "프로모션", "나만의 관광지", "질문", "사진 핫스팟")
    private var pinImg = arrayOf(R.drawable.pinred, R.drawable.pinblue, R.drawable.pingreen, R.drawable.pin, R.drawable.pinyellow)

    class pinViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        mMNSViewModel = ViewModelProvider(context as FragmentActivity).get(MNSViewModel::class.java)
        val baseLayout = LayoutInflater.from(parent.context).inflate(R.layout.fragment_view_scrap_adapter, parent, false)
        return pinViewHolder(baseLayout)
    }

    override fun getItemCount(): Int {
        return scrapLog.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentItem = scrapLog[position]
        val postWithUser = mMNSViewModel.getPostWithUser(currentItem.key)
        val post = postWithUser.post
        val postCreator = postWithUser.user
        val uriPhoto = Uri.parse(post.photoUri)
        val uriUser = Uri.parse(postCreator.photoUri)
        val pinType = post.pinType

        if(uriUser.toString() != "") {
            Glide.with(context).load(uriUser).circleCrop().into(holder.itemView.iV_scrap_userImg)
        }
        holder.itemView.tV_scrap_user.text = postCreator.nickname
        holder.itemView.tV_scrap_title.text = post.title
        holder.itemView.tV_scrap_date.text = dateForm.format(post.date)
        holder.itemView.tV_scrap_pin.text = pinArray[pinType]
        holder.itemView.iV_scrap_pin.setImageResource(pinImg[pinType])

        if(uriPhoto.toString() != "") {
            Glide.with(context).load(uriPhoto).into(holder.itemView.iV_scrap_photo)
        } else {
            holder.itemView.iV_scrap_photo.visibility = GONE
        }
        holder.itemView.tV_scrap_text.text = post.text

        holder.itemView.imgBtn_scrap_delete.setOnClickListener {
            val aD = deleteScrapAlertDialog(currentItem)
            aD.show()
        }

    }

    fun setScrapLog(scraps: List<Scrap>) {
        this.scrapLog = scraps
    }

    fun deleteScrapAlertDialog(currentItem: Scrap): AlertDialog {
        val postid = currentItem.key
        val userid = currentItem.scraper

        val builder = AlertDialog.Builder(context)
            .setTitle("스크랩 취소")
            .setMessage("취소하시겠습니까?")
            .setPositiveButton("아니오"){ dialog, which ->
            }
            .setNegativeButton("네"){ dialog, which ->
                mMNSViewModel.deleteSingleScrap(userid, postid)
            }
            .create()

        return builder
    }

}