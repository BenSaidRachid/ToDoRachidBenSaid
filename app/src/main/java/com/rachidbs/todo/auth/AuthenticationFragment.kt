package com.rachidbs.todo.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.rachidbs.todo.MainActivity
import com.rachidbs.todo.R
import com.rachidbs.todo.databinding.FragmentAuthenticationBinding
import com.rachidbs.todo.network.Api

class AuthenticationFragment : Fragment() {
    private lateinit var binding: FragmentAuthenticationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAuthenticationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val navController = findNavController();

        binding.logIn.setOnClickListener {
            navController.navigate(R.id.action_authenticationFragment_to_loginFragment)
        }
        binding.signUp.setOnClickListener {
            navController.navigate(R.id.action_authenticationFragment_to_signupFragment)
        }

        if(!Api.INSTANCE.getToken().isNullOrEmpty()) {
            val newIntent = Intent(activity, MainActivity::class.java)
            newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(newIntent)
        }
    }
}