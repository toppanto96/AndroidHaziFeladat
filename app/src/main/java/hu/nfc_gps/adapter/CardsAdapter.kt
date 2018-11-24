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

    companion object {
        var AID: String? = null
    }
    //TODO az AID nincs megjelenítve az XML-ben
    private var selectedCard = -1
    private val cards = mutableListOf<NfcCardModel>()
    private var cardCallback: OnCardSelectedListener? = null

    interface OnCardSelectedListener {
        fun onCardSelected(Aid: String)
    }

    fun setOnCardSelectedListener(param: OnCardSelectedListener) {
        cardCallback = param
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.nfc_card_list_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (place, until, aid) = cards[holder.adapterPosition]
        holder.apply {
            tvPlace.text = place
            tvUntil.text = until
            rbCard.apply {
                isChecked = (position == selectedCard)
                setOnClickListener {
                    cardCallback?.apply {
                        onCardSelected(aid)
                        AID = aid
                    }
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

    fun getSelectedCardIndex() = selectedCard

    fun SetSelectedCardIndex(index: Int) {
        selectedCard = index
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvPlace: TextView = itemView.card_place
        val tvUntil: TextView = itemView.card_until
        val rbCard: RadioButton = itemView.card_radio_button
    }
}