package kz.sd.kutpecalendar.fragments

import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kz.sd.kutpecalendar.R
import kz.sd.kutpecalendar.base.BaseFragment
import kz.sd.kutpecalendar.databinding.FragmentRegisterBinding
import javax.inject.Inject

@AndroidEntryPoint
class RegisterFragment:BaseFragment<FragmentRegisterBinding>(FragmentRegisterBinding::inflate) {
    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    override var showBottomNavigation: Boolean = false

    override fun onBindView() {
        super.onBindView()

//        binding.backBtn.setOnClickListener{
//            findNavController().popBackStack()
//        }
        binding.signupBtn.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            val confirmPassword = binding.etConfirmpassword.text.toString()



            if (email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
                if (password == confirmPassword) {
                    firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                showCustomDialog("Success!", "You've successfully registered")
                                binding.tilEmail.isErrorEnabled = false
                                findNavController().navigate(
                                    R.id.action_registerFragment_to_userDetailsFragment
                                )
                            } else {
                                binding.tilEmail.isErrorEnabled = true
                                binding.tilConfirmpassword.isErrorEnabled = true
                                binding.tilPassword.isErrorEnabled = true
                                binding.tilEmail.error = "Something is wrong"
                                binding.tilPassword.error = "Something is wrong"
                                binding.tilConfirmpassword.error = "Something is wrong"
                            }
                        }

                } else {
                    Toast.makeText(
                        requireContext(),
                        "Passwords are not matching",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(requireContext(), "Enter something", Toast.LENGTH_SHORT).show()
            }
        }
        binding.haveAccountBtn.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
    }

}