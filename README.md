<div align="center">
    <img src="https://github.com/brianml31/InstaMoon/blob/InstaMoon/assets/InstaMoon_Logo.png" alt="InstaMoon_Logo" height="250" />


# InstaMoon 🌙
InstaMoon is a reverse-engineered injectable menu that enhances your Instagram experience.<br/><br/>

<sub>Created with ❤️ by <a href="https://github.com/brianml31">brianml31</a></sub>
</div>

<hr>

> [!WARNING]  
> This project is strictly for personal use and is not affiliated, endorsed, or certified by Instagram in any way. Use at your own risk.

<hr>

**Recommended Instagram version:** [`376.0.0.0.42 alpha`](https://www.apkmirror.com/apk/instagram/instagram-instagram/instagram-376-0-0-0-42-release/instagram-376-0-0-0-42-4-android-apk-download/)

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
</details>

<details>
<summary><h3>How to use</h3></summary>

### **Note:** To inject the **DEX files** from the generated APK, use **MT Manager**.

#### **Steps to Follow:**

1. **Compile the project** using **Android Studio**.
2. From the generated APK, use **MT Manager "Dex Redivide"** function to merge all DEX files into a **single DEX file**.
3. **Transfer the generated DEX file** to the **Instagram application**.
4. **Add the function calls** from the `KotlinInvoker` class.
    - *(If unsure, refer to the sample APK provided in the channel.)*
5. **Add the following permissions and activity** to `AndroidManifest.xml`:
   ```xml
   <uses-permission android:name="android.permission.MANAGE_EXTERNAL_STORAGE" />
   <activity 
       android:theme="@android:style/Theme.DeviceDefault.Dialog" 
       android:name="com.brianml31.insta_moon.PermissionDialog" />
   
</details>

<hr>

## Special Thanks
- **Monserrat G**
- [Revanced](https://github.com/revanced)