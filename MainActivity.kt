package br.com.ps2ps4.converter

import android.app.Activity
import android.os.Bundle
import android.content.Intent
import android.net.Uri
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import java.text.DecimalFormat

class MainActivity : Activity() {
    private var isoUri: Uri? = null
    private lateinit var fileLabel: TextView
    private lateinit var logView: TextView
    private lateinit var statusView: TextView
    private lateinit var titleInput: EditText
    private lateinit var outputLabel: TextView
    private lateinit var profile: Spinner
    private lateinit var region: Spinner
    private lateinit var progress: ProgressBar

    private val pickIso = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        if (uri != null) {
            isoUri = uri
            try { contentResolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION) } catch (_: Exception) {}
            val name = uri.lastPathSegment ?: uri.toString()
            fileLabel.text = "Arquivo selecionado:\n$name"
            if (titleInput.text.isNullOrBlank()) titleInput.setText(name.substringAfterLast('/').substringBeforeLast('.'))
            log("Arquivo selecionado: $name")
        }
    }

    private val pickOutput = registerForActivityResult(ActivityResultContracts.OpenDocumentTree()) { uri ->
        if (uri != null) {
            outputLabel.text = "Pasta de saída selecionada:\n$uri"
            log("Pasta de saída selecionada.")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = Color.rgb(5, 13, 26)
        window.navigationBarColor = Color.rgb(5, 13, 26)
        buildUi()
        log("PS2 ISO → PS4 PKG Converter para Android iniciado.")
        log("Modo protótipo: interface e seleção de arquivos prontas.")
    }

    private fun buildUi() {
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(Color.rgb(5, 13, 26))
        }
        val scroll = ScrollView(this)
        val content = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(18), dp(18), dp(18), dp(24))
        }
        scroll.addView(content)
        root.addView(scroll, LinearLayout.LayoutParams(-1, 0, 1f))

        content.addView(label("🎮  PS2 ISO → PS4 PKG", 25, "#F1F6FF", true))
        content.addView(label("ANDROID 14 / 15  •  PROTÓTIPO v0.1.0", 12, "#70B5FF", true).apply {
            setPadding(0, dp(4), 0, dp(18))
        })

        panel(content, "1. ARQUIVO DE ORIGEM")
        fileLabel = label("Nenhuma ISO selecionada", 14, "#A9BED8", false)
        content.addView(fileLabel)
        content.addView(button("Selecionar ISO / BIN / IMG") {
            pickIso.launch(arrayOf("application/octet-stream", "application/x-iso9660-image", "*/*"))
        })

        panel(content, "2. DETALHES DO JOGO")
        titleInput = EditText(this).apply {
            hint = "Nome do jogo (opcional)"
            setTextColor(Color.WHITE); setHintTextColor(Color.GRAY)
            setSingleLine(true); setPadding(dp(12), dp(10), dp(12), dp(10))
            setBackgroundColor(Color.rgb(14, 29, 49))
        }
        content.addView(titleInput, matchWrap())
        content.addView(label("Perfil de preparação", 13, "#DDEAFF", true))
        profile = spinner(listOf("Equilibrado", "Rápido", "Priorizar compatibilidade"))
        content.addView(profile, matchWrap())
        content.addView(label("Região", 13, "#DDEAFF", true))
        region = spinner(listOf("Auto detectar", "NTSC-U", "NTSC-J", "PAL"))
        content.addView(region, matchWrap())

        panel(content, "3. SAÍDA")
        outputLabel = label("Pasta de saída: não selecionada", 13, "#A9BED8", false)
        content.addView(outputLabel)
        content.addView(button("Selecionar pasta de saída") { pickOutput.launch(null) })

        panel(content, "4. PROGRESSO")
        statusView = label("Aguardando seleção dos arquivos.", 14, "#DDEAFF", false)
        content.addView(statusView)
        progress = ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal).apply {
            max = 100; progress = 0
        }
        content.addView(progress, matchWrap())

        content.addView(button("Preparar conversão") {
            if (isoUri == null) {
                Toast.makeText(this, "Selecione primeiro uma imagem ISO.", Toast.LENGTH_LONG).show()
            } else {
                statusView.text = "Pipeline de conversão não configurado"
                log("Ação interrompida: este protótipo ainda não integra as ferramentas de conversão.")
                AlertDialogBuilder.show(this,
                    "Ferramentas necessárias",
                    "A interface Android está pronta para selecionar arquivos, mas não gera PKG real. O fluxo PS2 ISO → PS4 PKG exige ferramentas compatíveis de preparação/emulação e empacotamento, que normalmente são executadas em um computador. O Android não pode simplesmente converter a ISO renomeando o arquivo.")
            }
        })

        panel(content, "LOGS DO PROCESSO")
        logView = label("", 12, "#8DBEFF", false).apply {
            setPadding(dp(10), dp(10), dp(10), dp(10))
            setBackgroundColor(Color.rgb(3, 9, 18))
        }
        content.addView(logView, matchWrap())
        content.addView(button("Limpar logs") { logView.text = "" })

        content.addView(label(
            "Nota: app compatível como projeto-base para Android 14/15. A criação real de PKG requer integração com ferramentas externas; compatibilidade do resultado depende do jogo e do ambiente PS4.",
            12, "#8FA3BD", false
        ).apply { setPadding(0, dp(16), 0, 0) })

        setContentView(root)
    }

    private fun panel(parent: LinearLayout, text: String) {
        parent.addView(label(text, 13, "#70B5FF", true).apply {
            setPadding(0, dp(18), 0, dp(8))
        })
    }

    private fun label(text: String, size: Int, color: String, bold: Boolean): TextView =
        TextView(this).apply {
            this.text = text
            textSize = size.toFloat()
            setTextColor(Color.parseColor(color))
            if (bold) setTypeface(null, android.graphics.Typeface.BOLD)
            setPadding(0, dp(3), 0, dp(3))
        }

    private fun button(text: String, action: () -> Unit): Button =
        Button(this).apply {
            this.text = text
            isAllCaps = false
            setTextColor(Color.WHITE)
            setBackgroundColor(Color.rgb(25, 105, 225))
            setOnClickListener { action() }
            layoutParams = LinearLayout.LayoutParams(-1, dp(50)).apply {
                topMargin = dp(8); bottomMargin = dp(4)
            }
        }

    private fun spinner(items: List<String>): Spinner =
        Spinner(this).apply {
            adapter = ArrayAdapter(this@MainActivity, android.R.layout.simple_spinner_dropdown_item, items)
            setBackgroundColor(Color.rgb(14, 29, 49))
        }

    private fun matchWrap() = LinearLayout.LayoutParams(-1, -2).apply {
        topMargin = dp(5); bottomMargin = dp(6)
    }

    private fun dp(value: Int): Int = (value * resources.displayMetrics.density).toInt()

    private fun log(message: String) {
        if (!::logView.isInitialized) return
        val time = java.text.SimpleDateFormat("HH:mm:ss", java.util.Locale.getDefault()).format(java.util.Date())
        logView.append("[$time] $message\n")
    }
}

private object AlertDialogBuilder {
    fun show(activity: Activity, title: String, message: String) {
        android.app.AlertDialog.Builder(activity)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Entendi", null)
            .show()
    }
}
