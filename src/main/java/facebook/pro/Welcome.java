package facebook.pro;
import javax.swing.*;
import java.awt.*;

public class Welcome extends pageLayOut{

    
    public Welcome() {
        super.frame = new JFrame("facebook");
        super.cardLayout = new CardLayout();
        super.cardPanel = new JPanel(cardLayout);

        ImageIcon image = new ImageIcon("logo.png");
        frame.setIconImage(image.getImage());


        // Set up the main menu panel
        MainMenuPanel  MainPage= new MainMenuPanel();

        // Set up the registration panel
        RegisterPage registerPanel = new RegisterPage();

        // Set up the login panel
        LoginPage loginPanel = new LoginPage();

        // Set up the home page panel
        HomePage homePagePanel = new HomePage();


         // Set up the additional panels for the Welcone page features

        JPanel profilePanel = createSectionPanel("Profile");
        JPanel seePostsPanel = createSectionPanel("See Posts");
        JPanel friendsPanel = createSectionPanel("Friends");
        JPanel writePostsPanel = createSectionPanel("Write Posts");

        cardPanel.add(MainMenuPanel.menupan(), "Main Menu");
        cardPanel.add(RegisterPage.createRegisterPanel(), "Register");
        cardPanel.add(LoginPage.createLoginPanel(), "Login");
        cardPanel.add(HomePage.createHomePagePanel(), "Home Page");

        cardPanel.add(profilePanel, "Profile");
        cardPanel.add(seePostsPanel, "See Posts");
        cardPanel.add(friendsPanel, "Friends");
        cardPanel.add(writePostsPanel, "Write Posts");


        // Add the card panel to the frame
        frame.add(super.cardPanel);

        // Set frame properties
        frame.setSize(380, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   
        frame.setVisible(true);                                 
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);     
    }

}
