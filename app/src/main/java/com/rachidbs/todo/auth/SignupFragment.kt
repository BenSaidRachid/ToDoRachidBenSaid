package com.rachidbs.todo.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.preference.PreferenceManager
import com.rachidbs.todo.MainActivity
import com.rachidbs.todo.databinding.FragmentSignupBinding
import com.rachidbs.todo.utils.Constants
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
class SignupFragment : Fragment() {
    private lateinit var binding: FragmentSignupBinding
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun checkFields(
        vararg fields: String
    ): Boolean {
        for (field: String in fields) {
            if (field.isEmpty()) return false;
        }
        return true;
    }

    private fun checkPasswords(
        password: String,
        passwordConfirmation: String
    ): Boolean {
        return password == passwordConfirmation
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        authViewModel.response.observe(viewLifecycleOwner, { response ->
            if (response.token.isNotEmpty()) {
                PreferenceManager.getDefaultSharedPreferences(context).edit {
                    putString(Constants.SHARED_PREF_TOKEN_KEY, response.token)
                }
                val newIntent = Intent(activity, MainActivity::class.java)
                newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(newIntent)
            }
        })
        binding.signUp.setOnClickListener {
            val firstName = binding.firstName.text.toString()
            val lastName = binding.lastName.text.toString()
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()
            val passwordConfirmation = binding.passwordConfirmation.text.toString()
            if (checkFields(firstName, lastName, email, password, passwordConfirmation)) {
                if (checkPasswords(password, passwordConfirmation)) {
                    authViewModel.signUp(SignUpForm(firstName, lastName, email, password, passwordConfirmation))
                } else {
                    Toast.makeText(
                        context,
                        "Les mots de passe ne correspond pas",
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
            } else {
                Toast.makeText(context, "Veuillez remplir tous les champs", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }
}