# Lösung: [Titel der Challenge]

## Benötigte Tools
* [z.B. jadx-gui]
* [z.B. adb]

## Schritt-für-Schritt Lösung

### Schritt 1: Analyse
Wir öffnen die APK in Tool XY. In der Klasse `MainActivity` sehen wir folgenden Code:
*(Code-Snippet)*

### Schritt 2: Exploitation
Das Passwort wird im Klartext verglichen. Wir sehen den String "SuperSecret".

### Schritt 3: Flag erhalten
Eingabe des Passworts in der App gibt das Flag aus.
Alternativ via Shell:
`grep -r "pwn{" .`

**Flag:** `pwn{this_is_the_solution_flag}`
