package com.example.kakaomaptest_1.Fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.kakaomaptest_1.Activity.MainActivity
import com.example.kakaomaptest_1.R
import com.example.kakaomaptest_1.model.Post
import com.example.kakaomaptest_1.viewmodel.MNSViewModel
import java.util.*

class PostCreateFragment : Fragment() {
    lateinit var rootView: View
    lateinit var userPhoto: ImageView
    private lateinit var imgBtn_createPost : ImageButton
    private lateinit var currentImgUri: String
    private lateinit var imgBtn_photoAdd: ImageButton
    private lateinit var imgBtn_photoDelete: ImageButton
    private lateinit var eText_post: EditText
    private lateinit var eText_post_title: EditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_post_create, container, false)
        currentImgUri = ""
        imgBtn_photoAdd = rootView.findViewById<ImageButton>(R.id.addPhoto)
        imgBtn_photoDelete = rootView.findViewById<ImageButton>(R.id.deletePhoto)
        eText_post = rootView.findViewById(R.id.eText_post)
        userPhoto = rootView.findViewById(R.id.userPhoto)
        eText_post_title = rootView.findViewById(R.id.eText_post_title)
        var pinbtn = rootView.findViewById<ImageButton>(R.id.pin)
        var pinArray = arrayOf("실시간 상황", "프로모션", "나만의 관광지", "질문", "사진 핫스팟")
        var pinColor = rootView.resources.getIntArray(R.array.pinColors)
        var markerType = 0
        imgBtn_createPost = requireActivity().findViewById(R.id.imgBtn_createPost)

        imgBtn_createPost.setOnClickListener{
            if(eText_post_title.text.toString().trim() == "") {
                Toast.makeText(context, "제목을 입력하세요", Toast.LENGTH_SHORT).show()
            } else if(eText_post.text.toString().trim() == "") {
                if(currentImgUri == "") {
                    Toast.makeText(context, "그림을 선택하세요", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "내용을 입력하세요", Toast.LENGTH_SHORT).show()
                }
            } else {
                val post = makePost(markerType)
                insertPost(post)
                findNavController().navigate(R.id.action_postCreateFragment_to_mapFragment)
            }
        }

        pinbtn.setOnClickListener {
            var dlg = AlertDialog.Builder(context)
            dlg.setTitle("핀 카테고리 선택")
            dlg.setItems(pinArray){dialog, which->
                pinbtn.setColorFilter(pinColor[which])
                markerType = which
            }
            dlg.setIcon(R.drawable.ic_pin)
            dlg.show()
        }

        // + 버튼 클릭했을 때 갤러리 열어 이미지 파일 들고오기
        imgBtn_photoAdd.setOnClickListener {
            var intent = Intent(Intent.ACTION_PICK)
            intent.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }

        imgBtn_photoDelete.setOnClickListener {
            currentImgUri = ""
            userPhoto.setImageDrawable(null)
            imgBtn_photoDelete.visibility = GONE
            imgBtn_photoAdd.visibility = VISIBLE
        }

        return rootView
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            var uri: Uri? = data?.data

            if(uri != null) {
                currentImgUri = uri.toString()
                imgBtn_photoAdd.visibility = GONE
                imgBtn_photoDelete.visibility = VISIBLE
            }

            Glide.with(this).load(uri).into(userPhoto)
        }
    }

    private fun makePost(mT: Int): Post {
        val id = (activity as MainActivity).getUserId()
        val title = eText_post_title.text.toString().trim()
        val lati = requireArguments().getDouble("lati")
        val long = requireArguments().getDouble("long")
        val text = eText_post.text.toString().trim()
        val date = Calendar.getInstance().time
        //val random = Math.random()*2

        val post = Post(0,id,0,lati,long,"","",title,currentImgUri,text,date)

        return post
    }

    private fun insertPost(post: Post) {
        var mMNSViewModel = ViewModelProvider(this).get(MNSViewModel::class.java)
        mMNSViewModel.addPost(post)
    }
}