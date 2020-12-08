package hu.bme.aut.fitary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import hu.bme.aut.fitary.data.Workout

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d("DEBUG_RTDB", "Call upload")
        uploadWorkout()
    }

    private fun uploadWorkout() {
        Log.d("DEBUG_RTDB", "Generate id")
        val key = FirebaseDatabase.getInstance().reference.child("workouts").push().key
        Log.d("DEBUG_RTDB", "id: " + key)
        Log.d("DEBUG_RTDB", "Create Workout object")
        val newWorkout = Workout(
            "123",
            "chest",
            10,
            "doit")
        Log.d("DEBUG_RTDB", "workout: " + newWorkout.toString())
        Log.d("DEBUG_RTDB", "Save object")
        FirebaseDatabase.getInstance().reference
            .child("workouts")
            .child(key!!)
            .setValue(newWorkout)
            .addOnSuccessListener { Log.d("DEBUG_RTDB", "Save SUCCESSFUL") }
            .addOnFailureListener { Log.d("DEBUG_RTDB", "Save FAILED") }
            .addOnCanceledListener { Log.d("DEBUG_RTDB", "Save CANCELED") }
            .addOnCompleteListener { Log.d("DEBUG_RTDB", "Save COMPLETED") }
        Log.d("DEBUG_RTDB", "Firebase DB save returned")
    }
}