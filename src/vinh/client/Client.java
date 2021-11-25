package vinh.client;

/**
 *
 * @author Vinh
 */
public class Client {

    public static void main(String[] args) throws Exception {
//        UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
//        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        new ServerConnection().setVisible(true);
    }
   
}
