# Solution: Code Anatomy Challenge — Extract Username and Three-Part Password

## Required Tools
* jadx-gui (static analysis)
* apktool (optional, resource and manifest extraction)

## Step-by-step Solution

### Step 1: Static analysis with jadx-gui
First, open the APK using jadx-gui.

Navigate to the package:

`de.hhn.dojo.dummy.ui.screens`

Locate the file containing the login logic. Inside, you will find the function responsible for authentication:

`checkLogin(userIn, passIn, context, viewModel)`

The decompiled code shows how the credentials are constructed and verified:

`val part1 = ai.metaData.getString("secret_part_1") ?: ""`

`val part2Id = context.resources.getIdentifier("secret_part_2", "string", context.packageName)`
`val part2 = if (part2Id != 0) context.getString(part2Id) else ""`

`val part3 = "anatomy"`

`val correctPassword = part1 + part2 + part3`
`val correctUser = "admin"`

`if (userIn == correctUser && passIn == correctPassword) {`
`    Log.d("ChallengeVerify", "AUTH_SUCCESS_FULL_PIECES")`
`    viewModel.requestFlag()`
`} else {`
`    viewModel.sendSnackbarMessage("Invalid Credentials! Check the code anatomy.")`
`}`

Observation:
* The username is hardcoded as `"admin"`
* The password consists of three parts concatenated together
* Each part comes from a different source:
    * part1 → AndroidManifest meta-data
    * part2 → string resource in strings.xml
    * part3 → hardcoded string `"anatomy"`

---

### Step 2: Extract part1 from AndroidManifest.xml
The code retrieves part1 using:

`ai.metaData.getString("secret_part_1")`

This means the value is stored inside the AndroidManifest.

In jadx-gui, open:

`AndroidManifest.xml`

Look for the following entry:

`<meta-data`
`    android:name="secret_part_1"`
`    android:value="pwn_"/>`

The value of `android:value` is the first part of the password.

Alternatively, using apktool:

`apktool d app-release.apk -o app_dec`

Then open:

`app_dec/AndroidManifest.xml`

Search for:

`secret_part_1`

---

### Step 3: Extract part2 from strings.xml
The code retrieves part2 using:

`context.resources.getIdentifier("secret_part_2", "string", context.packageName)`

This means the value is stored in the string resources.

In jadx-gui, open:

`Resources → res → values → strings.xml`

Find the following entry:

`<string name="secret_part_2">college_</string>`

The content between the tags is the second part of the password.

Alternatively, using apktool:

`app_dec/res/values/strings.xml`

Search for:

`secret_part_2`

---

### Step 4: Extract part3 from the source code
The third part is directly hardcoded in the application source:

`val part3 = "anatomy"`

---

### Step 5: Construct the correct credentials
The password is constructed as:

`correctPassword = part1 + part2 + part3`

This means the final password format is:

`pwn_college_anatomy`

The username is:

`admin`
---

### Step 6: Flag retrieval
After entering the correct credentials, the application displays the flag in a dialog window.

The authentication success is confirmed by:

`AUTH_SUCCESS_FULL_PIECES`

**Flag:** `pwn{this_is_the_solution_flag}`