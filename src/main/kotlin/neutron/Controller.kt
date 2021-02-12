package neutron

import com.sun.deploy.uitoolkit.impl.fx.HostServicesFactory
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.*
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import javafx.stage.DirectoryChooser
import javafx.stage.FileChooser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.javafx.JavaFx
import kotlinx.coroutines.launch
import java.awt.AWTException
import java.awt.SystemTray
import java.awt.Toolkit
import java.awt.TrayIcon
import java.awt.TrayIcon.MessageType
import java.io.*
import java.net.URL
import java.util.*

/** Controller class for sample-en.fxml and sample-hu.fxml
 *  @author Neutron17 */
class Controller : Initializable {
    var workDir =  System.getProperty("user.dir")
    var status = ""
    var finished = false
    var type = ""
    var format = ToggleGroup()
    var resizeable = ToggleGroup()
    var listFormat = ToggleGroup()
    var darkMode = ToggleGroup()
    var pathToYTdlEXE = "$workDir\\src\\main\\resources\\assets\\youtube-dl.exe"
    val home:String = System.getProperty("user.home")
    val fxScope:CoroutineScope = CoroutineScope(Job() + Dispatchers.JavaFx)

    // Importing elements from fxml
    @FXML var path = TextField("$home\\Downloads")
    @FXML var link = TextField()
    @FXML var errPane = Pane()
    @FXML var mainPane = Pane()
    @FXML var errLabel = Label()
    @FXML var outputLabel = Label()
    @FXML var mainBgPicker = ColorPicker()
    @FXML var settingsBgPicker = ColorPicker()
    @FXML var settingsPane = Pane()
    @FXML var surePane = Pane()
    @FXML var mp4 = RadioButton()
    @FXML var mp3 = RadioButton()
    @FXML var vidBest = RadioButton()
    @FXML var audioBest = RadioButton()
    @FXML var listVid = RadioButton()
    @FXML var listAudio = RadioButton()
    @FXML var listAudioAny = ToggleButton()
    @FXML var listVideoMp4 = ToggleButton()
    @FXML var toggleNem = ToggleButton()
    @FXML var toggleIgen = ToggleButton()
    @FXML var ytdlpath = TextField(pathToYTdlEXE)
    @FXML var downloadButton = Button()
    @FXML var settingsTab = Tab()
    @FXML var mainTab = Tab()
    @FXML var playlistLink = TextField()
    @FXML var listButton = Button()
    @FXML var plistLabel = Label()
    @FXML var vidLabel = Label()
    @FXML var audioLabel = Label()
    @FXML var darkOn = ToggleButton()
    @FXML var darkOff = ToggleButton()

    /** Opens file explorer to browse the output directory */
    @FXML fun handleBrowse() {
        println("handleBrowse")
        val dirChooser = DirectoryChooser()
        dirChooser.title = when(Lang.isEnglish) { false -> "Cél mappa tallózása" else -> "Browse output directory" }
        val file = dirChooser.showDialog(Main.stage)
        if (file != null) {
            path.text = file.toString()
        }
    }
    /** Checks link and passes link to cmd() to download */
    @FXML fun convertAct() {
        val array = format.selectedToggle.toString().split("'".toRegex()).toTypedArray()
        println("convertAct")
        if (link.text.isEmpty()) {
            errPaner(when(Lang.isEnglish) {
                false -> "Nem lehet üres"
                else -> "Cannot be empty"})
        } else if (!link.text.contains("https://www.youtube.com/watch")) {
            if (link.text.startsWith("youtube.com/watch") || link.text.startsWith("www.youtube.com/watch")) {
                link.text = "https://" + link.text
                cmd(array[1])
            } else errPaner(when(Lang.isEnglish) {
                false -> "Ez nem egy youtube videó linkje!"
                else -> "This isn't a youtube video's link"})
        } else {
            Writer.withoutOverwrite(link.text + ";", "$workDir\\assets\\path.txt")
            println(format.selectedToggle)
            cmd(array[1])
        }
    }
    /** Checks playlist link and passes the link to cmd() to download */
    @FXML fun convertList() {
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
    /** idc */
    @FXML fun handleOk() {
        errPane.isVisible = false
        playlistLink.isVisible = true
        listButton.isVisible = true
        plistLabel.isVisible = true
        mp3.isVisible = true
        mp4.isVisible = true
        vidBest.isVisible = true
        audioBest.isVisible = true
        listVid.isVisible = true
        audioLabel.isVisible = true
        vidLabel.isVisible = true
        mainPane.opacity = 1.0
    }
    @FXML fun restoreSettingA() { surePane.isVisible = true } // TODO Make background invisible
    /** Does not restore default settings */
    @FXML fun restoreSettingNo() { surePane.isVisible = false }
    /** Restores default settings */
    @FXML fun restoreSettingYes() {
        Writer.bufferedWriter("0xffffffff", "$workDir\\src\\main\\resources\\assets\\mainBg.txt")
        Writer.bufferedWriter("0xffffffff", "$workDir\\src\\main\\resources\\assets\\historyBg.txt")
        Writer.bufferedWriter("0xffffffff", "$workDir\\src\\main\\resources\\assets\\settingsBg.txt")
        surePane.isVisible = false
        loadSetts()
    }
    /** Writes background to files, cause no database 😓 */
    @FXML fun save() {
        Writer.bufferedWriter(mainBgPicker.value.toString(), "$workDir\\src\\main\\resources\\assets\\mainBg.txt")
        Writer.bufferedWriter(settingsBgPicker.value.toString(),
            "$workDir\\src\\main\\resources\\assets\\settingsBg.txt")
        loadSetts()
    }
    /** Brows youtube-dl.exe */
    @FXML fun browseYTdlEXE() {
        val fileChooser = FileChooser()
        fileChooser.initialFileName = "youtube-dl.exe"
        fileChooser.title = when(Lang.isEnglish) { false -> "youtube-dl.exe tallózása" else -> "browse youtube-dl.exe"}
        fileChooser.initialFileName = "youtube-dl.exe"
        fileChooser.extensionFilters.addAll(
            FileChooser.ExtensionFilter(
                when(Lang.isEnglish) { false -> "Végrehajtható fájlok"
                    else -> "Executable files"}, "*.exe"),
            FileChooser.ExtensionFilter(when(Lang.isEnglish) { false -> "Más fájlok" else -> "Other files"}, "*.*")
        )
        val file = fileChooser.showOpenDialog(Main.stage)
        if (file != null) {
            ytdlpath.text = file.toString()
        }
    }
    /** Makes window resizable */
    @FXML fun setResizeableT() { Main.stage.isResizable = true }
    /** Makes window not resizable */
    @FXML fun setResizeableF() { Main.stage.isResizable = false }
    /** Opens https://youtube-dl.org/ in default browser */
    @FXML fun handleDownload() {
        val hostServices = HostServicesFactory.getInstance(neutron.Main())
        hostServices.showDocument("https://youtube-dl.org/")
    }
    /** Makes errPane visible and the background invisible
     *  @param message Error message */
    private fun errPaner(message: String?) {
        errPane.isVisible = true
        playlistLink.isVisible = false
        listButton.isVisible = false
        plistLabel.isVisible = false
        mp3.isVisible = false
        mp4.isVisible = false
        vidBest.isVisible = false
        audioBest.isVisible = false
        listVid.isVisible = false
        audioLabel.isVisible = false
        vidLabel.isVisible = false
        errLabel.text = message
    }
    /** Searches for youtube-dl.exe */
    private fun findYtDl() {
        val f = File("$home\\Documents")
        val matchingFiles = f.listFiles { _: File?, name: String -> name.startsWith("youtube-dl") && name.endsWith("exe") }
        for (i in matchingFiles!!) {
            println(i.toString())
        }
    } // TODO Implement
    /** Downloads video/audio/playlist */
    private fun cmd(type: String) {
        val t1 = Thread {
            this.type = type
            try {
                val builder = ProcessBuilder()
                when (type) { // TODO Make radio buttons compatible with english
                    "mp4" -> {
                        println("mp4")
                        builder.command(
                            "cmd.exe",
                            "/c",
                            "$pathToYTdlEXE -f mp4 -o ${path.text}\\%(title)s.%(ext)s  ${link.text}"
                        )
                    }
                    "mp3" -> {
                        println("$pathToYTdlEXE -x --audio-format mp3 -o ${path.text}\\%(title)s.%(ext)s ${link.text}")
                        builder.command(
                            "cmd.exe",
                            "/c",
                            "$pathToYTdlEXE -x --audio-format mp3 -o ${path.text}\\%(title)s.%(ext)s ${link.text}"
                        )
                        builder.directory(File("$home\\Downloads"))
                    }
                    "legjobb videó" -> {
                        builder.command(
                            "cmd.exe",
                            "/c",
                            "$pathToYTdlEXE -f best -o ${path.text}\\%(title)s.%(ext)s ${playlistLink.text}"
                        )
                    }
                    "legjobb hang" -> {
                        builder.command(
                            "cmd.exe",
                            "/c",
                            "$pathToYTdlEXE -x -f best -o ${path.text}\\%(title)s.%(ext)s ${playlistLink.text}"
                        )
                    }
                    "Videó" -> {
                        builder.command(
                            "cmd.exe",
                            "/c",
                            "$pathToYTdlEXE -o ${path.text}\\%(title)s.%(ext)s ${playlistLink.text}"
                        )
                    }
                    "Videó(mp4)" -> {
                        builder.command(
                            "cmd.exe",
                            "/c",
                            "$pathToYTdlEXE -f mp4 -o ${path.text}\\%(title)s.%(ext)s ${playlistLink.text}"
                        )
                    }
                    "Hang" -> {
                        builder.command(
                            "cmd.exe",
                            "/c",
                            "$pathToYTdlEXE --extract-audio -o ${path.text}\\%(title)s.%(ext)s ${playlistLink.text}"
                        )
                    }
                    "Hang(mp3)" -> {
                        println("Hang")
                        builder.command(
                            "cmd.exe",
                            "/c",
                            "$pathToYTdlEXE --extract-audio --audio-format mp3 -o ${path.text}\\%(title)s.%(ext)s ${playlistLink.text}"
                        )
                    }
                    else -> {
                        System.err.println("ELSE")
                    }
                }
                status = when (Lang.isEnglish) {
                    false -> "A letöltés elkezdődött..."
                    true -> "The downloading has began..."
                }
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
                    if (SystemTray.isSupported()) {
                        val td = Controller()
                        td.notification()
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
                outputLabel.text = when (Lang.isEnglish) {
                    false -> "A letöltés befejeződött"
                    true -> "The downloading has ended"
                }
            }
        }
        t1.start()
    }
    /** Sets default toggles */
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
    /** Loads preferences from files */
    private fun loadSetts() {
        mainBgPicker.value = Color.valueOf(read("mainBg"))
        settingsBgPicker.value = Color.valueOf(read("settingsBg"))
        mainTab.style = "-fx-background-color: #" + read("mainBg").split("x")[1] + ";"
        settingsTab.style = "-fx-background-color: #" + read("settingsBg").split("x")[1] + ";"
        mainPane.style = "-fx-background-color: #" + read("mainBg").split("x")[1] + ";"
        settingsPane.style = "-fx-background-color: #" + read("settingsBg").split("x")[1] + ";"
        if (darkMode.selectedToggle == darkOn) { // TODO Implement dark mode
            println("dark")
            Main.scene.stylesheets.add("/foo.css")
            val bg:String = "-fx-background-color: #ffffffff;"
            mainTab.style = bg
            settingsTab.style = bg
            mainPane.style = bg
            settingsPane.style = bg
        }
    }
    /** Restores the default visibility */
    fun defaultVisibility() {
        mainPane.isVisible = true
        mainPane.opacity = 1.0
        mainPane.isDisable = false
        errPane.isVisible = false
        settingsPane.isVisible = true
        settingsPane.isDisable = false
        surePane.isVisible = false
        settingsPane.opacity = 1.0
    }
    /** Reads from file in workDir/src\main/resources/assets
     *  @param name filename in workDir/src/main/resources/assets */
    fun read(name: String): String {
        println("read")
        try {
            val file = File("$workDir/src/main/resources/assets/$name.txt")
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
    /** Creates a notification using awt */
    @Throws(AWTException::class) fun notification() {
        val tray = SystemTray.getSystemTray()
        val image: java.awt.Image? = Toolkit.getDefaultToolkit().createImage("/youtube2.png")
        val trayIcon = TrayIcon(image, "Tray Demo")
        trayIcon.isImageAutoSize = true
        trayIcon.toolTip = "System tray icon demo"
        tray.add(trayIcon)
        trayIcon.displayMessage(
            when(Lang.isEnglish){ false -> "Youtube videó letöltő" else -> "Youtube video downloader"},
            when(Lang.isEnglish){ false -> "A letöltés befejeződött" else -> "The downloading has ended"}, MessageType.INFO)
    }
    /** Gets called the first time */
    override fun initialize(location: URL?, resources: ResourceBundle?) {
        println("Initialize")
        val downloadView = ImageView(Image("/download.png"))
        downloadView.fitHeight = 25.0
        downloadView.fitWidth = 25.0
        downloadButton.graphic = downloadView
        ytdlpath.text = "$workDir\\src\\main\\resources\\assets\\youtube-dl.exe"
        defaultVisibility()
        findYtDl()
        setToggles()
        loadSetts()
        path.text = "$home\\Downloads"
    }
}
/** Contains functions for writing to files */
internal object Writer {
    /** Writes to file with buffers
     *  @see withoutOverwrite Write to file without overwriting it's content
     *  @param text Text to write into the file
     *  @param path Path to the file */
    fun bufferedWriter(text: String, path: String?) {
        try {
            val fos = FileOutputStream(path)
            val bout = BufferedOutputStream(fos)
            val a = text.toByteArray()
            bout.write(a)
            bout.flush()
            bout.close()
        } catch (ex: IOException) {
            System.err.println("Error in bufferedWriter")
            ex.printStackTrace()
        }
    }
    /** Writes to file without overwriting it's content using java.io.FileWriter
     *  @see bufferedWriter Write to file with overwriting it's content
     *  @param text Text to write into the file
     *  @param path Path to the file */
    fun withoutOverwrite(text: String?, path: String?) {
        val log = File(path)
        try {
            if (!log.exists()) {
                println("We had to make a new file.")
                log.createNewFile()
            }
            val fileWriter = FileWriter(log, true)
            val bufferedWriter = BufferedWriter(fileWriter)
            bufferedWriter.write(text)
            bufferedWriter.close()
            println("Done")
        } catch (e: IOException) {
            println("COULD NOT LOG!!")
        }
    }
}