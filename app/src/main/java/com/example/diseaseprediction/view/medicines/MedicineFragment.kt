package com.example.diseaseprediction.view.medicines

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.diseaseprediction.R
import com.example.diseaseprediction.databinding.FragmentConsultationBinding
import com.example.diseaseprediction.databinding.FragmentMedicineBinding
import com.example.diseaseprediction.view.consultate.ConsultationViewModel

class MedicineFragment : Fragment() {
    private var _binding: FragmentMedicineBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel : MedicineViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[MedicineViewModel::class.java]
        _binding = FragmentMedicineBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val toolbar: Toolbar = binding.toolbar

        toolbar.inflateMenu(R.menu.settings_menu)
        return root
    }

//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        // TODO: Use the ViewModel
//    }


}