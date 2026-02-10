# Gruppen-Manifest: Statische Analyse & Patching


**Mitglieder:**
* Widmann, Simon (216071) 
* Kuhbier, Felix (212115)

## Der Rote Faden
*Die beiden Challenges verfolgen das gemeinsame Ziel, Studierende schrittweise an die statische Analyse und das gezielte Patchen von Android-APKs heranzuführen. In der ersten Challenge lernen sie die grundlegende Struktur einer APK kennen und identifizieren einfache Konfigurationsentscheidungen im Manifest oder in den Ressourcen, die das Verhalten der App beeinflussen. Durch einen gezielten Patch wird ein gesperrtes Feature freigeschaltet. Darauf aufbauend erweitert die zweite Challenge dieses Wissen um die Analyse von Entscheidungslogik auf Code-Ebene. Die Studierenden müssen hierbei den Kontrollfluss der Anwendung nachvollziehen und eine zentrale Bedingung im Smali-Code gezielt umkehren, um einen Sicherheitsmechanismus zu umgehen. Die Reihenfolge der Challenges ist didaktisch sinnvoll, da sie von strukturellem Verständnis hin zu logischer Analyse und Manipulation führt. Insgesamt entsteht ein konsistenter Lernpfad von einfachen Konfigurations-Patches hin zu realistischeren Logik-Bypässen.*

## Übersicht der Challenges

| Nr. | Titel der Challenge | Autor | Schwierigkeit (1-5) | Lernziel (Stichwort)                      |
| --- |---------------------|-------| ------------------- |-------------------------------------------|
| 1   | "Debug"-Schalter    | Simon | ⭐⭐                  | APK-Struktur verstehen                    |
| 2   | Logik-Umkehr        | Felix | ⭐⭐⭐                 | Smali-Code lesen und Bedingungen umkehren |


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