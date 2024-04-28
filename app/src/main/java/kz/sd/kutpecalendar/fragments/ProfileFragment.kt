package kz.sd.kutpecalendar.fragments

import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kz.sd.kutpecalendar.R
import kz.sd.kutpecalendar.base.BaseFragment
import kz.sd.kutpecalendar.databinding.FragmentProfileBinding
import kz.sd.kutpecalendar.firebase.UserDao
import javax.inject.Inject


@AndroidEntryPoint
class ProfileFragment:BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {
    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    @Inject
    lateinit var userDao: UserDao
    override fun onBindView() {
        super.onBindView()
        userDao.getData()
        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.signOutBtn.setOnClickListener {
            signOut()
        }
        userDao.getDataLiveData.observe(this){
            binding.name.text = it?.name
            binding.email.text = firebaseAuth.currentUser?.email
            binding.phone.text = it?.phoneNumber.toString()
            if (it?.pictureUrl != null) {
                Glide.with(requireContext())
                    .load(it?.pictureUrl)
                    .into(binding.ava)
            } else {
                binding.ava.setImageResource(R.drawable.profile_icon)
            }
        }


    }
    override fun onStart() {
        super.onStart()
        if(firebaseAuth.currentUser == null){
            findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
        }

    }
    private fun signOut() {
        var alertDialog: AlertDialog? = null
        alertDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle("Sign out")
            .setMessage("Are you sure you want to sign out?")
            .setPositiveButton("Yes") { _, _ ->
                firebaseAuth.signOut()
                alertDialog?.dismiss()
                findNavController().navigate(
                    R.id.action_profileFragment_to_loginFragment
                )
            }
            .setNegativeButton("Cancel") { _, _ ->
                alertDialog?.dismiss()
            }
            .show()
    }

}