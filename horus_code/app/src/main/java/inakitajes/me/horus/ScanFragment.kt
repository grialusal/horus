package inakitajes.me.horus

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_scan.*


class ScanFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_scan, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arregloMaloAnimateForever()
    }

    private fun arregloMaloAnimateForever() {
        /*
        scanImage.animate().alpha(0.2F).setDuration(1500).withEndAction {
            scanImage.animate().alpha(1F).setDuration(1500).withEndAction {
                arregloMaloAnimateForever()
            }.start()
        }.start()
        */
    }
}
