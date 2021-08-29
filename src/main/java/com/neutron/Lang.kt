package com.neutron

import kotlinx.serialization.Serializable

@Serializable
open class Lang(
    val title: String,
    val dirTitle: String,
    val cannotBeEmpty: String,
    val notYTVLink: String,
    val notYTPLLink: String,
    val resetSettingsTitle: String,
    val browseYTDL: String,
    val executableFiles: String,
    val otherFiles: String,
    val downloadStarted: String,
    val downloadEnded: String

)
sealed class Langs {
    object HU : Lang("Youtube videó letöltő",
        "Cél mappa tallózása", "Nem lehet üres",
        "Ez nem egy youtube videó linkje!",
        "Ez nem egy youtube lejátszási lista neve",
        "youtube-dl.exe tallózása", "Youtube-dl.exe tallózása",
        "Végrehajtható fájlok", "Más fájlok",
        "A letöltés elkezdődött...",
        "Letöltés befejeződött"
    )
    object EN: Lang("Youtube video downloader","Browse target directory",
        "Cannot be empty","This is not a valid youtube video link",
        "This is not a valid youtube playlist link",
        "Reset settings","Browse Youtube-dl",
        "Executable files","Other files",
        "Download started...","Download finished")
}