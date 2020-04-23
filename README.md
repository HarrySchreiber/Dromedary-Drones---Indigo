# Dromedary-Drones---Indigo

READEME for running team Indigo's project in Eclipse (assumes you have Git downloaded)
And to save a local copy on your eclipse workspace


1) Click on the big green "Clone or Download" on the Github website
2) Copy this link "https://github.com/HarrySchreiber/Dromedary-Drones---Indigo.git" so you post our github link into eclipse
3) Open up Eclipse, and selct your workspace of choice
4) Click file at the top left corner.
5) Click import (should be 6th from the bottom).
6) In the "import" dialog box, click the ">" arrow around next to the Git folder.
7) Options should pop up and you want to select "Projects from Git" and click "Next >"
8) In the "Select Repository Source" box, you want to select "Clone URI" and click "Next>"
9) You should be led to the "Source Git Repository" box with a bunch of text fields.
10) Please fill in the box as follows.

	URI: https://github.com/HarrySchreiber/Dromedary-Drones---Indigo.git
	Host: github.com
	Repository path: /HarrySchreiber/Dromedary-Drones---Indigo.git
	Protocol: https
	Port: Can be left blank
	And under "Authentication", please login using your GTIHUB user and password.
11) You should be led to a "Branch Selection" and ONLY check SprintOneIterationAttempt and click "Next >"
12) You should be led to a "Local Destination", click "Next >". (saving your local git folder)
NOTE: You may have to change your "Directory" location, so just rename the end if you can't continue
13) You should be led to a "select a wizard for importing projects", select "Import existing Eclipse projects" and click "Next >".
15)In the "Import Projects" box, click the "Dromedary Drones -- Indigo" + where you saved in your git project in Step 12.
16) Then click "Finish" and you should be good to go!



If you get to this point and there are JavaFX import errors in "Main.java" follow these steps:
	1. Download JavaFX JDK: https://gluonhq.com/download/javafx-14-0-1-sdk-windows/
	2. Extract the files anywhere and write down the file path
	3. In eclipse go to window>preferences>Java>Build Path>User Libraries
	4. Click "New..."
	5. Name the user library "JavaFX" and click ok
	6. Click on "Add External JARs"
	7. Navigate to where you saved those extracted files
	8. Go into the "lib" folder and just select all of the JAR files and click open
	9. Click "Apply and Close"
	10. Right click on your eclipse project and navigate to Build Path> Add Libraries...
	11. Click "User Library" and Click "Next" and Select "JavaFX" and Click "Finish"
If at this point you cannot run the application because of missing runtime components follow these steps:
	1. In eclipse, next to the run button there is a drop down arrow, select it and Click "run configurations"
	2. Select Arguments Tab on the main page
	3. In the VM Arguments box type: --module-path <the file path of your JavaFX jars to the lib level, the file path between quotation marks> --add-modules javafx.controls,javafx.fxml
	4. Hit apply
	5. You should now be able to run the program if you were previously unable to do so

A video breakdown of the buildpath steps can be found here: https://www.youtube.com/watch?v=oVn6_2KuYbM


Runing Our Simulation:

1) Click the ">" arrow next to "Dromedary Drones -- Indigo"
2) Click "src", then "application" and open up Main.java like you normally would to edit your code
3) Run "Main.java" and our simulation page with our blank graphs should open up.
4) Clicking "Run Simulation" which will run with the typical orders and our GCC locations
5) If you want to edit your simulation, click "Edit Simulation" and change what you want
