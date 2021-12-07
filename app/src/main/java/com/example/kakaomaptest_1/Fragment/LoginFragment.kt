package com.example.kakaomaptest_1.Fragment

import android.content.Context
import android.os.Bundle
import android.provider.ContactsContract
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.kakaomaptest_1.Activity.MainActivity
import com.example.kakaomaptest_1.R
import com.example.kakaomaptest_1.data.MNSDatabase
import com.example.kakaomaptest_1.model.User
import com.example.kakaomaptest_1.repository.MNSRepository
import com.example.kakaomaptest_1.viewmodel.MNSViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginFragment : Fragment(), View.OnClickListener {
    // TODO: Rename and change types of parameters
    private lateinit var rootView: View
    private lateinit var edtTxtId: EditText
    private lateinit var edtTxtPasswd: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnJoin: Button
    private lateinit var mMNSViewModel: MNSViewModel
    private lateinit var userNickname: String
    private lateinit var callback : OnBackPressedCallback

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //Map으로 이동하기 전 LoginFragment와 JoinFragment에서는 ActionBar를 숨긴다.
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
        //Map으로 이동하기 전 LoginFragment와 JoinFragment에서는 옆에서 나오는 drawer 금지!
        (requireActivity() as DrawerLocker).setDrawerEnabled(false)
        // fragment_login.xml inflate하기
        rootView = inflater.inflate(R.layout.fragment_login, container, false)

        // mMNSViewModel을 통해서 Database에 있는 User정보들 다 불러오기 -> 아이디 체크하기 위해
        mMNSViewModel = ViewModelProvider(this).get(MNSViewModel::class.java)

        edtTxtId = rootView.findViewById(R.id.login_layout_edtText1)
        edtTxtPasswd = rootView.findViewById(R.id.login_layout_edtText2)
        btnLogin = rootView.findViewById(R.id.login_layout_btnLogin)
        btnJoin = rootView.findViewById(R.id.login_layout_btnJoin)

        btnLogin.setOnClickListener(this)
        btnJoin.setOnClickListener(this)

        return rootView
    }

    // rootView에서 onClick 이벤트 발생시 코드
    override fun onClick(v: View?) {
        // 사용자가 입력한 ID
        val id = edtTxtId.text.toString().trim()

        when (v!!.id) {
            // 로그인 버튼을 클릭한 경우
            R.id.login_layout_btnLogin ->
                //아이디와 비밀번호가 일치한다면 fragment_map.xml(홈 화면)으로 전환
                if (checkValid()) {
                    (activity as MainActivity)!!.setUserId(id)
                    val user = mMNSViewModel.getUser(id)
                    (activity as MainActivity).setUserProfile(user)
                    Toast.makeText(context, "로그인되었습니다", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_loginFragment_to_mapFragment)
                }
            // 회원가입 버튼을 클릭한 경우
            R.id.login_layout_btnJoin ->
                // fragment_join.xml(회원가입 화면)으로 전환
                findNavController().navigate(R.id.action_loginFragment_to_joinFragment)
        }
    }

    // 사용자가 입력한 로그인 정보가 데이터베이스에 있는지 검사
    // ?? userlist를 checkValid()안에서 불러오면 안되나? 
    private fun checkValid(): Boolean {
        val edtId = edtTxtId.text.toString().trim()
        val edtPasswd = edtTxtPasswd.text.toString().trim()

        if(edtId == "") {
            Toast.makeText(activity, "아이디를 입력하세요", Toast.LENGTH_SHORT).show()
            return false
        } else if(edtPasswd == "") {
            Toast.makeText(activity, "비밀번를 입력하세요", Toast.LENGTH_SHORT).show()
            return false
        }

        // 데이터 베이스에 쿼리를 날려 유저 확인
        var user = mMNSViewModel.getUser(edtId)

        if (user != null) {
            return if (user.password == edtPasswd) {
                userNickname = user.nickname
                true
            } else {
                Toast.makeText(activity, "비밀번호가 맞지 않습니다", Toast.LENGTH_SHORT).show()
                false
            }
        }

        Toast.makeText(activity, "존재하지 않는 아이디입니다.", Toast.LENGTH_SHORT).show()

        return false
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                (requireActivity() as MainActivity).finish()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }
}


