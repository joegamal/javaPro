
package facebook.pro;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

import static facebook.pro.Welcome.*;
import static facebook.pro.pageLayOut.*;


public abstract class UserManager {

    public static String FRIENDNAME ;
    public static String current_user;

    public static ObjectMapper mapper = new ObjectMapper();

    public static  ArrayList<LinkedHashMap<String, Object>> listOfUsers;

    public static File jsonFile = new File("users.json");

    public static void loadUserFromjsonFile(String path){
        try {
            // Read the JSON file content into a string
            String jsonString = new String(Files.readAllBytes(Paths.get("users.json")));


            if(!(jsonString == null || jsonString.isEmpty() || jsonString.equals(""))) {
                // Deserialize JSON into a List of User objects
                listOfUsers = mapper.readValue(new File(path), ArrayList.class);
            }else{
                listOfUsers = new ArrayList<LinkedHashMap<String, Object>>();
            }


        } catch (Exception e) {
            e.getMessage();
            System.out.println("Error occurred while parsing JSON:");
        }
    }

    // Method to register a user
    public static void registerUser(User user) throws IOException {
        LinkedHashMap<String, Object> list = new LinkedHashMap<String, Object>();
        list.put("username", user.username);
        list.put("birthDate", user.birthDate);
        list.put("gender", user.gender);
        list.put("email", user.email);
        list.put("password", user.password);
        list.put("friends",User.friends);
        if(listOfUsers == null){
            listOfUsers = new ArrayList<LinkedHashMap<String, Object>>();
        }
        listOfUsers.add(list);
        for(int i = 0; i < listOfUsers.size(); i++){
            System.out.println(listOfUsers.get(i).get("email"));
        }
        UserManager.store();
        UserManager.loadUserFromjsonFile("users.json");
    }
    // Method to check if a user exists by emai
    public static boolean userExists(String email) {

        if (!(listOfUsers == null)) {
            // Iterate through the array to check for matching email
            for (int i = 0; i < listOfUsers.size(); i++) {

                if (email.equals(listOfUsers.get(i).get("email"))) {
                    return false;
                }
            }
        }
        return true;
    }
    //method returns current username

    // Method to validate login credentials
    public static boolean loginUser(String email, String password) {
        if(!(listOfUsers == null)) {
            // Iterate through the array to check for matching credentials
            for (int i = 0; i < listOfUsers.size(); i++) {
                if (email.equals(listOfUsers.get(i).get("email")) && password.equals(listOfUsers.get(i).get("password"))) {
                    current_user = email;
                    cardPanel.add(SeePosts.createSeePanel(), "See Posts");

                    return true;
                }
            }
        }

        return false;
    }

    public static void store () throws IOException{

        jsonFile.delete();
        File jsonFile = new File("users.json");
        String input = "";
        if(!(listOfUsers == null))
            input = listOfUsers.toString();
        // Convert to valid JSON

        String validJson = input
                .replace("=", ":") ; // Close quotes for array elements
//        System.out.println("Valid JSON String: \n" + validJson);

        ObjectMapper mapper = new ObjectMapper();
        try {
            String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(listOfUsers);

            mapper.writerWithDefaultPrettyPrinter().writeValue(new File("users.json"), listOfUsers);

            System.out.println("Data successfully written to output.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

// overloading
    public static void searchFriend(String name){
        boolean to = false;
        for(int i = 0; i < listOfUsers.size(); i++){
            if(name.equals(listOfUsers.get(i).get("username"))) {
                cardLayout.show(cardPanel, "Friend");
                to = true;
                FRIENDNAME = name;
            }
        }
        if(to == false){
            JOptionPane.showMessageDialog(frame, "user doesnt exist!");
        }
    }

    public static boolean searchFriend(String fname, int i){

        for( LinkedHashMap<String, Object> user : listOfUsers){
            if(listOfUsers != null ){
                if(current_user.equals(user.get("username"))){
                    ArrayList<LinkedHashMap<String, Object>> friendsList = ( ArrayList<LinkedHashMap<String, Object>>) user.get("friends");
                    if(friendsList != null) {
                        for (LinkedHashMap<String, Object> friend : friendsList) {
                            if (fname.equals(friend.get("friendname"))) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    //messaging part----------------------------------
    //send message to a friend
    public static void sendMessage(String message) {
        String usernameofuser=null;
        boolean state1=false;
        boolean state2=false;
        for (LinkedHashMap<String,Object>user:listOfUsers) {
            if (user.get("email").equals(current_user)) {

                ArrayList<LinkedHashMap<String,Object>> friend =  (ArrayList<LinkedHashMap<String, Object>>) user.get("friends");
                usernameofuser = (String) user.get("username");

                for (LinkedHashMap<String,Object> frienddata : friend) {

                    if (frienddata.get("friendname").equals(FRIENDNAME)) {
                        ArrayList<String>messageyousend=(ArrayList<String>) frienddata.get("messagesYouwrite");
                        int index = messageyousend.size() - 1;

                        if (index<0) {
                            messageyousend.add(message);
                            state1 = true;
                        }

                        else{
                          if (!messageyousend.get(index).equals(message)) {
                            messageyousend.add(message);
                            state1 = true;
                         }
                        }
                        break;
                    }
                }
                if (state1==true){break;}
            }
        }

        for (LinkedHashMap<String,Object>user:listOfUsers) {

            if (user.get("username").equals(FRIENDNAME)) {
                ArrayList<LinkedHashMap<String,Object>>friend=(ArrayList<LinkedHashMap<String, Object>>) user.get("friends");

                for (LinkedHashMap<String,Object>frienddata:friend) {

                    if (frienddata.get("friendname").equals(usernameofuser)) {
                        ArrayList<String> messageyousend = (ArrayList<String>) frienddata.get("messages(He/she)write");
                        int index = messageyousend.size() - 1;

                        if (index<0) {
                            messageyousend.add(message);
                            state2 = true;
                        }

                        else{

                            if (!messageyousend.get(index).equals(message)) {
                                messageyousend.add(message);
                                state2 = true;
                            }
                        }
                        break;
                    }
                }
                if (state2==true){break;}
            }
        }

        try {
            store();
        } catch (IOException e) {
            System.err.println("Error saving data to file: " + e.getMessage());
        }


    }
    //end of messaging part------------------------------------

    public static void addfreind(String fname) throws IOException {
        FRIENDNAME = fname;
        LinkedHashMap<String, Object> friendDetails = new LinkedHashMap<>();

        ArrayList<String> messaesHewrtie = new ArrayList<>();
        ArrayList<String> messaesYouwrtie = new ArrayList<>();


        friendDetails.put("friendname", fname); // Generate a 4-digit numeric ID
        friendDetails.put("messages(He/she)write", messaesHewrtie);
        friendDetails.put("messagesYouwrite", messaesYouwrtie);

        LinkedHashMap<String, Object> friendreplace = new LinkedHashMap<>();
        friendreplace.put("friendname", current_user);

        friendreplace.put("messages(He/she)write", messaesYouwrtie);
        friendreplace.put("messagesYouwrite", messaesHewrtie);



        for (LinkedHashMap<String, Object> user : listOfUsers) {
            if (user.get("email").equals(current_user)) {

                ArrayList<LinkedHashMap<String, Object>> friends =
                        (ArrayList<LinkedHashMap<String, Object>>) user.get("friends");

                if (friends == null) {
                    friends = new ArrayList<>();
                    user.put("friends", friends);
                }

                friends.add(friendDetails);
            }

        }

        for(LinkedHashMap<String, Object> user : listOfUsers){
            if(user.get("email").equals(fname)){


                ArrayList<LinkedHashMap<String, Object>> frnds =
                        (ArrayList<LinkedHashMap<String, Object>>) user.get("friends");

                if (frnds == null) {
                    frnds = new ArrayList<>();
                    user.put("friends", frnds);
                }

                frnds.add(friendreplace);
                store();

            }
        }



        // Save the updated data to the file
        try {
            store();
        } catch (IOException e) {
            System.err.println("Error saving data to file: " + e.getMessage());
        }
    }

    public static void addPost(String content, String privacy) {
        // Create a new post with a unique numeric ID (4 digits)
        LinkedHashMap<String, Object> postDetails = new LinkedHashMap<>();
        postDetails.put("postId", String.format("%04d", (int) (Math.random() * 10000))); // Generate a 4-digit numeric ID
        postDetails.put("content", content);
        postDetails.put("privacy", privacy);

        // Find the current user and add the post
        for (LinkedHashMap<String, Object> user : listOfUsers) {
            if (user.get("email").equals(current_user)) {
                ArrayList<LinkedHashMap<String, Object>> posts =
                        (ArrayList<LinkedHashMap<String, Object>>) user.get("posts");

                if (posts == null) {
                    posts = new ArrayList<>();
                    user.put("posts", posts);
                }

                posts.add(postDetails);
                break;
            }
        }

        // Save the updated data to the file
        try {
            store();
        } catch (IOException e) {
            System.err.println("Error saving data to file: " + e.getMessage());
        }
    }

}

//finish