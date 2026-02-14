# Group Manifest: Static Analysis & Patching

**Members:**
* Widmann, Simon (216071)
* Kuhbier, Felix (212115)

## The Common Thread
The two challenges share the common goal of gradually introducing students to the static analysis and targeted patching of Android APKs. 
In the first challenge, students learn about the basic structure of an APK and identify simple configuration decisions in the manifest and resources that influence the app’s behavior. 
By finding different parts of a password, the flag can be retrieved. 

Building on this, the second challenge expands this knowledge to include the analysis of decision logic at the code level. 
Students must trace the application’s control flow and deliberately invert a key condition in the Smali code to bypass a security mechanism. 
The sequence of the challenges is didactically meaningful, as it progresses from structural understanding to logical analysis and manipulation. 
Overall, this creates a consistent learning path from simple configuration patches to more realistic logic bypasses.*

## Challenge Overview

| No. | Challenge Title | Author | Difficulty (1–5) | Learning Objective (Keyword)           |
|-----|-----------------|--------|------------------|----------------------------------------|
| 1   | Anatomy Puzzle  | Simon  | ⭐                | Understanding APK structure            |
| 2   | Logic Reversal  | Felix  | ⭐⭐⭐              | Reading Smali code and inverting logic |

---

## Prerequisites
The apps for the challenges were tested using a Pixel 6 emulator with API Level 36.

# Using `daddel`

`daddel` can be started using the following `docker` command:

```bash
docker run -it --network host \
  -v "$(pwd):/challenge" \
  ghcr.io/s-solom/daddel:dev \
  --config /challenge/my-config.yml --debug