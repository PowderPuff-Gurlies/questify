package com.example.and101_capstone.ui.notifications

import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.example.and101_capstone.R
import com.example.and101_capstone.databinding.FragmentNotificationsBinding
import com.example.and101_capstone.ui.notifications.CalendarQuickstart.signIn
import kotlinx.coroutines.launch


class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textNotifications
        notificationsViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

       //val view: View = inflater!!.inflate(R.layout.fragment_notifications, container, false)

        //view.findViewById<View>(R.id.google_sign_in_button).setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                val SDK_INT = Build.VERSION.SDK_INT
                if (SDK_INT > 8) {
                    val policy = ThreadPolicy.Builder()
                        .permitAll().build()
                    StrictMode.setThreadPolicy(policy)
                    //your codes here
//                    CalendarQuickstart.main1()
                    signIn(requireContext())
                    //CalendarQuickstart.main(requireContext())
                }
            }
        //}
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

//    private lateinit var mGoogleSignInClient: GoogleSignInClient
//
//    mGoogleSignInClient = GoogleSignIn.getClient(this, null)


}