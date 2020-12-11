package hu.bme.aut.fitary.ui.workouts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import hu.bme.aut.fitary.R
import hu.bme.aut.fitary.adapter.WorkoutAdapter
import hu.bme.aut.fitary.data.Workout
import kotlinx.android.synthetic.main.fragment_workouts_social.view.*

class SocialWorkoutsFragment : Fragment() {

    private lateinit var workoutAdapter: WorkoutAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_workouts_social, container, false)
        workoutAdapter = WorkoutAdapter(context?.applicationContext)
        root.rvSocialWorkouts.layoutManager = LinearLayoutManager(container?.context).apply {
            reverseLayout = true
            stackFromEnd = true
        }
        root.rvSocialWorkouts.adapter = workoutAdapter

        initWorkoutsListener()

        return root
    }

    private fun initWorkoutsListener() {
        FirebaseDatabase.getInstance()
            .getReference("workouts")
            .addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                    val newWorkout = dataSnapshot.getValue<Workout>(Workout::class.java)
                    workoutAdapter.addWorkout(newWorkout)
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    TODO("Not yet implemented")
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    TODO("Not yet implemented")
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                    TODO("Not yet implemented")
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }

}