# Lösung: Logik-Umkehr

## Benötigte Tools
* jadx-gui (statische Analyse)
* apktool (Dekompilieren & Neubauen)
* apksigner (Signieren der APK)
* adb (Installieren & Logcat)

## Schritt-für-Schritt Lösung

### Schritt 1: Analyse mit jadx-gui
Zuerst wird die APK in jadx-gui geöffnet.

Beim Durchsuchen des Codes (z. B. über die Textsuche nach login, verify oder Log-Strings) fällt eine Methode auf, die für die Login-Überprüfung verantwortlich ist:

`checkLogin(user, pass, context, viewModel)`

Der dekompilierte Code zeigt eine klassische Verzweigung:


`if (isCorrectUser(user) && verifyPassword(pass)) {
    Log.d("LicenseManager", "BYPASS_DETECTED_SUCCESS");
    viewModel.requestFlag();
} else {
    viewModel.sendSnackbarMessage("Access denied.");
}`

Beobachtung:
Die kryptographische Überprüfung selbst ist nicht trivial umgehbar.
Der Erfolg hängt ausschließlich von einer booleschen Bedingung ab.

### Schritt 2: Übergang von Analyse zu Smali
jadx-gui dient ausschließlich der Analyse und erlaubt keine Codeänderungen.
Zur Modifikation wird die APK mit apktool dekompiliert:

`apktool d app-release.apk -o app_dec`

Anhand des in jadx ermittelten Paket- und Klassennamens

`de.hhn.dojo.dummy.ui.screens.MainScreenKt`

wird die entsprechende Smali-Datei lokalisiert:

`app_dec/smali/de/hhn/dojo/dummy/ui/screens/MainScreenKt.smali`


### Schritt 3: Analyse des Smali-Codes
In der Smali-Datei findet sich folgender relevanter Abschnitt:

`invoke-static {p0}, ...->isCorrectUser(Ljava/lang/String;)Z
move-result v0
if-eqz v0, :cond_fail`

`invoke-static {p1}, ...->verifyPassword(Ljava/lang/String;)Z
move-result v0
if-eqz v0, :cond_fail`

Bedeutung:
* Liefert einer der beiden Checks false, wird zum Fehlerpfad gesprungen.

* Nur wenn beide Prüfungen true ergeben, wird der Erfolgspfad ausgeführt.

### Schritt 4: Manipulation der Entscheidungslogik
Zur Umgehung des Logins werden die Sprungbedingungen invertiert, z. B.: 

`if-eqz v0, :cond_fail`

wird geändert zu: 

`if-nez v0, :cond_fail`

Auswirkung:
Der Kontrollfluss erreicht den Erfolgspfad auch bei falschen Login-Daten.

### Schritt 5: Neubau und Signierung der APK
Nach der Modifikation wird die APK neu gebaut:

`apktool b app_dec -o patched.apk`

Anschließend wird sie mit dem Debug-Keystore signiert:

`apksigner sign --ks %USERPROFILE%\.android\debug.keystore --ks-key-alias androiddebugkey --ks-pass pass:android patched.apk`

Installation auf dem Gerät:

`adb install -r patched.apk`

### Schritt 6: Flag-Erhalt
Nach der Installation wird ein beliebiger Benutzername und ein beliebiges Passwort eingegeben.
Der Login wird nun als erfolgreich gewertet, wodurch das Flag ausgegeben wird.

`D/LicenseManager: BYPASS_DETECTED_SUCCESS`

**Flag:** `pwn{logic_beats_crypto}`
