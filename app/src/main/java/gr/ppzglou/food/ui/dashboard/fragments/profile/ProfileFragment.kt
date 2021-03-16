package gr.ppzglou.food.ui.dashboard.fragments.profile

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import gr.ppzglou.food.R
import gr.ppzglou.food.base.BaseFragment
import gr.ppzglou.food.databinding.FragmentProfileBinding
import gr.ppzglou.food.ext.foodToast
import gr.ppzglou.food.ext.isNullOrEmptyOrNullString
import gr.ppzglou.food.ext.safeNavigate
import gr.ppzglou.food.ext.setPic
import gr.ppzglou.food.ui.dashboard.DashboardViewModel

class ProfileFragment : BaseFragment<FragmentProfileBinding>(), ProfileAdapter.OnItemClickListener {

    private val viewModel: DashboardViewModel by activityViewModels()
    private val profileAdapter = ProfileAdapter(this)
    private val REQUEST_GALLERY = 1
    private var imageUri: Uri? = null

    override fun getViewBinding(): FragmentProfileBinding =
        FragmentProfileBinding.inflate(layoutInflater)

    override fun setupObservers() {
        viewModel.successProfile.observe(viewLifecycleOwner) {
            with(binding) {
                name.text = it.name
                email.text = it.email
                if (!it.phone.isNullOrEmptyOrNullString) {
                    phone.text = it.phone
                    phone.isVisible = true
                }
                it.photo?.let { it1 -> profilePic.setPic(it1) }
            }
        }

        viewModel.successUploadedFile.observe(viewLifecycleOwner) {
            activity?.foodToast("success uploaded!")
            binding.profilePic.setImageURI(imageUri)
        }
    }

    override fun setupViews() {
        profileAdapter.submitList(viewModel.getMenu())
        binding.recyclerView.adapter = profileAdapter
        viewModel.profile()
    }

    override fun setupListeners() {
        with(binding) {
            profilePic.setOnClickListener {
                openFile()
            }
        }
    }

    private fun openFile() {
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(gallery, REQUEST_GALLERY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == REQUEST_GALLERY) {
            val uri = data?.data
            if (uri != null) {
                viewModel.uploadFile(uri)
                imageUri = uri
            } else
                activity?.foodToast("error")
        }
    }

    override fun onMenuItemClick(nav: NavDirections) {
        findNavController().safeNavigate(
            nav,
            R.id.nav_profile
        )
    }
}