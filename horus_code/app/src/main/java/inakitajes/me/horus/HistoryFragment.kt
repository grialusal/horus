package inakitajes.me.horus


import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.zagum.speechrecognitionview.RecognitionProgressView
import com.github.zagum.speechrecognitionview.adapters.RecognitionListenerAdapter
import devlight.io.library.ArcProgressStackView
import java.util.ArrayList

class HistoryFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val MODEL_COUNT = 4
        val mArcProgressStackView = view.findViewById(R.id.aspv) as ArcProgressStackView
        mArcProgressStackView.shadowRadius = 0F
        mArcProgressStackView.animationDuration = 1000
        mArcProgressStackView.sweepAngle = 270F
        mArcProgressStackView.setIsRounded(true)
        mArcProgressStackView.textColor = Color.BLACK

        val stringColors = this.resources.getStringArray(R.array.medical_express)
        val stringBgColors = this.resources.getStringArray(R.array.medical_express_bg)

        val colors = IntArray(MODEL_COUNT)
        val bgColors = IntArray(MODEL_COUNT)
        for (i in 0 until MODEL_COUNT) {
            colors[i] = Color.parseColor(stringColors[i])
            bgColors[i] = Color.parseColor(stringBgColors[i])
        }

        val models = ArrayList<ArcProgressStackView.Model>()
        models.add(ArcProgressStackView.Model("PASOS", 50F, bgColors[0], colors[0]))
        models.add(ArcProgressStackView.Model("KCAL", 70F, bgColors[1], colors[1]))
        models.add(ArcProgressStackView.Model("SUEÑO", 80F, bgColors[2], colors[2]))
        models.add(ArcProgressStackView.Model("ESTRÉS", 90F, bgColors[3], colors[3]))
        mArcProgressStackView.models = models

    }



}
