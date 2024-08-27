package com.example.mysoreprintersproject_lvdmanagement.lvdfragment

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.mysoreprintersproject_lvdmanagement.network.APIManager
import com.example.mysoreprintersproject_lvdmanagement.R
import com.example.mysoreprintersproject_lvdmanagement.network.DataSource
import com.example.mysoreprintersproject_lvdmanagement.network.SessionManager
import com.example.mysoreprintersproject_lvdmanagement.responses.LVDRequest
import com.example.mysoreprintersproject_lvdmanagement.responses.ProfileResponses
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
    private lateinit var progressBar: ProgressBar

    private lateinit var sessionManager: SessionManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_l_v_d, container, false)
    }

    @Deprecated("Deprecated in Java")
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

        progressBar=requireView().findViewById(R.id.progressBar)

        // Load the string arrays from resources
        val plantDescriptions = resources.getStringArray(R.array.plant_description)
        val editDescriptions = resources.getStringArray(R.array.edition_description)
        // Setting up the adapters for the Spinners
        val plantDescriptionAdapter = ArrayAdapter(requireActivity(), android.R.layout.simple_spinner_item, plantDescriptions)
        plantDescriptionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        plantDescriptionSpinner.adapter = plantDescriptionAdapter

        val editDescriptionAdapter = ArrayAdapter(requireActivity(), android.R.layout.simple_spinner_item, editDescriptions)
        editDescriptionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        editDescriptionSpinner.adapter = editDescriptionAdapter

// Handling item selection
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



        getExecutiveProfile()


        submitButton.setOnClickListener {
            val id = sessionManager.fetchUserId()
            lprTime = lprTimeEditText.text.toString()

            // Check if any required field is missing
            when {
                id.isNullOrEmpty() -> {
                    Toast.makeText(context, "User ID is missing", Toast.LENGTH_SHORT).show()
                }
                selectedPlantDescription.isNullOrEmpty() -> {
                    Toast.makeText(context, "Please select a plant description", Toast.LENGTH_SHORT).show()
                }
                selectedEditDescription.isNullOrEmpty() -> {
                    Toast.makeText(context, "Please select an edit description", Toast.LENGTH_SHORT).show()
                }
                selectedDate.isNullOrEmpty() -> {
                    Toast.makeText(context, "Please select a date", Toast.LENGTH_SHORT).show()
                }
                lprTime!!.isEmpty() -> {
                    Toast.makeText(context, "Please enter the time", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    // All required fields are selected/entered
                    postCollectionReport(id, selectedPlantDescription!!, selectedEditDescription!!, selectedDate!!, lprTime!!)
                    progressBar.visibility = View.VISIBLE
                }
            }
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

                progressBar.visibility=View.GONE
                if (response.isSuccessful) {

                    Toast.makeText(requireActivity(),"LVD Report Sent Successfully", Toast.LENGTH_SHORT).show()
                    progressBar.visibility=View.GONE
                    //submitButton.text="Edit"
                } else {
                    // Handle other HTTP error codes if needed
                    Toast.makeText(requireActivity(), "Post Failed: ${response.message()}", Toast.LENGTH_SHORT).show()
                    progressBar.visibility=View.GONE
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                // Handle failure (e.g., network failure, timeout)
                Toast.makeText(requireActivity(), "User Already Registered, Please Login!", Toast.LENGTH_SHORT).show()
                progressBar.visibility=View.GONE
            }
        })
    }


    private fun getExecutiveProfile() {
        val serviceGenerator = APIManager.apiInterface
        val accessToken = sessionManager.fetchAuthToken()
        val authorization = "Bearer $accessToken"
        val id = sessionManager.fetchUserId()!!

        serviceGenerator.getProfileOfLVD(authorization, id.toInt())
            .enqueue(object : retrofit2.Callback<ProfileResponses> {
                override fun onResponse(call: Call<ProfileResponses>, response: Response<ProfileResponses>) {
                    val profileResponses = response.body()

                    if(profileResponses!=null){

                        val profileImage: ImageView =requireActivity().findViewById(R.id.imageSettings2)
                        val image=profileResponses.profileImage
                        val file= APIManager.getImageUrl(image!!)
                        Glide.with(requireActivity()).load(file).into(profileImage)
                    }
                }

                override fun onFailure(call: Call<ProfileResponses>, t: Throwable) {
                    Toast.makeText(requireActivity(), "Error fetching data", Toast.LENGTH_SHORT).show()
                }
            })
    }
}
