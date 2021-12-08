package com.example.kakaomaptest_1.Fragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kakaomaptest_1.R
import com.example.kakaomaptest_1.model.Post
import kotlinx.android.synthetic.main.near_places.view.*

// Adapter 생성시 매개변수로 dataSet을 전달 받음 ( 주변위치 마커들 리스트 )
class NearPinAdapter :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var context: Context
    private var dataSet = emptyList<Post>()

    class itemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    // 최초 로딩할 때 View가 없는 경우의 레이아웃
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): itemViewHolder {
        context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.near_places, parent, false)
        return itemViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentItem = dataSet[position]
        holder.itemView.near_pin_title.text = currentItem.title

        when (currentItem.pinType) {
            0 -> holder.itemView.near_pin_category.text = "실시간 상황"
            1 -> holder.itemView.near_pin_category.text = "프로모션"
            2 -> holder.itemView.near_pin_category.text = "나만의 관광지"
            3 -> holder.itemView.near_pin_category.text = "질문"
            4 -> holder.itemView.near_pin_category.text = "사진 핫스팟"
            else -> holder.itemView.near_pin_category.text = "카테고리 없음"
        }

        val photoUri = currentItem.photoUri
        if (photoUri != "") {
            val img = Uri.parse(photoUri)
            Glide.with(context).load(img).into(holder.itemView.near_pin_img)
        }

        holder.itemView.near_pin_row.setOnClickListener {
            val action = MapFragmentDirections.actionMapFragmentToPostReadFragment(currentItem.key)
            holder.itemView.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int = dataSet.size

    fun setData(data : List<Post>) {
        this.dataSet = data
    }

}