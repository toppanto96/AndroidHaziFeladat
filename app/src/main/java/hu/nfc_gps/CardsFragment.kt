package hu.nfc_gps

import android.content.ComponentName
import android.nfc.NfcAdapter
import android.nfc.cardemulation.CardEmulation
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import hu.nfc_gps.adapter.CardsAdapter
import hu.nfc_gps.models.NfcCardModel
import hu.nfc_gps.service.CardService
import kotlinx.android.synthetic.main.fragment_cards.*

class CardsFragment : Fragment(), CardsAdapter.OnCardSelectedListener {

    private lateinit var cardsAdapter: CardsAdapter
    private lateinit var cardsLayoutManager: RecyclerView.LayoutManager
    private lateinit var cardEmulation: CardEmulation
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var ref: DatabaseReference

    companion object {
        private const val CARD_KEY = "Cards"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_cards, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        auth = FirebaseAuth.getInstance()

        database = FirebaseDatabase.getInstance()

        ref = database.reference.child(auth.uid.toString()).child(CARD_KEY)

        cardEmulation = CardEmulation.getInstance(NfcAdapter.getDefaultAdapter(activity as MainMenuActivity))

        cardsAdapter = CardsAdapter(activity as MainMenuActivity)

        cardsLayoutManager = LinearLayoutManager(activity as MainMenuActivity)

        cards_recycler_view.apply {
            layoutManager = cardsLayoutManager
            adapter = cardsAdapter
        }

        cardsAdapter.setOnCardSelectedListener(this)

        ref.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                val card = dataSnapshot.getValue(NfcCardModel::class.java)
                card?.let {
                    cardsAdapter.addCard(card)
                    cardsAdapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("myTag", "A hiba: ${error.message}")
            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {
                //empty
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {
                //empty
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                val card = dataSnapshot.getValue(NfcCardModel::class.java)
                card?.let {
                    cardsAdapter.removeCard(card)
                    cardsAdapter.notifyDataSetChanged()
                }
            }
        })
    }

    override fun onCardSelected(Aid: String) {
        cardEmulation.registerAidsForService(
            ComponentName(activity as MainMenuActivity, CardService::class.java),
            CardEmulation.CATEGORY_OTHER,
            mutableListOf(Aid)
        )
    }
}