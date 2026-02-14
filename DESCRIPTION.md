## Module Description: Static Analysis & Patching

In this module, students learn how to understand and deliberately manipulate Android APKs using static analysis. The focus is not on executing or instrumenting apps, but on reading, analyzing, and patching existing code.

The first challenge introduces the fundamental structure of an APK and demonstrates how configuration decisions in the manifest or resources influence an app’s behavior. Through a simple patch, a locked feature is unlocked.

Building on this, the second challenge introduces the analysis of decision and license logic. Students must trace the application’s control flow in the Smali code and deliberately modify it in order to bypass a security mechanism. The correct behavior is not achieved by discovering a secret key, but by intentionally manipulating the decision logic.

The module gradually progresses from simple structural modifications to more realistic logic bypasses and teaches core techniques of static analysis and Android reverse engineering.