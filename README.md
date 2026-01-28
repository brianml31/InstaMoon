<div align="center">
    <img src="https://github.com/brianml31/InstaMoon/blob/InstaMoon/assets/InstaMoon_Logo.png" alt="InstaMoon_Logo" height="250" />


# InstaMoon 🌙
InstaMoon is a modding framework for Instagram focused on enhancing the user experience by enabling custom features and improvements.<br/><br/>
### A powerful **modding framework**

<sub>Created with ❤️ by <a href="https://github.com/brianml31">brianml31</a></sub>
</div>

<hr>

## 🏗️ Framework Architecture

<p align="center">
	<img src="https://github.com/brianml31/InstaMoon/blob/InstaMoon/assets/instamoon_architecture.png"
	alt="InstaMoon Framework Architecture"
	width="800"/>
</p>

> [!WARNING]  
> This project is strictly for personal use and is not affiliated, endorsed, or certified by Instagram in any way. Use at your own risk.

> **Note:** If you use any of the materials in this repository, please give proper credit by mentioning **Instamoon**. A simple mention or link is appreciated!
<hr>

**Recommended Instagram version:** [`413.0.0.0.76 alpha`](https://www.apkmirror.com/apk/instagram/instagram-instagram/instagram-413-0-0-0-76-release/instagram-413-0-0-0-76-2-android-apk-download/)

<details>
<summary><h3>Features</h3></summary>

### 👻 Ghost Mode
- Hide (Seen) in stories
- Hide (Seen) in DM
- Hide (Typing) in DM
- Hide (You took a screenshot) in DM
- Hide (Opened) in media
- Hide (Replayed) in media
- Hide (Seen) in live videos

### ⚙️ Other options
- Disable ads
- Disable analytics
- Disable video autoplay
- Disable 'Like' with double tap
- Hide suggested reels
- Remove empty bottom space

### 🅰️ App font
- Set a custom font for the app

### ‍💻 Developer options
- Open developer mode
- Export dev settings
- Import dev settings (.json)
- Import dev settings (.igmoon)
- Save mapping file
- Import mapping file
- Reset dev settings

### 🌙 Dark mode
</details>

<details>
  <summary><h3>Screenshots</h3></summary>
  <p>
    <img src="https://github.com/brianml31/InstaMoon/blob/InstaMoon/assets/instamoon_1.png" width="200">
    <img src="https://github.com/brianml31/InstaMoon/blob/InstaMoon/assets/instamoon_2.png" width="200" style="margin-right:10px;">
	<img src="https://github.com/brianml31/InstaMoon/blob/InstaMoon/assets/instamoon_3.png" width="200" style="margin-right:10px;">
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
          android:name="com.brianml31.instamoon.permissions.StoragePermissionActivity" />
      ```

6. **Insert the required function calls** from the `InjectionHooks` class into Instagram.
</details>

<hr>

## Special Thanks
- **Monserrat G**
- [Revanced](https://github.com/revanced)
- [Marcos shiinaider - InstaFlow](https://github.com/Mshiinaider)
- **Amàzing World**
- [seven332 - library (UniFile)](https://github.com/seven332/UniFile)
- [ACRA](https://github.com/ACRA/acra)
- [winsontan520 - library (Android-WVersionManager)](https://github.com/winsontan520/Android-WVersionManager)