package hu.nfc_gps.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import hu.nfc_gps.R
import hu.nfc_gps.models.NfcCardModel
import kotlinx.android.synthetic.main.nfc_card_list_row.view.*

class CardsAdapter(private val context: Context) : RecyclerView.Adapter<CardsAdapter.ViewHolder>() {

    //TODO forgatás esetén elveszik, hogy melyik kártya van kiválasztva
    //TODO az AID nincs megjelenítve az XML-ben
    //TODO ha valaki már  be van jelentkezve, akkor ne kelljen újra bejelentkeznie az app elindításakor
    private var selectedCard = -1
    private val cards = mutableListOf<NfcCardModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.nfc_card_list_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (place, until) = cards[holder.adapterPosition]
        holder.apply {
            tvPlace.text = place
            tvUntil.text = until
            rbCard.apply {
                isChecked = (position == selectedCard)
                setOnClickListener {
                    selectedCard = adapterPosition
                    notifyDataSetChanged()
                }
            }
        }
    }

    override fun getItemCount() = cards.size

    fun addCard(card: NfcCardModel) {
        cards.add(card)
        notifyItemInserted(cards.lastIndex)
    }

    fun removeCard(card: NfcCardModel) {
        cards.remove(card)
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvPlace: TextView = itemView.card_place
        val tvUntil: TextView = itemView.card_until
        val rbCard: RadioButton = itemView.card_radio_button
    }
}