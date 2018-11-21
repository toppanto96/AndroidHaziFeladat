package hu.nfc_gps.service

import android.nfc.cardemulation.HostApduService
import android.os.Bundle
import android.util.Log
import hu.nfc_gps.NfcUtils
import hu.nfc_gps.adapter.CardsAdapter

class CardService : HostApduService() {
    companion object {
        const val TAG = "HostTag"
        const val STATUS_SUCCESS = "9000"
        const val STATUS_FAILED = "6F00"
        const val CLA_NOT_SUPPORTED = "6E00"
        const val INS_NOT_SUPPORTED = "6D00"
        const val SELECT_INS = "A4"
        const val DEFAULT_CLA = "00"
        const val MIN_APDU_LENGTH = 12
    }

    override fun processCommandApdu(commandApdu: ByteArray?, extras: Bundle?): ByteArray? {
        Log.d(TAG, "Process lefutott")

        if (commandApdu == null) {
            return NfcUtils.hexStringToByteArray(STATUS_FAILED)
        }

        val hexCommandApdu = NfcUtils.toHex(commandApdu)
        if (hexCommandApdu.length < MIN_APDU_LENGTH) {
            return NfcUtils.hexStringToByteArray(STATUS_FAILED)
        }

        if (hexCommandApdu.substring(0, 2) != DEFAULT_CLA) {
            return NfcUtils.hexStringToByteArray(CLA_NOT_SUPPORTED)
        }

        if (hexCommandApdu.substring(2, 4) != SELECT_INS) {
            return NfcUtils.hexStringToByteArray(INS_NOT_SUPPORTED)
        }

        CardsAdapter.AID?.let {
            return if (hexCommandApdu.substring(10, 24) == it) {
                NfcUtils.hexStringToByteArray(STATUS_SUCCESS)
            } else {
                NfcUtils.hexStringToByteArray(STATUS_FAILED)
            }
        }
    }

    override fun onDeactivated(reason: Int) {
        Log.d(TAG, "Deactivated: $reason")
    }
}