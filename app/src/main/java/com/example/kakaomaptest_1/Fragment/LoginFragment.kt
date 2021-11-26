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
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.kakaomaptest_1.Activity.MainActivity
import com.example.kakaomaptest_1.R
import com.example.kakaomaptest_1.model.User
import com.example.kakaomaptest_1.viewmodel.MNSViewModel

class LoginFragment : Fragment(), View.OnClickListener{
    // TODO: Rename and change types of parameters
    private lateinit var rootView : View
    private lateinit var edtTxtId : EditText
    private lateinit var edtTxtPasswd : EditText
    private lateinit var btnLogin : Button
    private lateinit var btnJoin : Button
    private lateinit var mMNSViewModel: MNSViewModel
    private var userList = emptyList<User>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
        rootView = inflater.inflate(R.layout.fragment_login, container, false)
        mMNSViewModel = ViewModelProvider(this).get(MNSViewModel::class.java)
        mMNSViewModel.readAllUserData.observe(viewLifecycleOwner, { user ->
            this.userList = user
        })

        edtTxtId = rootView.findViewById(R.id.login_layout_edtText1)
        edtTxtPasswd = rootView.findViewById(R.id.login_layout_edtText2)
        btnLogin = rootView.findViewById(R.id.login_layout_btnLogin)
        btnJoin = rootView.findViewById(R.id.login_layout_btnJoin)

        btnLogin.setOnClickListener(this)
        btnJoin.setOnClickListener(this)

        return rootView
    }

    //로그인 버튼을 눌렀을
    override fun onClick(v: View?) {
        val id = edtTxtId.text.toString().trim()

        when(v!!.id) {
            R.id.login_layout_btnLogin ->
                //아이디와 비밀번호가 일치한다면 findNavController로 loginfragment에서 mapfragment로 이동
                if(checkValid()) {
                    (activity as MainActivity)!!.setUserId(id)
                    Toast.makeText(context, "로그인되었습니다", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_loginFragment_to_mapFragment)
                }
                //회원가입 버튼을 눌렀을때 findNavController로 loginfragment에서 joinfragment로 이동
            R.id.login_layout_btnJoin ->
                findNavController().navigate(R.id.action_loginFragment_to_joinFragment)

        }
    }

    //입력한 항목이 데이터베이스와 일치하는지 검사
    private fun checkValid() : Boolean {
        val edtId = edtTxtId.text.toString().trim()
        val edtPasswd = edtTxtPasswd.text.toString().trim()

        for(i in userList) {
            if(i.id == edtId){
                return if(i.password == edtPasswd) {
                    true
                } else {
                    Toast.makeText(activity, "비밀번호가 맞지 않습니다", Toast.LENGTH_SHORT).show()
                    false
                }
            }
        }

        Toast.makeText(activity, "존재하지 않는 아이디입니다.", Toast.LENGTH_SHORT).show()

        return false
    }
}

