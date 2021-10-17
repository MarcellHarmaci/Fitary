package hu.bme.aut.fitary.ui.userProfile

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import androidx.core.content.ContextCompat
import co.zsmb.rainbowcake.base.RainbowCakeFragment
import co.zsmb.rainbowcake.dagger.getViewModelFromFactory
import co.zsmb.rainbowcake.extensions.exhaustive
import com.bumptech.glide.Glide
import hu.bme.aut.fitary.R
import kotlinx.android.synthetic.main.fragment_user_profile.*
import java.io.File

class UserProfileFragment : RainbowCakeFragment<UserProfileViewState, UserProfileViewModel>() {

    private val PICK_PROFILE_IMAGE = 9998
    private val REQUEST_PERMISSION_EXTERNAL_STORAGE = 9998

    override fun provideViewModel() = getViewModelFromFactory()
    override fun getViewResource() = R.layout.fragment_user_profile

    override fun onStart() {
        super.onStart()

        btnEditImage?.setOnClickListener {
            context?.let {
                when (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )) {
                    PackageManager.PERMISSION_GRANTED -> {
                        showImagePickerActivity()
                    }
                    PackageManager.PERMISSION_DENIED -> {
                        /*
                        if (shouldShowRequestPermissionRationale(
                                Manifest.permission.READ_EXTERNAL_STORAGE
                        )) {
                            // Show permission explanation dialog
                        }
                        */

                        requestPermissions(
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            REQUEST_PERMISSION_EXTERNAL_STORAGE
                        )
                    }
                }
            }
        }
    }

    private fun showImagePickerActivity() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_PROFILE_IMAGE)
    }

    override fun render(viewState: UserProfileViewState) {
        when (viewState) {
            is Loading -> {
                // TODO Display something to indicate loading
            }
            is UserProfileLoaded -> {
                viewState.userProfile.let {
                    tvUsernameDisplay.text = it.username
                    tvNumberOfWorkoutsDisplay.text = it.numberOfWorkouts.toString()
                    tvScoreOfWorkoutsDisplay.text = it.fullScore.toString()
                }
            }
        }.exhaustive
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_PERMISSION_EXTERNAL_STORAGE) {
            if (grantResults.isNotEmpty() &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
                showImagePickerActivity()
            } else {
                // Explain to the user that the feature is unavailable because
                // the features requires a permission that the user has denied.
                // At the same time, respect the user's decision. Don't link to
                // system settings in an effort to convince the user to change
                // their decision.
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            PICK_PROFILE_IMAGE -> {
                if (resultCode == Activity.RESULT_OK) {
                    val uri: Uri = data?.data!!
                    val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)

                    val cursor: Cursor? = context?.contentResolver?.query(
                        uri, filePathColumn, null, null, null
                    )
                    cursor?.moveToFirst()

                    val columnIndex: Int? = cursor?.getColumnIndex(filePathColumn[0])
                    val filePath: String? = columnIndex?.let { cursor.getString(it) }

                    context?.let {
                        Glide.with(it)
                            .load(File(filePath!!)).into(ivAvatar)
                    }
                }
            }
        }
    }

}