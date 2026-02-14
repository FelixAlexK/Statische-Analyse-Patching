# Challenge: The APK Puzzle

**Difficulty:** ⭐☆☆☆☆  
**Category:** Code Anatomy – Static Analysis and Patching

---

## Scenario
*A cautious developer split the master password for this system into three fragments and hid them in completely different locations within the app's anatomy. He believes that nobody can find all the parts because they are hidden "outside the visible code."*

*To crack this system, you must become a digital archaeologist. Only those who understand the structure of an APK file—from the manifest to the resources—will be able to reassemble the puzzle.*

---

## Your Task
1.  **Install** the app `anatomy-challenge-puzzle.apk`.
2.  **Analyze** the app statically using appropriate reverse engineering tools (e.g., `jadx-gui` or `apktool`).
3.  **Find** the three fragments of the password and the username
4.  **Combine** the parts in the correct order and successfully log in as the user to retrieve the flag.

---

## Hints (Optional)
* **Tip 1:** Find the location where the login check logic is implemented. Look for hints there, there the password parts might be stored.
* **Tip 2:** I only there was a file where all meta data is stored...
* **Tip 3:** Some values are more helpful resources than others :)

---