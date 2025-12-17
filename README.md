# Gruppen-Manifest: [Thema der Gruppe]

**Mitglieder:**
* Name 1 (Matrikelnummer)
* Name 2 (Matrikelnummer)
...

## Der Rote Faden
*(Beschreiben Sie hier kurz (5-10 Sätze), welche Story oder welches Lernziel Ihre Challenges verbindet. Warum ist diese Reihenfolge sinnvoll?)*

## Übersicht der Challenges

| Nr. | Titel der Challenge | Autor | Schwierigkeit (1-5) | Lernziel (Stichwort) |
| --- | ------------------- | ----- | ------------------- | -------------------- |
| 1   | Hello World         | Max   | ⭐                   | ADB Setup            |
| 2   | The Key             | Erika | ⭐⭐                  | Logcat               |
| 3   | ...                 | ...   | ...                 | ...                  |


---

# Verwendung von `daddel`

`daddel` kann über den folgenden `docker`-Befehl gestartet werden:

```bash
docker run -it --network host \
  -v "$(pwd):/challenge" \
  -w /challenge \
  ghcr.io/s-solom/daddel:dev \
  --config examples/dynamic.config.yml --debug
````

- `--config`: Pfad zur Config-Datei **relativ zum aktuellen Verzeichnis**, da `$(pwd)` nach `/challenge` gemountet wird.
  Beispiel: `examples/dynamic.config.yml` muss lokal unter `./examples/dynamic.config.yml` existieren.
- `--debug`: Aktiviert den Debug-Modus (hilfreich beim Entwickeln für detailiertere Ausgaben).

## Beispiele

Als erstes können die beiden Beispiel-Configs aus den Challenges getestet werden:

- **Static**: Config in `Challenge_01_static`
- **Dynamic**: Config in `Challenge_02_dynamic`

Für den **Dynamic**-Typ befindet sich der Code der Demo-App in `Challenge_02_dynamic/submission/src`.