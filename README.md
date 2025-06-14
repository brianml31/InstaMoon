<div align="center">
    <img src="https://github.com/brianml31/InstaMoon/blob/InstaMoon/assets/InstaMoon_Logo.png" alt="InstaMoon_Logo" height="250" />


# InstaMoon 🌙
InstaMoon is a reverse-engineered injectable menu that enhances your Instagram experience.<br/><br/>

<sub>Created with ❤️ by <a href="https://github.com/brianml31">brianml31</a></sub>
</div>

<hr>

> [!WARNING]  
> This project is strictly for personal use and is not affiliated, endorsed, or certified by Instagram in any way. Use at your own risk.

> **Note:** If you use any of the materials in this repository, please give proper credit by mentioning **Instamoon**. A simple mention or link is appreciated!
<hr>

<!-- **Recommended Instagram version:** [`380.0.0.0.0 alpha`](https://www.apkmirror.com/apk/instagram/instagram-instagram/instagram-380-0-0-0-0-release/instagram-380-0-0-0-0-4-android-apk-download/) -->

<details>
<summary><h3>Features</h3></summary>

### Ghost Mode
- Stay anonymous by watching Stories, Messages, and live videos.

### Remove Ads
- Remove ads from feed, stories, reels, explore. (Thanks to revanced)

### Remove Analytics
- Block Instagram's tracking and analytics to protect your privacy.

### Developer Mode
- Access to instagram's in-development features.

### Export Developer Mode Settings
- Export your developer mode configurations

### Import Developer Mode Settings
- Import your developer mode configurations

### Clear Developer Mode Settings

### Save File (id_name_mapping.json)
</details>

<details>
  <summary><h3>Screenshots</h3></summary>
  <p>
    <img src="https://github.com/brianml31/InstaMoon/blob/InstaMoon/assets/1.png" width="200">
    <img src="https://github.com/brianml31/InstaMoon/blob/InstaMoon/assets/2.png" width="200" style="margin-right:10px;">
  </p>
</details>

<details>
<summary><h3>How to use</h3></summary>

#### **Steps to Follow:**

1. **Compile the project** using **Android Studio** to generate the APK.
2. **Decompile the generated APK** to extract:
   - **Smali code**
3. **Merge the extracted files** with the original Instagram APK, making sure to include:
   - Smali classes (especially the `InstagramInjectionManager` class)
5. **Modify Instagram’s `AndroidManifest.xml`**:
   - Add the following permission:
     ```xml
     <uses-permission android:name="android.permission.MANAGE_EXTERNAL_STORAGE" />
     ```
   - Declare the required activity:
     ```xml
     <activity 
         android:theme="@android:style/Theme.DeviceDefault.Dialog" 
         android:name="com.brianml31.instamoon.PermissionDialog" />
     ```

6. **Insert the required function calls** from the `InstagramInjectionManager` class into Instagram's execution flow.
</details>

<hr>

## Special Thanks
- **Monserrat G**
- [Revanced](https://github.com/revanced)
- [Marcos shiinaider - InstaFlow](https://github.com/Mshiinaider)
- **Amàzing World**