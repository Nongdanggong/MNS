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
    lateinit var rootView: View
    lateinit var edtId: TextInputLayout
    lateinit var edtPasswd: TextInputLayout
    lateinit var edtPasswd2: TextInputLayout
    lateinit var btnJoin: Button
    private lateinit var mMNSViewModel: MNSViewModel
    private var userList = emptyList<User>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // fragment_join.xml inflate 해줌
        rootView = inflater.inflate(R.layout.fragment_join, container, false)
        // userList에 데이터베이스에 있는 유저정보들 불러와서 저장
        mMNSViewModel = ViewModelProvider(this).get(MNSViewModel::class.java)
        mMNSViewModel.readAllUserData.observe(viewLifecycleOwner, { user ->
            this.userList = user
        })

        edtId = rootView.findViewById(R.id.join_layout_edtText1)
        edtPasswd = rootView.findViewById(R.id.join_layout_edtText3)
        edtPasswd2 = rootView.findViewById(R.id.join_layout_edtText4)
        btnJoin = rootView.findViewById(R.id.join_layout_btnJoin)


        // 아이디에 옳바른 값 입력했는지 체크해주는 부분
        edtId.editText!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val idInput = s.toString()
                if (idInput.length > 7) {
                    edtId.error = "아이디는 7자이내로 작성하세요"
                } else if (!idInput.none { it !in 'A'..'Z' && it !in 'a'..'z' && it !in '0'..'9' }) {
                    edtId.error = "숫자, 대소문자만 사용가능합니다"
                } else {
                    edtId.error = null
                }
            }
            override fun afterTextChanged(s: Editable?) {
            }

        })

        // 비밀번호에 옳바른 값 입력했는지 체크해주는 부분
        edtPasswd.editText!!.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val passwd = s.toString()
                val passwd2 = edtPasswd2.editText!!.text.toString()

                if (passwd.length > 15) {
                    edtPasswd.error = "비밀번호는 15자이내로 작성하세요"
                } else if (passwd.contains(" ")) {
                    edtPasswd.error = "공백은 포함하지 않습니다"
                } else {
                    edtPasswd.error = null
                }

                if (passwd != passwd2 && passwd2.isNotEmpty()) {
                    edtPasswd2.error = "일치하지 않습니다"
                } else {
                    edtPasswd2.error = null
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
        })

        // 비밀번호와 비밀번호 확인이 일치하는지 체크하는 부분
        edtPasswd2.editText!!.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val passwd2 = s.toString()

                if (edtPasswd.editText!!.text.toString() != passwd2) {
                    edtPasswd2.error = "일치하지 않습니다"
                } else if (edtPasswd.editText!!.text.toString() == passwd2) {
                    edtPasswd2.error = null
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
        })

        // ?? 근데 왜 버튼에 바로 ClickListener 안 달고 이렇게 달지?
        btnJoin.setOnClickListener(this)
        return rootView
    }


    // 사용자가 회원가입 form을 형식에 맞게 작성했는지 체크
    private fun isJoinEnabled(): Boolean {
        val id = edtId.editText!!.text.toString()
        val passwd = edtPasswd.editText!!.text.toString()
        val passwd2 = edtPasswd2.editText!!.text.toString()

        // 사용자가 입력한 id 값이 옳바른 값인지 체크
        if (!(id.isNotEmpty() && edtId.error == null)) {
            Toast.makeText(context, "ID 형식을 확인하세요", Toast.LENGTH_SHORT).show()
            return false
        }

        // 사용자가 입력한 비밀번호 값이 옳바른 값인지 체크
        if (passwd.isNotEmpty() && edtPasswd.error == null) {
            if (passwd != passwd2) {
                Toast.makeText(context, "비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show()
                return false
            }
        } else {
            Toast.makeText(context, "비밀번호 형식을 확인하세요", Toast.LENGTH_SHORT).show()
            return false
        }

        // 사용자가 입력한 아이디가 존재하는지 체크
        // ?? 쿼리문으로 사용자 존재하는지 체크할 수 는 없나?
        for (i in userList) {
            if (edtId.editText!!.text.toString() == i.id.toString()) {
                Toast.makeText(context, "이미 존재하는 아이디 입니다", Toast.LENGTH_SHORT).show()
                return false
            }
        }
        return true
    }

    private fun addUser() {
        val userId = edtId.editText!!.text.toString().trim()
        val userPasswd = edtPasswd.editText!!.text.toString().trim()
        val user = User(userId, userPasswd,"","")
        mMNSViewModel.addUser(user)
    }

    // 이 View에서 onClick 이벤트 발생시
    override fun onClick(v: View?) {
        if (isJoinEnabled()) {
            addUser()
            Toast.makeText(context, "회원가입이 완료되었습니다", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_joinFragment_to_loginFragment)
        }
    }
}