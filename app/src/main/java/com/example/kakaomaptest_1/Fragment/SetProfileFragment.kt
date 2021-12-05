package com.example.kakaomaptest_1.Fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.kakaomaptest_1.Activity.MainActivity
import com.example.kakaomaptest_1.R
import com.example.kakaomaptest_1.model.User
import com.example.kakaomaptest_1.viewmodel.MNSViewModel

class SetProfileFragment : Fragment() {
    lateinit var rootView : View
    private lateinit var callback : OnBackPressedCallback
    private lateinit var btnBack : ImageButton
    private lateinit var mMNSViewModel: MNSViewModel
    private lateinit var imgBtn : ImageButton
    private lateinit var editText: EditText
    private lateinit var btn : Button
    private var userImg = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        rootView = inflater.inflate(R.layout.fragment_setprofile, container, false)
        btnBack = (requireActivity() as MainActivity).findViewById<ImageButton>(R.id.imgBtn_back)
        mMNSViewModel = ViewModelProvider(this).get(MNSViewModel::class.java)
        imgBtn = rootView.findViewById(R.id.imgBtn_account)
        editText = rootView.findViewById(R.id.eT_name)
        btn = rootView.findViewById(R.id.btn_dup)
        val currentUser = (requireActivity() as MainActivity).getUserId()
        initUser(currentUser)
        //Toolbar에 뒤로가기 버튼을 생성후 리스너로 이전 Fragment로 돌아가게 한다
        btnBack.setOnClickListener {
            btnBack.visibility = GONE
            findNavController().popBackStack()
        }

        imgBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }

        btn.setOnClickListener {
            val nickname = editText.text.toString().trim()
            if(checkValid(currentUser, nickname)) {
                if(nickname == "") {
                    editText.setText(currentUser)
                    mMNSViewModel.editUser(currentUser, currentUser, userImg)
                } else {
                    mMNSViewModel.editUser(currentUser, nickname, userImg)
                }
                Toast.makeText(context, "업데이트 되었습니다", Toast.LENGTH_SHORT).show()

                val user = mMNSViewModel.getUser(currentUser)
                (requireActivity() as MainActivity).setUserProfile(user)
            }
        }

        return rootView
    }

    fun checkValid(currUser: String, nickname: String): Boolean {
        val currNick = mMNSViewModel.getUser(currUser).nickname

        if(nickname.length > 7) {
            Toast.makeText(context, "닉네임은 7자 이내로 해주세요", Toast.LENGTH_SHORT).show()
            return false
        } else if(currNick == nickname) {
            return true
        } else if(mMNSViewModel.isThisNickExists(nickname)){
            Toast.makeText(context, "닉네임이 겹칩니다", Toast.LENGTH_SHORT).show()
            return false
        } else {
            return true
        }
    }

    fun initUser(userid: String) {
        //userPhoto initialize
        val temp = mMNSViewModel.getUser(userid)

        if(temp.photoUri == "") {
            imgBtn.setImageResource(R.drawable.account)
        } else {
            val uri = Uri.parse(temp.photoUri)
            userImg = temp.photoUri
            Glide.with(this).load(uri).circleCrop().into(imgBtn)
        }

        //userNickname initialize
        editText.setText(temp.nickname)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            val uri: Uri? = data?.data

            if(uri != null) {
                userImg = uri.toString()
            }
            Glide.with(this).load(uri).circleCrop().into(imgBtn)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                btnBack.visibility = GONE
                findNavController().popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }

}