# LibGDX installation Instructions
- These instructions are mainly concerned with setting up a new project, for opening a project go straight to step 6 below.
- Instructions can be found on pp. 29-45 in *Mastering LibGDX Game Development* by Patrick Hoey.
- Most of the steps described here can also be seen in the following [video](https://www.youtube.com/watch?v=rzBVTPaUUDg)

1. First, you will need to install the latest Java Development Kit (JDK)

After installing the JDK, you can make sure that the environment variable, JAVA_HOME, is set by opening a Command Prompt window and typing the following in the command line:
`java –version`
If everything is set correctly, you should see something like the following output:
```shell
java version "1.8.0_161"
Java(TM) SE Runtime Environment (build 1.8.0_161-b12)
Java HotSpot(TM) 64-Bit Server VM (build 25.161-b12, mixed mode)
```

2. Second, we will need to install and set up an integrated development environment (IDE) in order to develop with LibGDX. Out of the box, LibGDX generates the startup projects for:
- IntelliJ IDEA: The community edition is free and can be found at http://www.jetbrains.com/idea/download/

3. Third, we will need to download and run the gdx-setup tool, which with a little configuration will generate our project files, platform-specific wrapper class, and our starter classes. You can download the gdx-setup.jar file by visiting http://libgdx.badlogicgames.com/download.html and then clicking on the Download Setup App option. Gdx-setup generates boilerplate code for each of the platform targets that you select, which wrap the main entry point for the game. See page 31 in *Hoey* for more info.

- Your `Android SDK` is usually located at: `~/Library/Android/sdk`.

4. You *might* need to make sure you have *gradle* installed.
- Your `gradle` is usually located at `/usr/local/bin/gradle`

5. To create a new LibGDX project, look for instructions on pp. 43-45:

    1. Select `Run | Edit Configurations` from the menu in Intellij.
    2. Click on the `+` icon in the upper-left corner and choose `Application`.
    3. You will be presented with the `Run/Debug Configuration` dialog.
    4. You should give your run target a name, such as `Run`.
    5. You will need to update the `Main class:` setting with `DesktopLauncher`. It should say: `com.red.boxx.desktop.DesktopLauncher`
    6. You will also need to set the `Working directory:` path to `<project root>\core\assets`.
    7. You will need to set the `Use classpath of module` option to `Desktop` or `desktop_main` since this is where the main entry point of your game lives.
    8. Click on `OK` when finished.
    Now, everything should be configured to finally run. From the menu, select `Run | Run target` or the green run button. If all the dependencies are correct, with a proper Gradle build setup and project configuration, you should see a red screen with a `bad logic games` image.

6. To open a LibGDX project it's best to import the `build.gradle` file from Intellij.
