package inakitajes.me.horus

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_alerts.*


class AlertsFragment : Fragment()  {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_alerts, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        animateViews()

        alertsLinearLayout.setOnClickListener {
            row2.visibility = View.GONE
            row4.visibility = View.GONE
        }
    }

    fun animateViews() {

        row1.alpha = 0F
        row2.alpha = 0F
        row3.alpha = 0F
        row4.alpha = 0F


        row1.animate().alpha(1F).setDuration(500).setStartDelay(100).start()
        row2.animate().alpha(1F).setDuration(500).setStartDelay(200).start()
        row3.animate().alpha(1F).setDuration(500).setStartDelay(300).start()
        row4.animate().alpha(1F).setDuration(500).setStartDelay(400).start()

    }



}
