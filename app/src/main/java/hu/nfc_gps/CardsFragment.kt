package hu.nfc_gps

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import hu.nfc_gps.adapter.CardsAdapter
import hu.nfc_gps.models.NfcCardModel
import kotlinx.android.synthetic.main.fragment_cards.*

class CardsFragment : Fragment() {

    private lateinit var cardsAdapter: CardsAdapter
    private lateinit var cardsLayoutManager: RecyclerView.LayoutManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_cards, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        this.cardsAdapter = CardsAdapter(activity as MainMenuActivity)

        cardsLayoutManager = LinearLayoutManager(activity as MainMenuActivity)

        cards_recycler_view.apply {
            layoutManager = cardsLayoutManager
            adapter = cardsAdapter
        }

        val database = FirebaseDatabase.getInstance()
        val ref = database.reference.child("Tamas").child("Cards")

        ref.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val card = p0.getValue(NfcCardModel::class.java)
                card?.let {
                    cardsAdapter.addCard(card)
                    cardsAdapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                Log.d("myTag", "A hiba: ${p0.message}")
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildRemoved(p0: DataSnapshot) {
                val card = p0.getValue(NfcCardModel::class.java)
                card?.let {
                    cardsAdapter.removeCard(card)
                    cardsAdapter.notifyDataSetChanged()
                }
            }
        })
    }
}