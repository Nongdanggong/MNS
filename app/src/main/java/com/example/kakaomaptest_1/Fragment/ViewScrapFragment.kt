package com.example.kakaomaptest_1.Fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kakaomaptest_1.R
import com.example.kakaomaptest_1.viewmodel.MNSViewModel
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.kakaomaptest_1.Activity.MainActivity
import com.example.kakaomaptest_1.model.Scrap
import java.util.*

class ViewScrapFragment : Fragment() {

    lateinit var rootView: View
    lateinit var recyclerView: RecyclerView
    lateinit var mMNSViewModel: MNSViewModel
    //뒤로가기 처리하는 두개의 방법(툴바 뒤로가기 및 '<' 뒤로가기)
    private lateinit var callback: OnBackPressedCallback
    private lateinit var btnBack: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        rootView = inflater.inflate(R.layout.fragment_view_scrap, container, false)
        recyclerView = rootView.findViewById<RecyclerView>(R.id.listView_post_read)
        btnBack = (requireActivity() as MainActivity).findViewById(R.id.imgBtn_back)
        btnBack.setOnClickListener {
            findNavController().navigate(R.id.action_viewScrapFragment_to_mapFragment)
            btnBack.visibility = View.GONE
        }
        val adapter = ViewScrapListAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        mMNSViewModel = ViewModelProvider(this).get(MNSViewModel::class.java)
        val userId = (requireActivity() as MainActivity).getUserId()
        mMNSViewModel.getScraps(userId).observe(viewLifecycleOwner, { scraps ->
            adapter.setScrapLog(scraps)
            adapter.notifyDataSetChanged()
        })
        return rootView
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                btnBack.visibility = View.GONE
                findNavController().navigate(R.id.action_viewScrapFragment_to_mapFragment)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onDetach() {
        super.onDetach()
        callback.remove()
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

}