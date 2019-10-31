package cjmb.com.raasrelease01;

/*IF THIS IS THE FIRST FILE YOU COME ACROSS IN THIS PROJECT -
*
* Please take time to learn some Android basics before coming to this code. I had been developing
* while learning, which is a hard enough task. Knowing Java is not enough to start modifying this
* code. It will be a nightmare to figure this out with some basic knowledge of activities, fragments,
* connecting to the network (Android volley), grade dependencies, and android manifest*/

/*This interface is to be implemented in Activities that progress to another activity.
*
* The primary Activities in this project are the login Activity and home Activity
 *
 * There are other activities (like change password, or task viewing) that could benefit
 * from these classes but are not particularly necessary*/

public interface AdvanceUserInformation {
    void advancePage(String username);
}
