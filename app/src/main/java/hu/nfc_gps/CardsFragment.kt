package hu.nfc_gps

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hu.nfc_gps.adapter.CardsAdapter
import kotlinx.android.synthetic.main.fragment_cards.*

class CardsFragment : Fragment() {

    private lateinit var cardsAdapter: CardsAdapter
    private lateinit var cardsLayoutManager: RecyclerView.LayoutManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_cards, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        cardsAdapter = CardsAdapter(
            activity as MainMenuActivity, listOf(
                NFC_card("Szeged", "2016"),
                NFC_card("Budapest", "2017"),
                NFC_card("Tata", "2018"),
                NFC_card("Szeged", "2016"),
                NFC_card("Budapest", "2017"),
                NFC_card("Tata", "2018")
            )
        )

        cardsLayoutManager = LinearLayoutManager(activity as MainMenuActivity)

        cards_recycler_view.apply {
            layoutManager = cardsLayoutManager
            adapter = cardsAdapter
        }
    }
}