package com.example.kakaomaptest_1.Fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.kakaomaptest_1.R
import com.example.kakaomaptest_1.model.User
import com.example.kakaomaptest_1.viewmodel.MNSViewModel
import com.google.android.material.textfield.TextInputLayout

class JoinFragment : Fragment(), View.OnClickListener {
    lateinit var rootView : View
    lateinit var et_id : TextInputLayout
    lateinit var et_passwd : TextInputLayout
    lateinit var et_passwd2 : TextInputLayout
    lateinit var btn_join : Button
    private lateinit var mMNSViewModel : MNSViewModel
    private var userList = emptyList<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_join, container, false)
        mMNSViewModel = ViewModelProvider(this).get(MNSViewModel::class.java)
        mMNSViewModel.readAllUserData.observe(viewLifecycleOwner, Observer { user ->
            this.userList = user
        })

        et_id = rootView.findViewById(R.id.join_layout_edtText1)
        et_passwd = rootView.findViewById(R.id.join_layout_edtText2)
        et_passwd2 = rootView.findViewById(R.id.join_layout_edtText3)
        btn_join = rootView.findViewById<Button>(R.id.join_layout_btnJoin)

        et_id.editText!!.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val id_input = s.toString()

                if(id_input.length > 7) {
                    et_id.error = "아이디는 7자이내로 작성하세요"
                } else if(!id_input.none { it !in 'A'..'Z' && it !in 'a'..'z' && it !in '0'..'9' }) {
                    et_id.error = "숫자, 대소문자만 사용가능합니다"
                } else {
                    et_id.error = null
                }
            }
            override fun afterTextChanged(s: Editable?) {
            }

        })

        et_passwd.editText!!.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val passwd = s.toString()
                val passwd2 = et_passwd2.editText!!.text.toString()

                if(passwd.length > 15) {
                    et_passwd.error = "비밀번호는 15자이내로 작성하세요"
                } else if(passwd.contains(" ")) {
                    et_passwd.error = "공백은 포함하지 않습니다"
                } else {
                    et_passwd.error = null
                }

                if(passwd != passwd2 && passwd2.length != 0) {
                    et_passwd2.error = "일치하지 않습니다"
                } else {
                    et_passwd2.error = null
                }
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })
        et_passwd2.editText!!.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val passwd2 = s.toString()

                if(et_passwd.editText!!.text.toString() != passwd2) {
                    et_passwd2.error = "일치하지 않습니다"
                } else if(et_passwd.editText!!.text.toString() == passwd2) {
                    et_passwd2.error = null
                }
            }
            override fun afterTextChanged(s: Editable?) {
            }

        })

        btn_join.setOnClickListener(this)

        return rootView
    }

    private fun isJoinBtnEnabled(): Boolean {
        val id = et_id.editText!!.text.toString()
        val passwd = et_passwd.editText!!.text.toString()
        val passwd2 = et_passwd2.editText!!.text.toString()

        if(id.length != 0 && et_id.error == null
            && passwd.length != 0 && et_passwd.error == null
            && passwd == passwd2) {
            return true
        }

        return false
    }

    private fun isJoinEnabled(): Boolean {
        for(i in userList) {
            if(et_id.editText!!.text.toString() == i.id.toString()) {
                Toast.makeText(context, "아이디가 이미 존재합니다", Toast.LENGTH_SHORT).show()
                return false
            }
        }
        return true
    }

    private fun addUser() {
        val userId = et_id.editText!!.text.toString().trim()
        val userPasswd = et_passwd.editText!!.text.toString().trim()
        val user = User(userId, userPasswd)
        mMNSViewModel.addUser(user)
    }

    override fun onClick(v: View?) {
        if(isJoinBtnEnabled()) {
            if(isJoinEnabled()) {
                addUser()
                Toast.makeText(context, "회원가입이 완료되었습니다\n다시 로그인해주세요", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_joinFragment_to_loginFragment)
            }
        } else {
            Toast.makeText(context, "입력 양식을 지켜주세요", Toast.LENGTH_SHORT).show()
        }
    }

}