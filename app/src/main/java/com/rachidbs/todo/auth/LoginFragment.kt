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
import com.rachidbs.todo.databinding.FragmentLoginBinding
import com.rachidbs.todo.utils.Constants
import kotlinx.serialization.ExperimentalSerializationApi


@ExperimentalSerializationApi
class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun checkFields(email: String, password: String): Boolean {
        return email.isNotEmpty() && password.isNotEmpty()
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
        binding.login.setOnClickListener {
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()
            if (checkFields(email, password)) {
                authViewModel.login(LoginForm(email, password))
            } else {
                Toast.makeText(context, "Veuillez remplir tous les champs", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }
}