# Solution: Logic Reversal

## Required Tools
* jadx-gui (static analysis)
* apktool (decompiling and rebuilding)
* apksigner (signing the APK)
* adb (installation and logcat)

## Step-by-step Solution

### Step 1: Analysis with jadx-gui
First, open the APK in jadx-gui.

While browsing the code (e.g., using text search for login, verify, or log strings), a method responsible for login verification becomes visible:

`checkLogin(user, pass, context, viewModel)`

The decompiled code shows a classic conditional branch:

`if (isCorrectUser(user) && verifyPassword(pass)) {`
`    Log.d("LicenseManager", "BYPASS_DETECTED_SUCCESS");`
`    viewModel.requestFlag();`
`} else {`
`    viewModel.sendSnackbarMessage("Access denied.");`
`}`

Observation:  
The cryptographic verification itself is not trivial to bypass.  
The success depends entirely on a boolean condition.

### Step 2: Transition from analysis to Smali
jadx-gui is used exclusively for analysis and does not allow code modification.  
To modify the application, the APK must be decompiled using apktool:

`apktool d app-release.apk -o app_dec`

Using the package and class name identified in jadx:

`de.hhn.dojo.dummy.ui.screens.MainScreenKt`

the corresponding Smali file can be located:

`app_dec/smali/de/hhn/dojo/dummy/ui/screens/MainScreenKt.smali`

### Step 3: Analysis of the Smali code
The following relevant section can be found in the Smali file:

`invoke-static {p0}, ...->isCorrectUser(Ljava/lang/String;)Z`  
`move-result v0`  
`if-eqz v0, :cond_fail`

`invoke-static {p1}, ...->verifyPassword(Ljava/lang/String;)Z`  
`move-result v0`  
`if-eqz v0, :cond_fail`

Meaning:
* If either check returns false, execution jumps to the failure path.
* Only if both checks return true, the success path is executed.

### Step 4: Manipulation of the decision logic
To bypass the login, the branch condition can be inverted. For example:

`if-eqz v0, :cond_fail`

is changed to:

`if-nez v0, :cond_fail`

Effect:  
The control flow reaches the success path even when incorrect login credentials are entered.

### Step 5: Rebuild and sign the APK
After modification, rebuild the APK:

`apktool b app_dec -o patched.apk`

Then sign it using the debug keystore:

`apksigner sign --ks %USERPROFILE%\.android\debug.keystore --ks-key-alias androiddebugkey --ks-pass pass:android patched.apk`

Install it on the device:

`adb install -r patched.apk`

### Step 6: Retrieve the flag
After installation, enter any username and any password.  
The login will now be considered successful, and the flag will be displayed.

`D/LicenseManager: BYPASS_DETECTED_SUCCESS`

**Flag:** `pwn{logic_beats_crypto}`