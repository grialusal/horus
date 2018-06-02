package inakitajes.me.horus


import android.animation.TimeInterpolator
import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.db.chart.animation.Animation
import com.db.chart.model.LineSet
import com.db.chart.view.LineChartView
import com.github.zagum.speechrecognitionview.RecognitionProgressView
import com.github.zagum.speechrecognitionview.adapters.RecognitionListenerAdapter
import devlight.io.library.ArcProgressStackView
import kotlinx.android.synthetic.main.fragment_history.*
import java.text.DecimalFormat
import java.util.*

class HistoryFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRoundChart()
        setWeeklyChart()
        animateViews()
    }

    fun animateViews() {

        row1.alpha = 0F
        row2.alpha = 0F
        row3.alpha = 0F
        row4.alpha = 0F
        aspv.alpha = 0F
        linearChartView.alpha = 0F

        row1.animate().alpha(1F).setDuration(500).setStartDelay(100).start()
        row2.animate().alpha(1F).setDuration(500).setStartDelay(200).start()
        row3.animate().alpha(1F).setDuration(500).setStartDelay(300).start()
        row4.animate().alpha(1F).setDuration(500).setStartDelay(400).start()
        linearChartView.animate().alpha(1F).setDuration(500).setStartDelay(500).start()
        aspv.animate().alpha(1F).setDuration(500).setStartDelay(600).start()
    }

    fun setRoundChart() {

        val MODEL_COUNT = 4
        val mArcProgressStackView = view?.findViewById(R.id.aspv) as ArcProgressStackView
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

    fun setWeeklyChart() {

        var context = context ?: return
        val values = arrayOf(34F, 67F,67F,56F,64F,80F,70F)
        val labels = arrayOf("MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN")


        val set = LineSet(labels, values.toFloatArray())


        set.isSmooth = true
        set.setDotsRadius(10F)
        set.setDotsColor(ColorUtils.fetchColor(R.color.white, context))
        set.color = ColorUtils.fetchColor(R.color.white, context)
        set.thickness = 6F
        set.setFill(ColorUtils.fetchColor(R.color.light_70,context))

        linearChartView.addData(set)

        val paint = Paint()
        paint.color = ColorUtils.fetchColor(R.color.light_70,context)
        linearChartView.setGrid(4,6, paint)

        val format = DecimalFormat()
        format.maximumFractionDigits = 0
        linearChartView.setLabelsFormat(format)

        linearChartView.setAxisColor(ColorUtils.fetchColor(R.color.white, context))
        val axisOn = true
        linearChartView.setXAxis(axisOn)
        linearChartView.setYAxis(axisOn)
        linearChartView.setLabelsColor(ColorUtils.fetchColor(R.color.white, context))

        val anim = Animation(1000)
        anim.inSequence(0.6F)
        anim.setInterpolator(TimeInterpolators.easeOutCubic)
        linearChartView.show(anim)
    }




}

object TimeInterpolators {

    var easeOutElastic: TimeInterpolator = TimeInterpolator { t ->
        val p = 0.3
        (Math.pow(2.0,-10*t.toDouble()) * Math.sin((t-p/4)*(2*Math.PI)/p) + 1).toFloat()
    }

    val easeOutCubic: TimeInterpolator = TimeInterpolator { t ->
        (1 - Math.pow(1.0-t.toDouble(),3.0)).toFloat()
    }

}
