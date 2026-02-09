# Challenge: Logik-Umkehr

**Schwierigkeit:** ⭐⭐⭐☆☆
**Kategorie:** Statische Analyse und Patching

## Szenario
*Die Entwickler dieser App sind überzeugt, ihr Login sei absolut sicher.
Benutzername und Passwort werden mit moderner Kryptographie geprüft – ein direkter Angriff scheint aussichtslos.*

*Aber Sicherheit steht und fällt nicht mit Kryptographie allein.
Irgendwo muss entschieden werden, ob Erfolg oder Misserfolg eintritt.*

## Deine Aufgabe
1.  Installiere die App `app-release.apk`.
2.  Analysiere die App statisch mit geeigneten Reverse-Engineering-Tools.
3.  Manipuliere die App so, dass ein beliebiger Login akzeptiert wird. 
4.  Erhalte das Flag, das bei einem erfolgreichen Login ausgegeben wird.

## Hinweise (Optional)
* Tipp 1: Achte auf Stellen im Code, an denen zwischen Login erfolgreich und Login fehlgeschlagen unterschieden wird.
* Tipp 2: Nicht die Überprüfung selbst ist entscheidend, sondern die Bedingung, die danach ausgewertet wird.
* Tipp 3: Eine sehr kleine Änderung im Smali-Code kann ausreichen, um den Kontrollfluss zu verändern.
