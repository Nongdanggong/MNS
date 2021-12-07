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
import com.example.kakaomaptest_1.model.Post
import com.example.kakaomaptest_1.model.Scrap
import com.example.kakaomaptest_1.viewmodel.MNSViewModel
import kotlinx.android.synthetic.main.adapter_pin_row.view.*
import java.text.SimpleDateFormat
import java.util.*

class ViewPinListAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var context: Context
    private var postLog = emptyList<Post>()
    private lateinit var mMNSViewModel: MNSViewModel
    private var pinArray = arrayOf("실시간 상황", "프로모션", "나만의 관광지", "질문", "사진 핫스팟")
    private var pinImg = arrayOf(R.drawable.pinred, R.drawable.pinblue, R.drawable.pingreen, R.drawable.pin, R.drawable.pinyellow)

    class PinViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        mMNSViewModel = ViewModelProvider(context as FragmentActivity).get(MNSViewModel::class.java)
        val baseLayout = LayoutInflater.from(parent.context).inflate(R.layout.adapter_pin_row, parent, false)
        return PinViewHolder(baseLayout)
    }

    override fun getItemCount(): Int {
        return postLog.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentItem = postLog[position]
        val form = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA)
        val uri = Uri.parse(currentItem.photoUri)

        holder.itemView.pin_post_title.text = currentItem.title
        holder.itemView.pin_post_date.text = form.format(currentItem.date)
        if(uri.toString() != "") {
            Glide.with(context).load(uri).into(holder.itemView.pin_post_photo)
        } else {
            holder.itemView.pin_post_photo.visibility = GONE
        }
        holder.itemView.pin_post_text.text = currentItem.text
        holder.itemView.tV_pin_post_pin.text = pinArray[currentItem.pinType]
        holder.itemView.iV_pin_post_pin.setImageResource(pinImg[currentItem.pinType])
        holder.itemView.imgBtn_pin_delete.setOnClickListener {
            val aD = deletePostAlertDialog(currentItem)
            aD.show()
        }

    }

    fun setPostData(postLog: List<Post>) {
        this.postLog = postLog
    }

    fun deletePostAlertDialog(currentItem: Post): AlertDialog {
        val postid = currentItem.key

        val builder = AlertDialog.Builder(context)
            .setTitle("핀 삭제")
            .setMessage("삭제하시겠습니까?")
            .setPositiveButton("아니오"){ dialog, which ->
            }
            .setNegativeButton("네"){ dialog, which ->
                mMNSViewModel.deleteSinglePost(postid)
            }
            .create()

        return builder
    }

}