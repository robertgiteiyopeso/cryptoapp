package com.example.cryptoapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.cryptoapp.databinding.FragmentLoginBinding
import com.example.cryptoapp.login.Credentials
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val MDBRepo = MDBRepositoryRetrofit("96d31308896f028f63b8801331250f03")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.bttnLogin.setOnClickListener {

//            //Get credentials from input fields
//            val username = binding.tiUsername.editText?.text.toString()
//            val password = binding.tiPassword.editText?.text.toString()
//
//            //If they're not empty, make the request
//            if (username != "" && password != "")
            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    //Get new token
                    val token = MDBRepo.getNewTokenParsed()
                    println("getNewTokenParsed() ran")

                    //Login
                    // val credentials = Credentials(username, password, token.requestToken)
                    val credentials = Credentials("robertyopeso", "filme123", token.requestToken)
                    val login = MDBRepo.login(credentials)
                    println("login() ran")

                    activity?.supportFragmentManager?.beginTransaction()
                        ?.replace(R.id.fragment_container_view_tag, HomeFragment(MDBRepo))
                        ?.commit()

//                    //Create session
//                    val session = MDBRepo.createSession(login)
//                    println("createSession() ran")
//
//                    //Invalidate session
//                    val invalidate = MDBRepo.invalidateSession(session)
//                    println("invalidateSession() ran")

                } catch (e: Exception) {
                    Log.e("LoginFragment: ", e.message.toString())
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}