<p align="center">
  <a href="https://edfapay.com">
      <img src="https://github.com/edfapay/edfapay.softpos.gradle/blob/master/header.jpg" alt="EdfaPg" width="400px"/>
  </a>
</p>

# EdfaPay SoftPOS Gradle Plugin
This Plugin helps developer to install the EdfaPay SoftPos SDK and apply configurations to the project followed by their partner code.

***

### Configure Your Partner Code
The **partner code** will be provided by EdfaPay, Developer should set the partner code to an environment variable as gradle script take it from their and perform the required task to configure and install SDK in to your project.
 - Variable should named as **EDFAPAY_PARTNER**


_**Below are the instruction to add permanent environment variable**_

**MAC/LINUX:** Permanent environment variables can be added to `.bash_profile` file

1. Open the .bash_profile file with a text editor of your choice. (create file if not exist)
2. Scroll down to the end of the .bash_profile file.
3. Copy below text and paste to a new line. (replace `your partner code` with actual value received from `EdfaPay`):
   - `export EDFAPAY_PARTNER=your partner code`

4. Save changes you made to the .bash_profile file.
5. Execute the new .bash_profile by either restarting the machine or running command below:
   - `source ~/.bash-profile`

**WINDOWS:**
1. Open the link below:
       https://phoenixnap.com/kb/windows-set-environment-variable#ftoc-heading-4

2. **Make sure below:**
   - Variable name should be `EDFAPAY_PARTNER`
   - Variable value should be `your partner code` received from `EdfaPay`

***

### Usage and Documentation
Apply the plugin in your root build.gradle

#### Applying the plugin
Check for Latest [Version](https://plugins.gradle.org/plugin/com.edfapay.softpos.tools)

Using [plugins DSL](https://docs.gradle.org/current/userguide/plugins.html#sec:plugins_block):
> ```
> plugins {
>     id "com.edfapay.softpos.tools" version "<version>"
> }
> ```
Using the [legacy plugin application](https://docs.gradle.org/current/userguide/plugins.html#sec:old_plugin_application):
> ```
> buildscript {
>     repositories {
>         maven {
>             url "https://plugins.gradle.org/m2/"
>         }
>     }
>     dependencies {
>         classpath "com.edfapay.softpos:plugin:0.0.1"
>     }
> }
>
> apply plugin: "com.edfapay.softpos.tools"
> ```


#### Install dependencies
Add below script to module level `build.gradle` _(example: PROJECT_ROOT/app/build.gradle)_
```
edfapay{
    softpos {
        install()
    }
}

// or (use above or below statement)

edfapay.softpos.install()
```

Sync or run the command on project root `./gradlew build`

