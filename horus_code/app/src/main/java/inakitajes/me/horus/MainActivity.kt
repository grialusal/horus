package inakitajes.me.horus

import android.Manifest
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.speech.SpeechRecognizer
import com.github.zagum.speechrecognitionview.adapters.RecognitionListenerAdapter
import com.github.zagum.speechrecognitionview.RecognitionProgressView
import android.speech.RecognizerIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Color
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.AsyncTask
import android.os.Build
import android.os.CountDownTimer
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.TextWatcher
import android.transition.Transition
import android.util.Log
import android.view.*
import android.widget.*
import com.amazonaws.auth.CognitoCachingCredentialsProvider
import devlight.io.library.ArcProgressStackView
import devlight.io.library.ArcProgressStackView.Model
import com.amazonaws.regions.Regions
import com.amazonaws.auth.CognitoCredentialsProvider
import com.amazonaws.mobileconnectors.lex.interactionkit.InteractionClient
import com.amazonaws.mobileconnectors.lex.interactionkit.Response
import com.amazonaws.mobileconnectors.lex.interactionkit.config.InteractionConfig
import com.amazonaws.mobileconnectors.lex.interactionkit.ui.InteractiveVoiceView
import kotlinx.android.synthetic.main.activity_main.*
import com.amazonaws.metrics.AwsSdkMetrics.setCredentialProvider
import java.util.*
import inakitajes.me.horus.R.id.voiceView
import com.amazonaws.metrics.AwsSdkMetrics.setCredentialProvider
import com.amazonaws.services.polly.AmazonPollyPresigningClient
import com.amazonaws.services.polly.model.*
import java.io.IOException
import java.io.Serializable
import java.text.SimpleDateFormat


class MainActivity : AppCompatActivity(), InteractiveVoiceView.InteractiveVoiceListener {

    var speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
    private val REQUEST_RECORDING_PERMISSIONS_RESULT = 75
    private var credentialsProvider: CognitoCredentialsProvider? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setFullScreenVisibility()
        createTimeUpdater()

        initLex()
        setOnclickListeners()

        val recognitionProgressView = findViewById<View>(R.id.recognition_view) as RecognitionProgressView
        val colors1 = intArrayOf(ContextCompat.getColor(this, R.color.white),
                ContextCompat.getColor(this, R.color.white),
                ContextCompat.getColor(this, R.color.white),
                ContextCompat.getColor(this, R.color.white),
                ContextCompat.getColor(this, R.color.white))
        recognitionProgressView.setColors(colors1)
        recognitionProgressView.setSpeechRecognizer(speechRecognizer)

        recognitionProgressView.play()

        // Starting with Marshmallow we need to explicitly ask if we can record audio
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) ==
                    PackageManager.PERMISSION_GRANTED) {
            } else {
                requestPermissions(arrayOf(Manifest.permission.RECORD_AUDIO), REQUEST_RECORDING_PERMISSIONS_RESULT);
            }
        }

    }


    private fun createTimeUpdater() {
        object : CountDownTimer(Long.MAX_VALUE, 60000) {

            override fun onTick(millisUntilFinished: Long) {
                var currentTime = Calendar.getInstance().time
                var simpleTimeFormat = SimpleDateFormat("HH:mm")
                var simpleDateFormat = SimpleDateFormat("MMMM, dd")
                timeLabel.text = simpleTimeFormat.format(currentTime)
                dateLabel.text = simpleDateFormat.format(currentTime)
            }

            override fun onFinish() { start() }

        }.start()
    }


    private fun initLex() {
        val appContext = applicationContext

        voiceView.setInteractiveVoiceListener(this)
        credentialsProvider = CognitoCredentialsProvider(
                resources.getString(R.string.identity_id_test),
                Regions.US_EAST_1)

        voiceView.viewAdapter.setCredentialProvider(credentialsProvider)
        voiceView.viewAdapter.setInteractionConfig(
                InteractionConfig(appContext.getString(R.string.bot_name),
                        appContext.getString(R.string.bot_alias)))
        voiceView.viewAdapter.awsRegion = appContext.getString(R.string.lex_region)
    }

    override fun dialogReadyForFulfillment(slots: Map<String, String>, intent: String) {
        Log.d("MAIN_ACTIVITY", String.format(
                Locale.US,
                "Dialog ready for fulfillment:\n\tIntent: %s\n\tSlots: %s",
                intent,
                slots.toString()))
    }

    var currentFragment: Fragment? = null
    override fun onResponse(response: Response) {
        Log.d("MAIN_ACTIVITY", "Bot response: " + response.textResponse)
        Log.d("MAIN_ACTIVITY", "Transcript: " + response.inputTranscript)

        speechTest.text = response.inputTranscript ?: ""

        response.intentName ?: return

        var buttons = arrayOf(homeButton,alertsButton,nutritionButton,scanButton,fitnessButton,helpButton,historyButton)

        when(response.intentName) {

            "ShowHistory" -> {
                val transaction = supportFragmentManager.beginTransaction()
                currentFragment = HistoryFragment()
                transaction
                        .replace(R.id.fragmentFrame, currentFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()

                for (button in buttons) { button.alpha = 0.5F }
                historyButton.alpha = 1F
            }

            "showAlerts" -> {
                val transaction = supportFragmentManager.beginTransaction()
                currentFragment = AlertsFragment()
                transaction
                        .replace(R.id.fragmentFrame, currentFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()

                for (button in buttons) { button.alpha = 0.5F }
                alertsButton.alpha = 1F
            }

            "showNutrition" -> {
                val transaction = supportFragmentManager.beginTransaction()
                currentFragment = NutritionFragment()
                transaction
                        .replace(R.id.fragmentFrame, currentFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()

                for (button in buttons) { button.alpha = 0.5F }
                nutritionButton.alpha = 1F
            }

            "showFitness" -> {
                val transaction = supportFragmentManager.beginTransaction()
                currentFragment = FitnessFragment()
                transaction
                        .replace(R.id.fragmentFrame, currentFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()

                for (button in buttons) { button.alpha = 0.5F }
                fitnessButton.alpha = 1F
            }

            "showScan" -> {
                val transaction = supportFragmentManager.beginTransaction()
                currentFragment = ScanFragment()
                transaction
                        .replace(R.id.fragmentFrame, currentFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()

                for (button in buttons) { button.alpha = 0.5F }
                scanButton.alpha = 1F
            }

            "showHelp" -> {
                val transaction = supportFragmentManager.beginTransaction()
                currentFragment = HelpFragment()
                transaction
                        .replace(R.id.fragmentFrame, currentFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()

                for (button in buttons) { button.alpha = 0.5F }
                helpButton.alpha = 1F
            }

            "goback" -> {
                val transaction = supportFragmentManager.beginTransaction()
                transaction
                        .remove(currentFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                        .commit()

                for (button in buttons) { button.alpha = 0.5F }
                homeButton.alpha = 1F
            }

        }

        Log.d("MAIN_ACTIVITY", "Transcript: " + response.intentName)

    }

    private fun setOnclickListeners() {

        var buttons = arrayOf(homeButton,alertsButton,nutritionButton,scanButton,fitnessButton,helpButton,historyButton)

        homeButton.setOnClickListener {
            val transaction = supportFragmentManager.beginTransaction()
            transaction
                    .remove(currentFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                    .commit()

            for (button in buttons) { button.alpha = 0.5F }
            homeButton.alpha = 1F

        }

        historyButton.setOnClickListener {
            val transaction = supportFragmentManager.beginTransaction()
            currentFragment = HistoryFragment()
            transaction
                    .replace(R.id.fragmentFrame, currentFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()

            for (button in buttons) { button.alpha = 0.5F }
            historyButton.alpha = 1F
        }

        alertsButton.setOnClickListener {
            val transaction = supportFragmentManager.beginTransaction()
            currentFragment = AlertsFragment()
            transaction
                    .replace(R.id.fragmentFrame, currentFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()

            for (button in buttons) { button.alpha = 0.5F }
            alertsButton.alpha = 1F
        }

        nutritionButton.setOnClickListener {
            val transaction = supportFragmentManager.beginTransaction()
            currentFragment = NutritionFragment()
            transaction
                    .replace(R.id.fragmentFrame, currentFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()

            for (button in buttons) { button.alpha = 0.5F }
            nutritionButton.alpha = 1F
        }

        fitnessButton.setOnClickListener {
            val transaction = supportFragmentManager.beginTransaction()
            currentFragment = FitnessFragment()
            transaction
                    .replace(R.id.fragmentFrame, currentFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()

            for (button in buttons) { button.alpha = 0.5F }
            fitnessButton.alpha = 1F
        }

        scanButton.setOnClickListener {
            val transaction = supportFragmentManager.beginTransaction()
            currentFragment = ScanFragment()
            transaction
                    .replace(R.id.fragmentFrame, currentFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()

            for (button in buttons) { button.alpha = 0.5F }
            scanButton.alpha = 1F
        }

        helpButton.setOnClickListener {
            val transaction = supportFragmentManager.beginTransaction()
            currentFragment = HelpFragment()
            transaction
                    .replace(R.id.fragmentFrame, currentFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()

            for (button in buttons) { button.alpha = 0.5F }
            helpButton.alpha = 1F
        }

    }

    override fun onError(responseText: String, e: Exception) {
        Log.e("MAIN_ACTIVITY", "Error: $responseText", e)
    }


    private fun setFullScreenVisibility(){
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
    }



}
