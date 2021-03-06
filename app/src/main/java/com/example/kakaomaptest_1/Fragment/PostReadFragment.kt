package com.example.kakaomaptest_1.Fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kakaomaptest_1.R
import com.example.kakaomaptest_1.model.Chat
import com.example.kakaomaptest_1.viewmodel.MNSViewModel
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.kakaomaptest_1.Activity.MainActivity
import com.example.kakaomaptest_1.model.Post
import com.example.kakaomaptest_1.model.Scrap
import java.util.*


class PostReadFragment: Fragment() {
    private val args by navArgs<PostReadFragmentArgs>()
    private val adapter = PostReadListAdapter()
    lateinit var rootView: View
    lateinit var recyclerView: RecyclerView
    lateinit var mMNSViewModel: MNSViewModel
    private lateinit var imgBtnCreatePost : ImageButton
    private lateinit var callback: OnBackPressedCallback
    private lateinit var btnBack: ImageButton
    private lateinit var post: Post

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        rootView = inflater.inflate(R.layout.fragment_post_read, container, false)
        recyclerView = rootView.findViewById(R.id.listView_post_read)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        mMNSViewModel = ViewModelProvider(this).get(MNSViewModel::class.java)

        val postId: Int
        if(args.postid == -1){
            postId = requireArguments().getInt("key")
        } else {
            (requireActivity() as MainActivity).setDrawerEnabled(false)
            postId = args.postid
        }

        post = mMNSViewModel.getPost(postId)
        val currUser = (requireActivity() as MainActivity).getUserId()
        val user = mMNSViewModel.getUser(post.userCreatorId)
        mMNSViewModel.readAllChatData.observe(viewLifecycleOwner, { logs ->
            adapter.setData(getChatLog(logs, postId))
            adapter.notifyDataSetChanged()
        })
        adapter.setFixedData(post, currUser, user.nickname, user.photoUri)

        // ????????? ????????? ??????
        imgBtnCreatePost = requireActivity().findViewById<ImageButton>(R.id.imgBtn_createPost)
        imgBtnCreatePost.setImageResource(0)

        //?????? ????????? ???????????? ???????????? ????????? ??? ????????? action
        btnBack = (requireActivity() as MainActivity).findViewById(R.id.imgBtn_back)
        btnBack.visibility = VISIBLE
        btnBack.setOnClickListener {
            //adapter????????? scrap????????? ?????????????????? ???????????? true or false ?????? ??? scrap ??????/??????
            if(adapter.getIsScrap()) {
                mMNSViewModel.addScrap(Scrap(currUser, postId))
            } else {
                if(mMNSViewModel.isPostScrapped(currUser, postId)) {
                    mMNSViewModel.deleteSingleScrap(currUser, postId)
                }
            }
            findNavController().navigate(R.id.action_postReadFragment_to_mapFragment)
            btnBack.visibility = View.GONE
        }

        return rootView
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val currUser = (requireActivity() as MainActivity).getUserId()
        val postId = requireArguments().getInt("key")

        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if(adapter.getIsScrap()) {
                    mMNSViewModel.addScrap(Scrap(currUser, postId))
                } else {
                    if(mMNSViewModel.isPostScrapped(currUser, postId)) {
                        mMNSViewModel.deleteSingleScrap(currUser, postId)
                    }
                }
                btnBack.visibility = View.GONE
                findNavController().navigate(R.id.action_postReadFragment_to_mapFragment)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onDetach() {
        super.onDetach()
        callback.remove()
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