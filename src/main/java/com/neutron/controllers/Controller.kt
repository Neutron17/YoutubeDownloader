package com.neutron.controllers

import com.neutron.Main
import com.neutron.Main.err
import com.neutron.Shared
import com.neutron.Writer
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.fxml.Initializable
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.scene.text.FontWeight
import javafx.scene.text.Text
import javafx.stage.DirectoryChooser
import javafx.stage.FileChooser
import javafx.stage.Stage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.javafx.JavaFx
import kotlinx.coroutines.launch
import java.awt.*
import java.awt.TrayIcon.MessageType
import java.io.BufferedReader
import java.io.File
import java.io.FileNotFoundException
import java.io.InputStreamReader
import java.net.URI
import java.net.URL
import java.util.*

class Controller : Initializable {
    private var status = ""
    private var finished = false
    private var type = ""
    private var format = ToggleGroup()
    private var resizeable = ToggleGroup()
    private var listFormat = ToggleGroup()
    private var darkMode = ToggleGroup()
    @Volatile
    private var isPathChanged = false
    private var pathToYTdlEXE = "youtube-dl"//"/youtube-dl.exe"
        set(value) {
            isPathChanged = true
            field = value
        }
    private val home:String = System.getProperty("user.home")
    private val fxScope:CoroutineScope = CoroutineScope(Job() + Dispatchers.JavaFx)

    @FXML var path = TextField("$home/Downloads")
    @FXML var link = TextField()
    @FXML var errPane = Pane()
    @FXML var mainPane = Pane()
    @FXML var historyPane = SplitPane()
    @FXML var errLabel = Label()
    @FXML var outputLabel = Label()
    @FXML var mainBgPicker = ColorPicker()
    @FXML var historyBgPicker = ColorPicker()
    @FXML var settingsBgPicker = ColorPicker()
    @FXML var settingsPane = Pane()
    @FXML var surePane = Pane()
    @FXML var table: TableView<*> = TableView<Any?>()
    @FXML var mp4 = RadioButton()
    @FXML var mp3 = RadioButton()
    @FXML var vidBest = RadioButton()
    @FXML var audioBest = RadioButton()
    @FXML var listVid = RadioButton()
    @FXML var listAudio = RadioButton()
    @FXML var listAudioAny = ToggleButton() //
    @FXML var listVideoMp4 = ToggleButton()
    @FXML var toggleNem = ToggleButton()
    @FXML var toggleIgen = ToggleButton()
    @FXML var ytdlpath = TextField(pathToYTdlEXE)
    @FXML var downloadButton = Button()
    @FXML var settingsTab = Tab()
    @FXML var mainTab = Tab()
    @FXML var historyTab = Tab()
    @FXML var playlistLink = TextField()
    @FXML var listButton = Button()
    @FXML var plistLabel = Label()
    @FXML var vidLabel = Label()
    @FXML var audioLabel = Label()
    @FXML var darkOn = ToggleButton()
    @FXML var darkOff = ToggleButton()

    @FXML
    fun handleBrowse() {
        println("handleBrowse")
        val dirChooser = DirectoryChooser()
        dirChooser.title = "Cél mappa tallózása"
        val file = dirChooser.showDialog(Main.stage)
        if (file != null) {
            path.text = file.toString()
        }
    }

    @FXML
    fun convertAct() {
        val array = format.selectedToggle.toString().split("'".toRegex()).toTypedArray()
        println("convertAct")
        if (link.text.isEmpty()) {
            errPaner("Nem lehet üres")
        } else if (!link.text.contains("https://www.youtube.com/watch")) {
            if (link.text.startsWith("youtube.com/watch") || link.text.startsWith("www.youtube.com/watch")) {
                link.text = "https://" + link.text
                cmd(array[1])
            } else errPaner("Ez nem egy youtube videó linkje!")
        } else {
            Writer.withoutOverwrite(link.text + ";", "/path.txt") // TODO
            println(format.selectedToggle)
            cmd(array[1])
        }
    }

    @FXML
    fun  convertList() {
        println("convertList ${listFormat.selectedToggle.toString().split("'".toRegex()).toTypedArray()[1]}")
        val array = listFormat.selectedToggle.toString().split("'".toRegex()).toTypedArray()
        if (playlistLink.text.isEmpty()) {
            errPaner("Nem lehet üres")
        } else if (playlistLink.text.contains("youtube.com/playlist?list=")) {
            when(true) {
                playlistLink.text.startsWith("https://youtube.com/playlist?list="),playlistLink.text.startsWith("https://www.youtube.com/playlist?list=") -> {
                    println("https")
                    cmd(array[1])
                    return
                }
                playlistLink.text.startsWith("youtube.com/playlist?list=") || playlistLink.text.startsWith("www.youtube.com/playlist?list=") -> {
                    println("www ˇ| you")
                    println("if")
                    playlistLink.text = "https://" + playlistLink.text
                    cmd(array[1])
                    return
                }
                else -> {
                    errPaner("Ez nem egy youtube lejátszási lista neve")
                    return
                }
            }
        }
    }

    @FXML fun restoreSetting() {
        val wind = window("Reset settings", false, 400.0, 200.0, "/reset.fxml")
        Main.s = Shared(this, wind)
        wind.show()
    }
    private fun errPaner(message: String?) {
        val stage = Stage()
        val txt = Label(message) // TODO Make text bigger
        txt.font = Font.font("System Bold", FontWeight.BOLD, 25.0)
        txt.style += "-fx-text-fill : red;"
        stage.scene = Scene(Pane(txt))
        stage.show()
    }

    @FXML
    fun save() {
        Writer.bufferedWriter(mainBgPicker.value.toString(), "/mainBg.txt")
        Writer.bufferedWriter(historyBgPicker.value.toString(), "/historyBg.txt")
        Writer.bufferedWriter(settingsBgPicker.value.toString(), "/settingsBg.txt")
        System.err.println("Main: ${read("mainBg")}\nHistory: ${read("historyBg")}Settings: ${read("settingsBg")}")
        loadSettings()
    }

    private fun findYtDl() {
        val f = File("$home/Documents")
        val matchingFiles = f.listFiles { _: File?, name: String -> name.startsWith("youtube-dl") && name.endsWith("exe") }
        for (i in matchingFiles!!) {
            println(i.toString())
        }
    }
    @FXML
    fun browseYTdlEXE() {
        val fileChooser = FileChooser()
        fileChooser.initialFileName = "youtube-dl.exe"
        fileChooser.title = "youtube-dl.exe tallózása"
        fileChooser.initialFileName = "youtube-dl.exe"
        fileChooser.extensionFilters.addAll(
            FileChooser.ExtensionFilter("Végrehajtható fájlok", "*.exe"),
            FileChooser.ExtensionFilter("Más fájlok", "*.*")
        )
        val file = fileChooser.showOpenDialog(Main.stage)
        if (file != null) {
            ytdlpath.text = file.toString()
        }
    }
    @FXML
    fun setResizeableT() { Main.stage.isResizable = true }
    @FXML
    fun setResizeableF() { Main.stage.isResizable = false }
    @FXML
    fun handleDownload() {
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            Desktop.getDesktop().browse(URI("https://youtube-dl.org"));
        }
    }

    private fun cmd(type: String) { // TODO Make it work for non windows systems
        println("cmd")
        println(path.toString() + "\t" + path.text)
        println(link.toString() + "\t" + path.text)
        println(pathToYTdlEXE + " -o " + path.text + " " + link.text)
        val t1 = Thread {
            this.type = type
            println("Type: $type\nLink: ${link.text}\nPath: ${path.text}")
            try {
                System.err.println(type)
                println("Type: $type\nLink: ${link.text}\nPath: ${path.text}\nOutput Label: $outputLabel")
                val builder = ProcessBuilder()
                builder.environment()["LANG"] = "hu_HU.UTF-8"
                when (type) {
                    "mp4" -> {
                        println("mp4")
                        builder.command(
                            "cmd.exe",
                            "/c",
                            "chcp 65001 && $pathToYTdlEXE -f mp4 -o ${path.text}/%(title)s.%(ext)s  " + link.text
                        )
                        //}
                    }
                    "mp3" -> {
                        println("mp3")
                        println(pathToYTdlEXE + " -x --audio-format mp3 -o ${path.text}/%(title)s.%(ext)s " + link.text)
                        builder.command(
                            "cmd.exe",
                            "/c",
                            "chcp 65001 && $pathToYTdlEXE -x --audio-format mp3 -o ${path.text}/%(title)s.%(ext)s " + link.text
                        )
                        builder.directory(File("$home/Downloads"))
                    }
                    "legjobb videó" -> {
                        println("vidBest")
                        builder.command(
                            "cmd.exe",
                            "/c",
                            "chcp 65001 && $pathToYTdlEXE -f best -o ${path.text}/%(title)s.%(ext)s " + link.text
                        )
                    }
                    "legjobb hang" -> {
                        println("webmVid")
                        builder.command(
                            "cmd.exe",
                            "/c",
                            "chcp 65001 && $pathToYTdlEXE -x -f best -o ${path.text}/%(title)s.%(ext)s " + link.text
                        )
                    }
                    "Videó" -> {
                        println("Videó")
                        builder.command(
                            "cmd.exe",
                            "/c",
                            "chcp 65001 && $pathToYTdlEXE -o ${path.text}/%(title)s.%(ext)s ${playlistLink.text}"
                        )
                    }
                    "Videó(mp4)" -> {
                        println("Videó")
                        builder.command(
                            "cmd.exe",
                            "/c",
                            "chcp 65001 && $pathToYTdlEXE -f mp4 -o ${path.text}/%(title)s.%(ext)s ${playlistLink.text}"
                        )
                    }
                    "Hang" -> {
                        println("Hang")
                        builder.command(
                            "cmd.exe",
                            "/c",
                            "chcp 65001 && $pathToYTdlEXE --extract-audio -o ${path.text}/%(title)s.%(ext)s ${playlistLink.text}"
                        )
                    }
                    "Hang(mp3)" -> {
                        println("Hang")
                        builder.command(
                            "cmd.exe",
                            "/c",
                            "chcp 65001 && $pathToYTdlEXE --extract-audio --audio-format mp3 -o ${path.text}/%(title)s.%(ext)s ${playlistLink.text}"
                        )
                    }

                    else -> {
                        println("ELSE")
                    }
                }
                status = "A letöltés elkezdődött..."
                println("Builder: " + builder + "\tCommand: " + builder.command())
                var line: String?
                var process: Process? = null
                try {
                    process = builder.start()
                    val r = BufferedReader(InputStreamReader(process.inputStream))
                    while (true) {
                        line = r.readLine()
                        if (line == null) {
                            break
                        }
                        status = line
                        println(line)
                        fxScope.launch {
                            outputLabel.text = line
                        }
                    }
                    //fxScope.launch { outputLabel.text = "A letöltés befejeződött" }
                    if (SystemTray.isSupported()) {
                        val td = Controller()
                        td.displayTray()
                    } else {
                        System.err.println("System tray not supported!")
                    }
                } catch (e: Exception) {
                    println("Inner")
                    e.printStackTrace()
                    finished = true
                }
            } catch (ex: Exception) {
                println("Outer")
                ex.printStackTrace()
                finished = true
            }
            finished = true
            fxScope.launch {
                outputLabel.text = "Letöltés befejeződött"
            }
        }
        t1.start()
    }

    private fun setToggles() {
        mp3.toggleGroup = format
        mp4.toggleGroup = format
        vidBest.toggleGroup = format
        audioBest.toggleGroup = format

        toggleIgen.toggleGroup = resizeable
        toggleNem.toggleGroup = resizeable

        listVid.toggleGroup = listFormat
        listAudio.toggleGroup = listFormat
        listAudioAny.toggleGroup = listFormat
        listVideoMp4.toggleGroup = listFormat

        darkOff.toggleGroup = darkMode
        darkOn.toggleGroup = darkMode
        darkOff.isSelected = true
        listVid.isSelected = true
        toggleNem.isSelected = true
        mp4.isSelected = true
    }

    fun loadSettings() {
        try {
            val main = read("mainBg")
            val history = read("historyBg")
            val settings = read("settingsBg")
            mainBgPicker.value = Color.valueOf(main)
            historyBgPicker.value = Color.valueOf(history)
            settingsBgPicker.value = Color.valueOf(settings)
            mainTab.style = "-fx-background-color: #${main.split("x")[1]};"
            historyTab.style = "-fx-background-color: #${history.split("x")[1]};"
            settingsTab.style = "-fx-background-color: #" + settings.split("x")[1] + ";"
            mainPane.style = "-fx-background-color: #" + main.split("x")[1] + ";"
            historyPane.style = "-fx-background-color: #" + history.split("x")[1] + ";"
            settingsPane.style = "-fx-background-color: #" + settings.split("x")[1] + ";"
        }catch(th: Throwable) {
            th.printStackTrace()
            err(th)
        }
    }

    private fun defaultVisibility() {
        mainPane.isVisible = true
        mainPane.opacity = 1.0
        mainPane.isDisable = false
        errPane.isVisible = false
        settingsPane.isVisible = true
        settingsPane.isDisable = false
        surePane.isVisible = false
        settingsPane.opacity = 1.0
    }

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        println("Initialize")
        val downloadView = ImageView(Image("/download.png"))
        downloadView.fitHeight = 25.0
        downloadView.fitWidth = 25.0
        downloadButton.graphic = downloadView
        ytdlpath.text = "/youtube-dl.exe"
        defaultVisibility()
        findYtDl()
        setToggles()
        loadSettings()
        path.text = "$home/Downloads"
    }

    private fun read(name: String): String {
        println("read")
        try {
            val file = File(this::class.java.getResource("/$name.txt")!!.file)
            val sc = Scanner(file)
            while (sc.hasNextLine()) {
                val data = sc.nextLine()
                println(data)
                return data
            }
            sc.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        return ""
    }

    @Throws(AWTException::class)
    fun displayTray() {
        //Obtain only one instance of the SystemTray object
        val tray = SystemTray.getSystemTray()

        //If the icon is a file
        val image: java.awt.Image? = Toolkit.getDefaultToolkit().createImage("/youtube2.png")
        //Alternative (if the icon is on the classpath):
        //Image image = Toolkit.getDefaultToolkit().createImage(getClass().getResource("icon.png"));
        val trayIcon = TrayIcon(image, "Tray Demo")
        //Let the system resize the image if needed
        trayIcon.isImageAutoSize = true
        //Set tooltip text for the tray icon
        trayIcon.toolTip = "System tray icon demo"
        tray.add(trayIcon)
        trayIcon.displayMessage("Youtube videó letöltő", "A letöltés befejeződött", MessageType.INFO)
    }

    companion object {
        var workDir: String = System.getProperty("user.dir")!!
    }
    private fun window(title: String = "", isResizable: Boolean = false, width: Double = 400.0, height: Double = 200.0): Stage {
        val stage = Stage()
        stage.title = title
        stage.isResizable = isResizable
        stage.width = width
        stage.height = height
        return stage
    }
    private fun window(title: String = "", isResizable: Boolean = false, width: Double = 400.0, height: Double = 200.0, fxml: String): Stage {
        val stage = window(title, isResizable, width, height)
        val root = FXMLLoader.load<Parent>(Objects.requireNonNull(javaClass.getResource(fxml)))
        stage.scene = Scene(root, width, height)
        return stage;
    }
}