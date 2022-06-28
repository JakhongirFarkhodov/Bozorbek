package com.example.bozorbek_vol2.ui.main.profile.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.loader.content.CursorLoader
import androidx.navigation.fragment.findNavController
import com.example.bozorbek_vol2.R
import com.example.bozorbek_vol2.model.main.profile.Profile
import com.example.bozorbek_vol2.ui.OnDataStateChangeListener
import com.example.bozorbek_vol2.ui.auth.AuthActivity
import com.example.bozorbek_vol2.ui.main.profile.state.ProfileStateEvent
import com.example.bozorbek_vol2.util.Constants
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File


//import com.theartofdev.edmodo.cropper.CropImage
//import com.theartofdev.edmodo.cropper.CropImageView


class ProfileFragment : BaseProfileFragment() {

    private var showMenu:Boolean = false
    private lateinit var onDataStateChangeListener: OnDataStateChangeListener
    private lateinit var uri_image:String
    private lateinit var image_url:String
    private lateinit var username:String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        mb_go_to_registration.setOnClickListener {
            val intent = Intent(requireActivity(), AuthActivity::class.java)
            startActivity(intent)
        }

        profile_user_image2.setOnClickListener {
            if (onDataStateChangeListener.isStoragePermissionGranted())
            {
                pickFromGallery()
            }
        }

        observeData()
        getProfileInfo()
    }

    private fun pickFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        val mimeTypes = arrayListOf("image/jpeg","image/jpg","image/png")
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivityForResult(intent, Constants.GALLERY_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == Constants.GALLERY_REQUEST_CODE)
        {
            Toast.makeText(this.requireContext(), "OK", Toast.LENGTH_LONG).show()
            val uri = data?.data
            var multiPartBody:MultipartBody.Part? = null
            uri?.let { uri1 ->

                Log.d(TAG, "uri: ${uri1}")
                uri1.path?.let { filePath ->
                    val imageFile = File(getRealPathFromURI(uri1))
                    Log.d(TAG, "imageFile: ${imageFile}")
                    val requestBody = RequestBody.create(
                        this.requireContext()?.contentResolver?.getType(uri1)
                            ?.let { it.toMediaTypeOrNull() },
                        imageFile
                    )

                    multiPartBody = MultipartBody.Part.createFormData(
                        "image",
                        imageFile.name,
                        requestBody,
                    )

                    multiPartBody?.let {
                        Log.d(TAG, "onActivityResult: ")
                        GlobalScope.launch(Main) {
                            delay(2000)
                            viewModel.setStateEvent(event = ProfileStateEvent.UploadProfileImage(image = it))

                        }
                    }
                }
            }




            uri_image = uri.toString()
            profile_user_image2.setImageURI(uri)
        }
    }


    override fun onResume() {
        super.onResume()
        viewModel.setStateEvent(event = ProfileStateEvent.GetProfileInfo())
    }


    private fun observeData() {

        Log.d(TAG, "observeData: ${viewModel.sessionManager.cachedAuthToken.value}")
        val checkUser = viewModel.sessionManager.cachedAuthToken.value

        if (checkUser != null)
        {
            showMenu = true
            lottie_constraint.visibility = View.GONE
            profile_constraint_layout.visibility = View.VISIBLE
            viewModel.setStateEvent(event = ProfileStateEvent.GetProfileInfo())

        }
        else{
            showMenu = false
            lottie_constraint.visibility = View.VISIBLE
            profile_constraint_layout.visibility = View.GONE
        }

    }

    private fun getProfileInfo() {
        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            if (dataState != null)
            {
                onDataStateChangeListener.onDataStateChange(dataState)
                dataState.data?.let { data ->
                    data.data?.let { event ->
                        event.getContentIfNotHandled()?.let { profileViewState ->
                            profileViewState.profile?.let { profile ->
                                Log.d(TAG, "getProfileInfo dataState: ${profile}")
                                viewModel.setProfileData(profile)
                            }
                        }
                    }
                }
            }

        })

        viewModel.viewState.observe(viewLifecycleOwner, Observer { profileViewState ->
            profileViewState.profile?.let { profile ->
                Log.d(TAG, "getProfileInfo viewState: ${profile}")
                setProfileData(profile)
            }
        })
    }

    private fun setProfileData(profile: Profile) {
        requestManager.load(Constants.BASE_URL + profile.get_image).into(profile_user_image2)
        profile_user_title.setText(profile.first_name)
        profile_text_name.setText(profile.first_name)
        profile_text_phone.setText(profile.username)

        image_url = Constants.BASE_URL + profile.get_image
        username = profile.first_name
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        if (showMenu)
        {
            inflater.inflate(R.menu.main_bottom_sheet, menu)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId)
        {
            R.id.menu ->{
                val action = ProfileFragmentDirections.actionProfileFragmentToProfileBottomSheetDialogFragment(image = image_url, username = username)
                findNavController().navigate(action)
            }

            R.id.log_out ->{
                sessionManager.logOut()
            }

        }
        return super.onOptionsItemSelected(item)

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            onDataStateChangeListener = context as OnDataStateChangeListener
        } catch (e: Exception) {
            Log.d(TAG, "onAttach: ${context} must implement OnDataStateChangeListener")
        }
    }

    private fun getRealPathFromURI(contentUri: Uri): String {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val loader = CursorLoader(this.requireContext(), contentUri, proj, null, null, null)
        val cursor: Cursor = loader.loadInBackground()!!
        val column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        val result = cursor.getString(column_index)
        cursor.close()
        return result
    }

}