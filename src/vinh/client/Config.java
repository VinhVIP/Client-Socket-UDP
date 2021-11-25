package vinh.client;

import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;

/**
 *
 * @author Vinh
 */
public class Config {

    public static final String ACT_CONNECT = "connect_server";
    public static final String ACT_LOGIN = "login_sql";
    public static final String ACT_ADD = "add";
    public static final String ACT_LIST = "list";

    public static final int OK = 200;
    public static final int FAIL = 400;

    public static String serverName;
    public static int serverPort;

    public static String sqlName;
    public static int sqlPort;
    public static String sqlUser;
    public static String sqlPass;

    public static void centerScreen(JFrame frame) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(screenSize.width / 2 - frame.getWidth() / 2, screenSize.height / 2 - frame.getHeight() / 2);
    }

    public static boolean isEmpty(String s) {
        return s == null || s.trim().length() == 0;
    }

    public static String formatName(String name) {
        String[] a = name.trim().split("\\s+");
        String res = "";
        for (String s : a) {
            res += s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase() + " ";
        }
        return res.trim();
    }
}
