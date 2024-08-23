package com.example.mysoreprintersproject_lvdmanagement.lvdfragment

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import com.example.mysoreprintersproject_lvdmanagement.network.APIManager
import com.example.mysoreprintersproject_lvdmanagement.R
import com.example.mysoreprintersproject_lvdmanagement.network.DataSource
import com.example.mysoreprintersproject_lvdmanagement.network.SessionManager
import com.example.mysoreprintersproject_lvdmanagement.responses.LVDRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class LVDFragment : Fragment() {


    private lateinit var plantDescriptionSpinner: Spinner
    private lateinit var editDescriptionSpinner: Spinner
    private lateinit var dateEditText: EditText
    private lateinit var lprTimeEditText: EditText

    private var selectedPlantDescription: String? = null
    private var selectedEditDescription: String? = null
    private var selectedDate: String? = null
    private var lprTime: String? = null
    private lateinit var submitButton: AppCompatButton

    private lateinit var apiService: DataSource

    private lateinit var sessionManager: SessionManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_l_v_d, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        sessionManager= SessionManager(requireActivity())
        apiService= APIManager.apiInterface
        // Initialize views
        plantDescriptionSpinner = view?.findViewById(R.id.spinnerPlantDescription)!!
        editDescriptionSpinner = view?.findViewById(R.id.spinnerEditDescription)!!
        dateEditText = view?.findViewById(R.id.editTextDate)!!
        lprTimeEditText = view?.findViewById(R.id.editTextLPRTime)!!
        submitButton=view?.findViewById(R.id.submitButton)!!

        plantDescriptionSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedPlantDescription = parent.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        editDescriptionSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedEditDescription = parent.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // Set up DatePicker
        dateEditText.setOnClickListener {
            val calendar = Calendar.getInstance()
            val dateSetListener = DatePickerDialog.OnDateSetListener { view: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                val selectedDate = "$year-${month + 1}-$dayOfMonth"
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                this.selectedDate = dateFormat.format(calendar.time)
                dateEditText.setText(this.selectedDate)
            }
            DatePickerDialog(requireContext(), dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }





        submitButton.setOnClickListener {
            val id=sessionManager.fetchUserId()
            lprTime=lprTimeEditText.text.toString()
            postCollectionReport(id!!,selectedEditDescription!!,selectedEditDescription!!,selectedDate!!,lprTime!!)
        }


    }



    private fun postCollectionReport(id:String,plant_description:String,edition_description:String,date:String,lprtime:String){
        val id=sessionManager.fetchUserId()!!
        val token=sessionManager.fetchAuthToken()
        val authorization="Bearer $token"
        val postRequests= LVDRequest(id,plant_description,edition_description,date,lprtime)
        val call = apiService.postLVD(authorization,postRequests)
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {

                if (response.isSuccessful) {

                    Toast.makeText(requireActivity(),"LVD Report Successfully", Toast.LENGTH_SHORT).show()
                    //submitButton.text="Edit"
                } else {
                    // Handle other HTTP error codes if needed
                    Toast.makeText(requireActivity(), "Post Failed: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                // Handle failure (e.g., network failure, timeout)
                Toast.makeText(requireActivity(), "User Already Registered, Please Login!", Toast.LENGTH_SHORT).show()
            }
        })
    }

}
