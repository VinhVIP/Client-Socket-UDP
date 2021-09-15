/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vinh.client;

/**
 *
 * @author Vinh
 */
public class Client {

    public static void main(String[] args) throws Exception {
//        UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
//        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        new ConnectorFrame().setVisible(true);
    }
   
}
