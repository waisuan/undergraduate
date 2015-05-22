/*
 * NAME: SIA WAI SUAN
 * STUDENT ID: S3308555
 * MOBILE APPLICATION DEVELOPMENT
 * ASSIGNMENT 2
 * FILE: UserCredentials.java
 */
package mad.ass2.meetup.constants;

/*
 * Class filled with STRING CONSTANTS.
 */
public interface UserCredentials {
	public final String clientID = "663239628075-21139n2lken0rf2gn00clh230ce6a5lr.apps.googleusercontent.com";
	public final String clientSecret = "hS62sNeaSyqN0FqHqrGuKaXe";
	public final String apiKey = "AIzaSyAPp9Dk9qZmNaR5vOdOFmNXCEJZbRuUgFM";
	
	public final String listURI = "https://www.googleapis.com/tasks/v1/users/@me/lists?key=";
	public final String tasksURI = "https://www.googleapis.com/tasks/v1/lists/";
	public final String tasksURISuffix = "/tasks?key=";
	public final String taskURISuffix = "/tasks/";
	public final String keyURI = "?key=";
}
