package vinh.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Vinh
 */
public class Database {

    private Connection conn;
    private final String dbURL;

    public Database(String name, int port, String user, String pass) {
        dbURL = String.format("jdbc:sqlserver://%s:%d;user=%s;password=%s;Database=LTM", name, port, user, pass);
    }

    /**
     * Kết nối tới database
     *
     * @return tình trạng kết nối
     */
    public boolean connect() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            conn = DriverManager.getConnection(dbURL);

            System.out.println("Ket noi csdl thanh cong!");
            return true;
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            conn = null;
            return false;
        }
    }

    /**
     * Thêm dữ liệu điểm sinh viên vào database
     *
     * @param msv
     * @param ten
     * @param toan
     * @param van
     * @param anh
     * @return kết quả lệnh thêm
     */
    public boolean add(String msv, String ten, float toan, float van, float anh) {
        try {
            String sql = "INSERT INTO STUDENT (MSV, TEN, TOAN, VAN, ANH) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);

            String enMsv = DES.encrypt(msv, Server.secretKey);
            String enTen = DES.encrypt(ten, Server.secretKey);
            String enToan = DES.encrypt(toan + "", Server.secretKey);
            String enVan = DES.encrypt(van + "", Server.secretKey);
            String enAnh = DES.encrypt(anh + "", Server.secretKey);

            ps.setString(1, enMsv);
            ps.setString(2, enTen);
            ps.setString(3, enToan);
            ps.setString(4, enVan);
            ps.setString(5, enAnh);

            ps.execute();

            System.out.println("them thanh cong!");
            return true;

        } catch (SQLException ex) {
            System.out.println("MSV bị trùng");
            return false;
        }

    }

    /**
     * Lấy danh sách tất cả điểm sinh viên từ database
     *
     * @return danh sách
     */
    public List<Student> getListStudents() {
        List<Student> list = new ArrayList<>();

        try {
            String sql = "SELECT * FROM STUDENT ORDER BY MSV ASC";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                String msv = DES.decrypt(rs.getString(1), Server.secretKey);
                String ten = DES.decrypt(rs.getString(2), Server.secretKey);
                float toan = Float.parseFloat(DES.decrypt(rs.getString(3), Server.secretKey));
                float van = Float.parseFloat(DES.decrypt(rs.getString(4), Server.secretKey));
                float anh = Float.parseFloat(DES.decrypt(rs.getString(5), Server.secretKey));

                Student s = new Student(msv, ten, (toan + van + anh) / 3);
                list.add(s);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Sắp xếp danh sách theo Mã sinh viên tăng dần
        list.sort((Student s1, Student s2) -> {
            return s1.getMaSV().compareTo(s2.getMaSV());
        });

        return list;
    }
}
