# GOIT test application
**Background:**
- Used language - java, js(jquery's libraries)
This application shows an index page where you can connect to an external file upload system.<br>
The root dir is set in settings.gradle file.<br>
When you click the "Connect" button root folder displayed with all the files and folders.

**Actions:**
- Upload file.</br> 
You can upload a file, whose size should be less than 100 kb.
- Delete</br>
When you click the "Delete" button, the file or folder will be deleted from the external storage.
- Download </br>
When you click on this button, the file download starts.
- Connect</br>
When you click on this button, the connection socket opens and a request is sent to load the root tree.

**Startup:**

Before starting the application, make sure that Java and Gradle are installed.<br>
To start the application, you need to build the project using the ***gradle build*** command.<br>
After that, to launch the application execute ***java -jar com.goit-1.0-SNAPSHOT.jar***

**Supported Operating Systems:**
- Unix based OS

