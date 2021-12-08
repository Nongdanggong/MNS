package com.example.kakaomaptest_1.Fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kakaomaptest_1.Activity.MainActivity
import com.example.kakaomaptest_1.R
import com.example.kakaomaptest_1.viewmodel.MNSViewModel
import com.example.kakaomaptest_1.model.Post
import java.util.*

class ViewPinFragment : Fragment() {

    lateinit var rootView: View
    lateinit var recyclerView: RecyclerView
    lateinit var mMNSViewModel: MNSViewModel
    private lateinit var callback: OnBackPressedCallback
    private lateinit var btnBack: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        rootView = inflater.inflate(R.layout.fragment_view_pin, container, false)
        recyclerView = rootView.findViewById<RecyclerView>(R.id.listView_post_read)
        btnBack = (requireActivity() as MainActivity).findViewById(R.id.imgBtn_back)
        btnBack.setOnClickListener {
            findNavController().navigate(R.id.action_viewPinFragment_to_mapFragment)
            btnBack.visibility = GONE
        }
        val adapter = ViewPinListAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        mMNSViewModel = ViewModelProvider(this).get(MNSViewModel::class.java)
        val userId = (requireActivity() as MainActivity).getUserId()
        mMNSViewModel.getUserPosts(userId).observe(viewLifecycleOwner, { logs ->
          adapter.setPostData(logs)
          adapter.notifyDataSetChanged()
        })
        return rootView
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                btnBack.visibility = GONE
                findNavController().navigate(R.id.action_viewPinFragment_to_mapFragment)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }

    private fun getPostLog(logs: List<Post>, id: String): ArrayList<Post> {
        val templist = arrayListOf<Post>()

        for(i in logs) {
            if(i.userCreatorId == id) {
                templist.add(i)
            }
        }

        return templist
    }


}