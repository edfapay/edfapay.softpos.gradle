# EdfaPay SoftPOS Gradle Plugin
This Plugin helps developer to install the EdfaPay SoftPos SDK and apply configurations to the project followed by their partner code.

<p align="center">
  <a href="https://edfapay.com">
      <img src="https://github.com/edfapay/edfapay.softpos.gradle/blob/master/header.jpg" alt="EdfaPg" width="400px"/>
  </a>
</p>

### Partner Code
The **partner code** will be provided by EdfaPay, Developer should pass the partner code to edfapay plugin in module `build.gradle`

***

### Usage and Documentation
Apply the plugin in your root build.gradle and install `EdfaPay SoftPOS SDK` followed by given instruction below:

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


#### Install EdfaPay SoftPOS SDK
Add below script to module level `build.gradle` _(example: PROJECT_ROOT/app/build.gradle)_
```
edfapay{
    softpos {
        install("partner code here")
    }
}

// or (use above or below statement)

edfapay.softpos.install("partner code here")
```


Sync or run the command on project root `./gradlew build`


> [!TIP]
> Developer also able to pass below optional parameters to plugin, So the plugin will install dependency based on passed parameters
> Parameters:
> - mode
>   - **Possible values**
>     - production
>     - development
>     - local (not for external developer)
> - type
>   - **Possible values**
>     - release
>     - debug
>
> ### Example:
>```
> edfapay{
>     softpos {
>         mode = "production"
>         type = "debug"
>         install("Partner Code")
>     }
> }
>```

