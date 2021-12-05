package hu.bme.aut.fitary.ui.userProfile

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.content.ContextCompat
import co.zsmb.rainbowcake.base.RainbowCakeFragment
import co.zsmb.rainbowcake.dagger.getViewModelFromFactory
import co.zsmb.rainbowcake.extensions.exhaustive
import com.bumptech.glide.Glide
import hu.bme.aut.fitary.R
import kotlinx.android.synthetic.main.fragment_user_profile.*

class UserProfileFragment : RainbowCakeFragment<UserProfileViewState, UserProfileViewModel>() {

    companion object {
        private const val REQUEST_CODE_PICK_AVATAR = 10000
        private const val REQUEST_CODE_EXTERNAL_STORAGE = 10001
    }

    override fun provideViewModel() = getViewModelFromFactory()
    override fun getViewResource() = R.layout.fragment_user_profile

    override fun onStart() {
        super.onStart()

        fabEditImage.setOnClickListener { onAvatarEditButtonClicked() }
        btnSave.setOnClickListener {
            if (viewModel.state.value is UserProfileLoaded) {
                viewModel.save()
            } else {
                Toast.makeText(
                    requireContext(),
                    "The user profile is not loaded yet",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    @Suppress("IMPLICIT_CAST_TO_ANY")
    override fun render(viewState: UserProfileViewState) {
        when (viewState) {
            is Loading -> {
                // TODO Display something to indicate loading
            }
            is UserProfileLoaded -> {
                tvUsernameDisplay.text = viewState.username
                tvUserMailDisplay.text = viewState.userMail
                tvNumberOfWorkoutsDisplay.text = viewState.numberOfWorkouts.toString()
                tvScoreOfWorkoutsDisplay.text = viewState.fullScore.toString()

                context?.let {
                    if (viewState.avatar != null) {
                        val avatarAsBitmap = BitmapFactory.decodeByteArray(
                            viewState.avatar,
                            0,
                            viewState.avatar.size
                        )

                        Glide.with(it)
                            .asBitmap()
                            .load(avatarAsBitmap)
                            .circleCrop()
                            .into(ivAvatar)
                    } else {
                        Glide.with(it)
                            .load(R.drawable.ic_launcher_background)
                            .circleCrop()
                            .into(ivAvatar)
                    }
                }
            }
        }.exhaustive
    }

    private fun onAvatarEditButtonClicked() {
        context?.let {
            val permissionCheckResult = ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            )

            when (permissionCheckResult) {
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
                        REQUEST_CODE_EXTERNAL_STORAGE
                    )
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_CODE_EXTERNAL_STORAGE) {
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

    private fun showImagePickerActivity() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_CODE_PICK_AVATAR)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQUEST_CODE_PICK_AVATAR -> {
                if (resultCode == Activity.RESULT_OK) {
                    // Uri transformations
                    val uri: Uri = data?.data!!
                    val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)

                    val cursor: Cursor? = context?.contentResolver?.query(
                        uri, filePathColumn, null, null, null
                    )
                    cursor?.moveToFirst()

                    val columnIndex: Int? = cursor?.getColumnIndex(filePathColumn[0])
                    val filePath: String? = columnIndex?.let { cursor.getString(it) }

                    // Load image
                    viewModel.loadImageAsAvatar(filePath)
                }
            }
        }
    }

}