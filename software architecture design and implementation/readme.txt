/*********************************************/
SOFTWARE ARCHITECTURE DESIGN & IMPLEMENTATION
COSC(2391/2401)
ASSIGNMENT 2
2013 SEMESTER 1

README.TXT
/*********************************************/
NOTES:-

-There were no specific techniques used to reduce network traffic. Gameplay seems to be running
 smoothly with RMi. So, there was not any need for any special techniques.

-Steps to start the PROGRAM:	1) START RMIREGISTRY (JavaSE-1.7 _ C:\Program Files\Java\jre7\bin)
				2) INCLUDE OJDBC14.JAR IN YOUR PROJECT.
				3) INCLUDE THE FOLLOWING VM ARGUMENT(S) IN THE RUN CONFIGURATIONS:
					-Djava.rmi.server.codebase=file:${workspace_loc}/SADI_Ass2/bin/
				4) RUN RMISERVERIMP.JAVA
				5) RUN RMICLIENT.JAVA

-REMEMBER TO ALTER THE SERVER'S HOST IP ADDRESS IN RMISERVERIMP.JAVA -- This can be found in the MAIN
function of the java file.
E.g.: Naming.rebind("//XXX.XXX.XXX.XX/RMIServer", serverImp);
---------------------------------------------------------------------------------------------------------