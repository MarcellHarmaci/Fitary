package hu.bme.aut.fitary

import android.os.Bundle
import com.google.firebase.database.FirebaseDatabase
import hu.bme.aut.fitary.data.Workout
import hu.bme.aut.fitary.extensions.validateNonEmpty
import kotlinx.android.synthetic.main.activity_create_workout.*

class CreateWorkoutActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_workout)

        btnSend.setOnClickListener { sendClick() }
    }

    private fun sendClick() {
        if (!validateForm()) return

        uploadWorkout()
    }

    private fun validateForm(): Boolean {
        return etExercise.validateNonEmpty() &&
                etRepCount.validateNonEmpty() &&
                etComment.validateNonEmpty()
    }

    private fun uploadWorkout() {
        val key = FirebaseDatabase.getInstance().reference.child("workouts").push().key ?: return

        val newWorkout = Workout(
            uid,
            userName,
            etExercise.text.toString(),
            etRepCount.text.toString().toInt(),
            etComment.text.toString()
        )

        FirebaseDatabase.getInstance().reference
            .child("workouts")
            .child(key)
            .setValue(newWorkout)
            .addOnCompleteListener {
                toast("Workout saved")
                finish()
            }
    }

}