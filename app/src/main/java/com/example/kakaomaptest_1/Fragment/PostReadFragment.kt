package com.example.kakaomaptest_1.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kakaomaptest_1.R
import com.example.kakaomaptest_1.model.Chat
import com.example.kakaomaptest_1.viewmodel.MNSViewModel
import androidx.core.os.bundleOf
import com.example.kakaomaptest_1.Activity.MainActivity
import java.util.*


class PostReadFragment: Fragment() {
    lateinit var rootView: View
    lateinit var recyclerView: RecyclerView
    lateinit var mMNSViewModel: MNSViewModel
    private lateinit var imgBtnCreatePost : ImageButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        rootView = inflater.inflate(R.layout.fragment_post_read, container, false)
        (requireActivity() as MainActivity).setDrawerEnabled(false)
        recyclerView = rootView.findViewById<RecyclerView>(R.id.listView_post_read)
        val adapter = ListAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        mMNSViewModel = ViewModelProvider(this).get(MNSViewModel::class.java)
        val postId = requireArguments().getInt("key")
        mMNSViewModel.readAllChatData.observe(viewLifecycleOwner, { logs ->
            adapter.setData(getChatLog(logs, postId), makeBundle())
            adapter.notifyDataSetChanged()
        })

        // 아이콘 이미지 숨김
        imgBtnCreatePost = requireActivity().findViewById<ImageButton>(R.id.imgBtn_createPost)
        imgBtnCreatePost.setImageResource(0)

        return rootView
    }

    private fun makeBundle(): Bundle {
        val currentUserId = (activity as MainActivity).getUserId()
        val bundle = bundleOf(
            "key" to requireArguments().getInt("key"),
            "userid" to requireArguments().getString("userid"),
            "title" to requireArguments().getString("title"),
            "markerType" to requireArguments().getInt("markerType"),
            "lati" to requireArguments().getDouble("lati"),
            "longi" to requireArguments().getDouble("longi"),
            "uri" to requireArguments().getString("uri"),
            "text" to requireArguments().getString("text"),
            "date" to requireArguments().getString("date"),
            "currUser" to currentUserId
        )
        return bundle
    }

    private fun getChatLog(logs: List<Chat>, id: Int): ArrayList<Chat> {
        val templist = arrayListOf<Chat>()

        for(i in logs) {
            if(i.postId == id) {
                templist.add(i)
            }
        }

        return templist
    }

}