package com.example.bozorbek_vol2.ui.main.profile.fragment

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.*
import androidx.lifecycle.Observer
import androidx.loader.content.CursorLoader
import androidx.navigation.fragment.findNavController
import com.example.bozorbek_vol2.R
import com.example.bozorbek_vol2.model.main.profile.Profile
import com.example.bozorbek_vol2.ui.OnDataStateChangeListener
import com.example.bozorbek_vol2.ui.auth.AuthActivity
import com.example.bozorbek_vol2.ui.main.profile.state.ProfileStateEvent
import com.example.bozorbek_vol2.util.Constants
import com.example.bozorbek_vol2.util.LocaleHelper
import com.yalantis.ucrop.UCrop
import com.yalantis.ucrop.model.AspectRatio
import com.yalantis.ucrop.view.CropImageView
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.*


//import com.theartofdev.edmodo.cropper.CropImage
//import com.theartofdev.edmodo.cropper.CropImageView


class ProfileFragment : BaseProfileFragment() {

    private var showMenu: Boolean = false
    private lateinit var onDataStateChangeListener: OnDataStateChangeListener
    private lateinit var uri_image: String
    private lateinit var image_url: String
    private lateinit var username: String

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
            if (onDataStateChangeListener.isStoragePermissionGranted()) {
                pickFromGallery()
            }
        }

        observeLanguage()

        observeData()
        getProfileInfo()
    }

    private fun observeLanguage() {
        localeHelper = LocaleHelper().setLocale(requireContext(), Locale.getDefault().language)!!
        val resurs = localeHelper.resources

        save_profile.setText(resurs.getString(R.string.save_profile_data))
    }

    private fun pickFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        val mimeTypes = arrayListOf("image/jpeg", "image/jpg", "image/png")
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivityForResult(intent, Constants.GALLERY_REQUEST_CODE)
    }

    

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d(TAG, "onActivityResult: ${requestCode}")
        if (resultCode == RESULT_OK && requestCode == Constants.GALLERY_REQUEST_CODE) {
            data?.data?.let { uri ->
                activity?.let {
                    launchImageCrop(uri)
                }
            }
            }
        if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK)
        {
//            Log.d(TAG, "onActivityResult: ${requestCode}")
            data?.let { data ->
                setImageToServerAndUI(data)
            }
        }

    }

    fun launchImageCrop(uri: Uri) {
        context?.let {
            Log.d(TAG, "launchImageCrop: ${uri}")
            UCrop.of(uri, Uri.fromFile(File(requireContext().cacheDir, "SampleImageCrop.jpg")))
                .withAspectRatio(16f, 9f)
                .withAspectRatio(4f, 3f)
                .withOptions(getCropOptions())
                .withMaxResultSize(450, 450)
                .start(requireContext(),this, UCrop.REQUEST_CROP)


        }
    }

    private fun getCropOptions(): UCrop.Options {
        val options = UCrop.Options()
        options.setCompressionQuality(70)
        options.setHideBottomControls(false)
        options.setFreeStyleCropEnabled(false)
        options.setImageToCropBoundsAnimDuration(666)
        options.setAspectRatioOptions(
            2,
            AspectRatio("1:2", 1f, 2f),
            AspectRatio("3:4", 3f, 4f),
            AspectRatio(
                "ORIGINAL",
                CropImageView.DEFAULT_ASPECT_RATIO,
                CropImageView.DEFAULT_ASPECT_RATIO
            ),
            AspectRatio("16:9", 16f, 9f),
            AspectRatio("1:1", 1f, 1f)
        )

        options.withAspectRatio(
            CropImageView.DEFAULT_ASPECT_RATIO,
            CropImageView.DEFAULT_ASPECT_RATIO
        )
        options.useSourceImageAspectRatio()
        return options
    }

    private fun setImageToServerAndUI(data: Intent) {
        val uri_image = UCrop.getOutput(data)
        if (uri_image != null) {
            Log.d(TAG, "setImageToServerAndUI: ${uri_image}")
            profile_user_image2.setImageURI(Uri.EMPTY)
            profile_user_image2.setImageURI(uri_image)

            var multiPartBody: MultipartBody.Part? = null
            uri_image.path?.let {filePath ->
                val imageFile = File(filePath)
                Log.d(TAG, "imageFile: ${imageFile}")
                val requestBody = RequestBody.create(
                    "image/*".toMediaTypeOrNull(),
                    imageFile
                )
                multiPartBody = MultipartBody.Part.createFormData(
                    "image",
                    imageFile.name,
                    requestBody
                )
            }

            multiPartBody?.let {
                GlobalScope.launch(Dispatchers.Main) {
                    delay(2000)
                    viewModel.setStateEvent(event = ProfileStateEvent.UploadProfileImage(image = it))

                }
            }
        }

//            uri_image?.path?.let { filePath ->
//                val imageFile = File(getRealPathFromURI(uri_image))
//                Log.d(TAG, "imageFile: ${imageFile}")
//                val requestBody = RequestBody.create(
//                    this.requireContext()?.contentResolver?.getType(uri_image)
//                        ?.let { it.toMediaTypeOrNull() },
//                    imageFile
//                )
//
//                multiPartBody = MultipartBody.Part.createFormData(
//                    "image",
//                    imageFile.name,
//                    requestBody,
//                )
//
//                multiPartBody?.let {
//                    Log.d(TAG, "onActivityResult: ")
//                    GlobalScope.launch(Dispatchers.Main) {
//                        delay(2000)
//                        viewModel.setStateEvent(event = ProfileStateEvent.UploadProfileImage(image = it))
//
//                    }
//                }
//            }


    }


    override fun onResume() {
        super.onResume()
        viewModel.setStateEvent(event = ProfileStateEvent.GetProfileInfo())
    }


    private fun observeData() {

        Log.d(TAG, "observeData: ${viewModel.sessionManager.cachedAuthToken.value}")
        val checkUser = viewModel.sessionManager.cachedAuthToken.value

        if (checkUser != null) {
            showMenu = true
            lottie_constraint.visibility = View.GONE
            profile_constraint_layout.visibility = View.VISIBLE
            viewModel.setStateEvent(event = ProfileStateEvent.GetProfileInfo())

        } else {
            showMenu = false
            lottie_constraint.visibility = View.VISIBLE
            profile_constraint_layout.visibility = View.GONE
        }

    }

    private fun getProfileInfo() {
        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            if (dataState != null) {
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
        if (showMenu) {
            inflater.inflate(R.menu.main_bottom_sheet, menu)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu -> {
                val action =
                    ProfileFragmentDirections.actionProfileFragmentToProfileBottomSheetDialogFragment(
                        image = image_url,
                        username = username
                    )
                findNavController().navigate(action)
            }

            R.id.log_out -> {
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