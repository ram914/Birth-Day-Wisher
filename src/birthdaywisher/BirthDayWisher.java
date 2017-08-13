/*
 * This is an open source project
 * Anybody can view, download this project
 * Authors : Ram Prasad Gudiwada
 *  
 */
package birthdaywisher;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;
import java.io.*;
import java.net.URL;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 *
 * @author ram bablu
 */
public class BirthDayWisher {
    JFrame screen;
    JPanel panel;
    JLabel imageLabel;
    boolean DISPLAY = true;
    HashSet<Integer> completedGIFs;
    private final int TOTAL_GIFS = 45;

    public BirthDayWisher() {
        try {
            init();
        } catch (Exception e) {
            System.out.println(e.getCause());
        }
    }
    
    /**
     * Initializes screen
     */
    private void init() {       
        completedGIFs = new HashSet<>();
        
        screen = new JFrame(); // creates the screen
        screen.setLayout(new BorderLayout());
        screen.setTitle("Wishes From Your Beloved Person");
        
        // create the two labels
        imageLabel = new JLabel("", SwingConstants.CENTER);
        
        panel = getNamePanel();
        
        screen.setLocationRelativeTo(null);
        screen.setBackground(Color.WHITE);
        screen.setIconImage(((ImageIcon)getIcon("/label.png")).getImage());
        
        screen.addWindowListener( new WindowAdapter() {
            @Override
            public void windowClosing( WindowEvent we) {
                DISPLAY = false;
                screen.dispose();
                completeDialog();
            }
        });
        
        try {
            playSound(); // starts the BGM
            infiniteLoop();
        } catch (Exception ex) {
            Logger.getLogger(BirthDayWisher.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(screen, "Application Ran out of Resources", "Error", 0, getIcon("/error_icon.png"));
        }
    }
    
    /**
     * As long as <em>DISPLAY</em> is true, this method calls <em>change()</em> method
     * @throws Exception 
     */
    private void infiniteLoop() throws Exception {
        while(DISPLAY){
            System.out.println("loop.");
            change();
        }
        System.out.println("Out of this loop");
    }
    
    /**
     * Changes the current .gif file on the screen
     * @throws Exception 
     */
    private void change() throws Exception {
        
        long randomNum = randomVal(TOTAL_GIFS);
        if( completedGIFs.size() >= TOTAL_GIFS ) {
            completedGIFs.removeIf( i -> i % (randomVal(4)+1) > 0);
        }
        while( completedGIFs.contains((int)randomNum) ) {
            System.out.println("Duplicate :"+randomNum);
            randomNum = randomVal(TOTAL_GIFS);
        }
        completedGIFs.add((int)randomNum);
        String fileName = "/HBD"+randomNum+".gif";
        System.out.println("Path :"+fileName);
        imageLabel.setIcon(getIcon(fileName));
        customizeScreen();
        Thread.sleep(3000);
        System.out.println("--Done--");
    }
    
    /**
     * Does appropriate fixes after changing the images
     */
    public void customizeScreen() {
        screen.getContentPane().removeAll();
        screen.add(imageLabel, java.awt.BorderLayout.CENTER);
        screen.add(panel, java.awt.BorderLayout.SOUTH);
        screen.setDefaultCloseOperation(3);
        screen.setResizable(false);
        screen.setSize(570, 470);
        screen.setLocationRelativeTo(null);
        screen.setVisible(true);
    }
    
    /**
     * It returns a panel that consists of both name of the
     * birth day boy and close button.
     * @return 
     */
    private JPanel getNamePanel() {
        JPanel p = new JPanel( new GridLayout(2,1) );
        String name = getName().trim();
        if( name == null ) {
            JOptionPane.showMessageDialog(screen, "Plz Don't do this next time\nBy the way,\nHappy Birth Day", "You Killed Me", 0, getIcon("/sad_icon.png"));
            System.exit(0);
        } else if( name.equals("")) {
            name = "My Friend";
        }
        
        JLabel nameLabel = new JLabel(name.toLowerCase(), SwingConstants.CENTER);
        nameLabel.setForeground(new Color(128,0,128));
        nameLabel.setFont(new java.awt.Font("Callibri",1, 25));
        
        JButton closeButton = new JButton("close");
        closeButton.addActionListener((ActionEvent e) -> {
            DISPLAY = false;
            screen.dispose();
            completeDialog();
            System.exit(0);
        });
        
        p.add(nameLabel);
        p.add(closeButton);
        
        return p;
    }
    
    /**
     * It returns the Icon for given <em>file</em>
     * @param file
     * @return 
     */
    public static Icon getIcon(String file) {
	if(file == null) throw new IllegalArgumentException();
	try {
            URL resource = BirthDayWisher.class.getResource(file);
            ImageIcon icon = new ImageIcon(resource);
            return icon;
        } catch( Exception fne){
            System.out.println("Fuc*ed up dude XDER\n");
	} return null;
    }
    
    /**
     * returns a random value between 0 to <em>i</em>
     * @param i
     * @return 
     */
    public long randomVal(int i){
        return System.currentTimeMillis()%i;
    }
    
    /**
     * Plays sound in the background
     */
    public void playSound() {
        try {
            // To get a sound with your friend name
            // visit    http://www.1happybirthday.com/
            // There you will find birthday song with your name
            
            
            // Those who want to play mp3 file can try this, but dont forget to initialize ToolKit for JavaFX
//            String bip = new File(getClass().getResource("/MyFriend.mp3").toURI()).toURI().toURL().toString();
//            //String path = bip.toString();
//            Media hit = new Media(bip);    
//            MediaPlayer mediaPlayer = new MediaPlayer(hit);
//            mediaPlayer.play();
//            mediaPlayer.setAutoPlay(true);
            
            InputStream path1 = getClass().getResourceAsStream("/MyFriend.wav");
            InputStream path = new BufferedInputStream(path1);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(path);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } 
        catch( UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
            ex.printStackTrace();
            System.out.println("Error with playing sound.\n"+ex.getMessage());
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName());
            BirthDayWisher f = new BirthDayWisher();
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            JOptionPane.showMessageDialog(null,"Application Crashed\nReopen the Application","Error",0);
        }
    }

    private String getName() {
        String  name = (String)JOptionPane.showInputDialog( screen, "This thing doesn't know your name\nTell her your name", "Hey Macha",
                JOptionPane.ERROR_MESSAGE,getIcon("/label.png"),null,"");
        return name;
    }
    
    private void completeDialog() {
        screen.dispose();
        UIManager.put("OptionPane.background", new Color(247,247,247));
        UIManager.getLookAndFeelDefaults().put("Panel.background", new Color(247,247,247));

        JPanel closePanel = new JPanel(new GridLayout(2,1));
        JPanel panel1 = new JPanel(new GridLayout(3,1));
        JPanel panel2 = new JPanel(new GridLayout(2,1));
        
        closePanel.setPreferredSize(new Dimension(350,200));
                
        panel1.setSize(250, 100);
        panel1.add(new JLabel("I wish this birth day will be a memorable one for you"));
        panel1.add(new JLabel("Have a blast my dear friend"));
        
        panel2.setSize(250, 100);
        panel2.add(new JLabel(getIcon("/wow.gif")) );        
        
        closePanel.setSize(350,400);
        closePanel.add(panel1);
        closePanel.add(panel2);
        
        JOptionPane.showMessageDialog( screen,closePanel,
                "Well That's it", JOptionPane.ERROR_MESSAGE, getIcon("/label.png") );
    }
}
