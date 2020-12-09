package hu.bme.aut.fitary

import android.os.Bundle
import com.google.firebase.database.FirebaseDatabase
import hu.bme.aut.fitary.adapter.ExerciseAdapter
import hu.bme.aut.fitary.data.Exercise
import hu.bme.aut.fitary.data.Workout
import hu.bme.aut.fitary.extensions.validateNonEmpty
import kotlinx.android.synthetic.main.activity_create_workout.*

class CreateWorkoutActivity : BaseActivity() {
    val exercises: MutableList<Exercise> = mutableListOf()
    lateinit var exercisesAdapter: ExerciseAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_workout)

        exercisesAdapter = ExerciseAdapter(this, exercises)
        listViewExercises.adapter = exercisesAdapter
        listViewExercises.setOnItemClickListener {
                parent, view, position, id ->
            val exercise = parent.getItemAtPosition(position) as Exercise

            //TODO(Show dialog to edit exercise data)

            exercises[position] = exercise
        }
        
        btnAddExercise.setOnClickListener {
            //TODO(Show dialog to enter exercise data)
            val newExercise = Exercise("Push-up", 100)
            exercises.add(newExercise)

            exercisesAdapter.notifyDataSetChanged()
        }

        btnSend.setOnClickListener { sendClick() }
    }

    private fun sendClick() {
//        if (!validateForm()) return
//
//        uploadWorkout()
    }

//    private fun validateForm(): Boolean {
//        //TODO(Validate listView - Maybe create extension function for it)
//        return etExercise.validateNonEmpty() &&
//                etRepCount.validateNonEmpty() &&
//                etComment.validateNonEmpty()
//    }
//
//    private fun uploadWorkout() {
//        val key = FirebaseDatabase.getInstance().reference.child("workouts").push().key ?: return
//
//        val newWorkout = Workout(
//            uid,
//            userName,
//            etExercise.text.toString(),
//            etRepCount.text.toString().toInt(),
//            etComment.text.toString()
//        )
//
//        FirebaseDatabase.getInstance().reference
//            .child("workouts")
//            .child(key)
//            .setValue(newWorkout)
//            .addOnCompleteListener {
//                toast("Workout saved")
//                finish()
//            }
//    }

}