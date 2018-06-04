package inakitajes.me.horus

import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.db.chart.animation.Animation
import com.db.chart.model.LineSet
import devlight.io.library.ArcProgressStackView
import kotlinx.android.synthetic.main.fragment_nutrition.*
import java.text.DecimalFormat
import java.util.*



class NutritionFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_nutrition, container, false)
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
        aspvLabel.alpha = 0F
        linearChartView.alpha = 0F
        linearChartlabel.alpha = 0F


        row1.animate().alpha(1F).setDuration(500).setStartDelay(100).start()
        row2.animate().alpha(1F).setDuration(500).setStartDelay(200).start()
        row3.animate().alpha(1F).setDuration(500).setStartDelay(300).start()
        row4.animate().alpha(1F).setDuration(500).setStartDelay(400).start()
        linearChartView.animate().alpha(1F).setDuration(500).setStartDelay(500).start()
        linearChartlabel.animate().alpha(1F).setDuration(500).setStartDelay(500).start()
        aspv.animate().alpha(1F).setDuration(500).setStartDelay(600).start()
        aspvLabel.animate().alpha(1F).setDuration(500).setStartDelay(600).start()
    }

    fun setRoundChart() {

        val MODEL_COUNT = 3
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
        models.add(ArcProgressStackView.Model("CARBS", 50F, bgColors[0], colors[0]))
        models.add(ArcProgressStackView.Model("PROTEIN", 70F, bgColors[1], colors[1]))
        models.add(ArcProgressStackView.Model("FAT", 80F, bgColors[2], colors[2]))
        mArcProgressStackView.models = models
    }

    fun setWeeklyChart() {

        var context = context ?: return
        val values = arrayOf(346F, 167F,627F,356F,664F,680F,870F)
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
