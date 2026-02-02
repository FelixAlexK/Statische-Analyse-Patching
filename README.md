# Gruppen-Manifest: Code Anatomy – Statische Analyse & Patching


**Mitglieder:**
* Ghinea, Iulia Maria (209029)
* Widmann, Simon (216071) 
* Kuhbier, Felix (212115)

## Der Rote Faden
*Die drei Challenges verfolgen das gemeinsame Lernziel, Studierende schrittweise an die statische Analyse und gezieltes Patching von Android-APKs heranzuführen. In der ersten Challenge lernen sie die grundlegende Struktur einer APK kennen und setzen einen einfachen Smali-Patch, um ein gesperrtes Feature freizuschalten. Darauf aufbauend fokussiert sich die zweite Challenge auf die Analyse von Entscheidungs- und Lizenzlogik, bei der Rückgabewerte über mehrere Klassen hinweg nachvollzogen und manipuliert werden müssen. Die dritte Challenge erweitert dieses Wissen um den Aspekt der internen Zustandskonsistenz, bei dem mehrere abhängige Statuswerte gemeinsam betrachtet und korrekt angepasst werden müssen. Die Reihenfolge ist didaktisch sinnvoll, da sie von strukturellem Verständnis über logische Entscheidungen hin zu architektonischem Denken führt. Jede Challenge verwendet ähnliche Werkzeuge, erhöht jedoch gezielt die konzeptionelle Schwierigkeit. Dadurch wird vermieden, dass Lösungen aus früheren Aufgaben direkt übertragbar sind. Insgesamt entsteht ein klarer Lernpfad von einfachen Eingriffen hin zu realistischeren Schutzmechanismen.*

## Übersicht der Challenges

| Nr. | Titel der Challenge | Autor | Schwierigkeit (1-5) | Lernziel (Stichwort) |
| --- | ------------------- |-------| ------------------- | -------------------- |
| 1   | APK Anatomy 101         | Felix | ⭐⭐                  | APK-Struktur & Smali-Grundlagen            |
| 2   | License Logic            | Simon | ⭐⭐⭐                 | Statische Analyse von Entscheidungslogik              |
| 3   | Trust, but Verify        | Iulia | ⭐⭐⭐⭐⭐                | Zustandsbasierte Schutzmechanismen & Konsistenzanalyse                 |


---

# Verwendung von `daddel`

`daddel` kann über den folgenden `docker`-Befehl gestartet werden:

```bash
docker run -it --network host \
  -v "$(pwd):/challenge" \
  ghcr.io/s-solom/daddel:dev \
  --config /challenge/my-config.yml --debug
````

- `--config`: Pfad zur Config-Datei **relativ zum aktuellen Verzeichnis**, da `$(pwd)` nach `/challenge` gemountet wird.
  Beispiel: `examples/dynamic.config.yml` muss lokal unter `./examples/dynamic.config.yml` existieren.
- `--debug`: Aktiviert den Debug-Modus (hilfreich beim Entwickeln für detailiertere Ausgaben).

## Beispiele

Als erstes können die beiden Beispiel-Configs aus den Challenges getestet werden:

- **Static**: Config in `Challenge_01_static`
- **Dynamic**: Config in `Challenge_02_dynamic`

Für den **Dynamic**-Typ befindet sich der Code der Demo-App in `Challenge_02_dynamic/submission/src`.