/*********************************************/
SOFTWARE ARCHITECTURE DESIGN & IMPLEMENTATION
COSC(2391/2401)
ASSIGNMENT 2
2013 SEMESTER 1

README.TXT
/*********************************************/

---------------------------------------------------------------------------------------------------------
GROUP MEMBERS:-
SIA WAI SUAN (s3308555)
CHONG SZE ZHEN (s3310388)
MING WEI TEE (s3260935)
---------------------------------------------------------------------------------------------------------

---------------------------------------------------------------------------------------------------------
BREAKDOWN OF CONTRIBUTION:-

SIA WAI SUAN (s3308555)
	-Developed/Implemented the RMI structure of the game/program.
	-Developed/Implemented the SCORING/RANKING system of the game (Logic wise).

CHONG SZE ZHEN (s3308555):-
	-Developed/Implemented the JDBC side of the program.
	-Developed the LOGIN/REGISTRATION GUI forms.
	-Implemented the REVERSE gear function for the snake(s).

MING WEI TEE (s3260935):-
	-Developed/Implemented the SCORING/RANKING system of the game (GUI wise).
---------------------------------------------------------------------------------------------------------

---------------------------------------------------------------------------------------------------------
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