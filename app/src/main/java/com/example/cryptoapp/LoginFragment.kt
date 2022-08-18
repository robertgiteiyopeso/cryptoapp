package com.example.cryptoapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.example.cryptoapp.databinding.FragmentLoginBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class LoginFragment : Fragment() {

    private val viewModel: LoginViewModel by viewModels()

    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.loginViewModel = viewModel

        binding.bttnLogin.setOnClickListener {
            //takes too long, just press the button and we worry about actual login when we need it
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.fragment_container_view_tag, HomeFragment())
                ?.commit()
//            //actual login
//            viewModel.doLogin()
        }

        viewModel.state.observe(viewLifecycleOwner) { state ->
            loginStateObserver(state)
        }

    }

    private fun loginStateObserver(state: LoginState) {
        when (state) {
            is LoginState.Error -> {
                binding.bttnLogin.text = "Login"
                binding.bttnLogin.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.secondaryColor))

                Toast.makeText(
                    requireContext(),
                    state.message,
                    Toast.LENGTH_LONG
                ).show()
            }
            LoginState.InProgress -> {
                binding.bttnLogin.text = "In progress"
                binding.bttnLogin.setBackgroundColor(R.color.transparent)
            }
            LoginState.Success -> {
                binding.bttnLogin.text = "Login"
                binding.bttnLogin.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.secondaryColor))

                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.fragment_container_view_tag, HomeFragment())
                    ?.commit()
            }
        }
    }


}