# Challenge: Logic Reversal

**Difficulty:** ⭐⭐⭐☆☆  
**Category:** Static Analysis and Patching

## Scenario
*The developers of this app are convinced that their login is absolutely secure.  
Username and password are verified using modern cryptography — a direct attack seems hopeless.*

*But security does not depend on cryptography alone.  
Somewhere, a decision must be made whether success or failure occurs.*

## Your Task
1. Install the app `app-release.apk`.
2. Analyze the app statically using suitable reverse engineering tools.
3. Manipulate the app so that any login is accepted.
4. Obtain the flag that is displayed after a successful login.

## Hints (Optional)
* Hint 1: Pay attention to locations in the code where the application distinguishes between successful and failed login.
* Hint 2: The verification itself is not the key — the condition evaluated afterward is what matters.
* Hint 3: A very small change in the Smali code may be enough to alter the control flow.