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
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.kakaomaptest_1.R
import com.example.kakaomaptest_1.model.User
import com.example.kakaomaptest_1.viewmodel.MNSViewModel
import com.google.android.material.textfield.TextInputLayout

class JoinFragment : Fragment(), View.OnClickListener {
    lateinit var rootView : View
    lateinit var edtId : TextInputLayout
    lateinit var edtPasswd : TextInputLayout
    lateinit var edtPasswd2 : TextInputLayout
    lateinit var btnJoin : Button
    private lateinit var mMNSViewModel : MNSViewModel
    private var userList = emptyList<User>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_join, container, false)
        mMNSViewModel = ViewModelProvider(this).get(MNSViewModel::class.java)
        mMNSViewModel.readAllUserData.observe(viewLifecycleOwner, { user ->
            this.userList = user
        })

        edtId = rootView.findViewById(R.id.join_layout_edtText1)
        edtPasswd = rootView.findViewById(R.id.join_layout_edtText3)
        edtPasswd2 = rootView.findViewById(R.id.join_layout_edtText4)
        btnJoin = rootView.findViewById(R.id.join_layout_btnJoin)

        edtId.editText!!.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val idInput = s.toString()

                if(idInput.length > 7) {
                    edtId.error = "아이디는 7자이내로 작성하세요"
                } else if(!idInput.none { it !in 'A'..'Z' && it !in 'a'..'z' && it !in '0'..'9' }) {
                    edtId.error = "숫자, 대소문자만 사용가능합니다"
                } else {
                    edtId.error = null
                }
            }
            override fun afterTextChanged(s: Editable?) {
            }

        })

        edtPasswd.editText!!.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val passwd = s.toString()
                val passwd2 = edtPasswd2.editText!!.text.toString()

                if(passwd.length > 15) {
                    edtPasswd.error = "비밀번호는 15자이내로 작성하세요"
                } else if(passwd.contains(" ")) {
                    edtPasswd.error = "공백은 포함하지 않습니다"
                } else {
                    edtPasswd.error = null
                }

                if(passwd != passwd2 && passwd2.isNotEmpty()) {
                    edtPasswd2.error = "일치하지 않습니다"
                } else {
                    edtPasswd2.error = null
                }
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })
        edtPasswd2.editText!!.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val passwd2 = s.toString()

                if(edtPasswd.editText!!.text.toString() != passwd2) {
                    edtPasswd2.error = "일치하지 않습니다"
                } else if(edtPasswd.editText!!.text.toString() == passwd2) {
                    edtPasswd2.error = null
                }
            }
            override fun afterTextChanged(s: Editable?) {
            }

        })

        btnJoin.setOnClickListener(this)

        return rootView
    }

    private fun isJoinBtnEnabled(): Boolean {
        val id = edtId.editText!!.text.toString()
        val passwd = edtPasswd.editText!!.text.toString()
        val passwd2 = edtPasswd2.editText!!.text.toString()

        if(id.isNotEmpty() && edtId.error == null
            && passwd.isNotEmpty() && edtPasswd.error == null
            && passwd == passwd2) {
            return true
        }

        return false
    }

    private fun isJoinEnabled(): Boolean {
        for(i in userList) {
            if(edtId.editText!!.text.toString() == i.id.toString()) {
                Toast.makeText(context, "아이디가 이미 존재합니다", Toast.LENGTH_SHORT).show()
                return false
            }
        }
        return true
    }

    private fun addUser() {
        val userId = edtId.editText!!.text.toString().trim()
        val userPasswd = edtPasswd.editText!!.text.toString().trim()
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