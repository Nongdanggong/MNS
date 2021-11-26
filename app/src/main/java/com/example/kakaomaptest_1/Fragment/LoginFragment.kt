package com.example.kakaomaptest_1.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.kakaomaptest_1.Activity.MainActivity
import com.example.kakaomaptest_1.R
import com.example.kakaomaptest_1.model.User
import com.example.kakaomaptest_1.viewmodel.MNSViewModel

class LoginFragment : Fragment(), View.OnClickListener{
    // TODO: Rename and change types of parameters
    lateinit var rootView : View
    lateinit var et_id : EditText
    lateinit var et_passwd : EditText
    lateinit var btn_login : Button
    lateinit var btn_join : Button
    private lateinit var mMNSViewModel: MNSViewModel
    private var userList = emptyList<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    public fun newInstance(param1: String, param2: String): LoginFragment {
        val args = Bundle()
        val fragment = LoginFragment()
        fragment.arguments = args
        return fragment
    }

    public override fun onResume() {
        super.onResume()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
        rootView = inflater.inflate(R.layout.fragment_login, container, false)
        mMNSViewModel = ViewModelProvider(this).get(MNSViewModel::class.java)
        mMNSViewModel.readAllUserData.observe(viewLifecycleOwner, Observer { user ->
            this.userList = user
        })

        et_id = rootView.findViewById(R.id.login_layout_edtText1)
        et_passwd = rootView.findViewById(R.id.login_layout_edtText2)
        btn_login = rootView.findViewById(R.id.login_layout_btnLogin)
        btn_join = rootView.findViewById(R.id.login_layout_btnJoin)

        btn_login.setOnClickListener(this)
        btn_join.setOnClickListener(this)

        return rootView
    }

    override fun onClick(v: View?) {
        val id = et_id.text.toString().trim()

//        findNavController().navigate(R.id.action_loginFragment_to_mapFragment)

        when(v!!.id) {
            R.id.login_layout_btnLogin ->
                if(checkValid()) {
                    (activity as MainActivity)!!.setUserId(id)
                    Toast.makeText(context, "로그인되었습니다", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_loginFragment_to_mapFragment)
                }

            R.id.login_layout_btnJoin ->
                findNavController().navigate(R.id.action_loginFragment_to_joinFragment)

        }
    }

    private fun checkValid() : Boolean {
        val et_id = et_id.text.toString().trim()
        val et_passwd = et_passwd.text.toString().trim()

        for(i in userList) {
            if(i.id == et_id){
                if(i.password == et_passwd) {
                    return true
                } else {
                    Toast.makeText(activity, "비밀번호가 맞지 않습니다", Toast.LENGTH_SHORT).show()
                    return false
                }
            }
        }

        Toast.makeText(activity, "존재하지 않는 아이디입니다.", Toast.LENGTH_SHORT).show()

        return false
    }
}

