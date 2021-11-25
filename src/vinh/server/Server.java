package vinh.server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import vinh.client.Config;

/**
 *
 * @author Vinh
 */
public class Server {

    public static void main(String[] args) {
        Server server = new Server(PORT);
        server.run();
    }

    public static final String secretKey = "abcd6789";      // key bí mật dùng cho mã hóa DES
    public static final int PORT = 1234;                    // cổng kết nối server

    DatagramSocket server;
    Database db = null;

    public Server(int port) {
        try {
            server = new DatagramSocket(PORT);
            System.out.println("Server Running...");
        } catch (SocketException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void run() {
        String cmd;
        byte[] buffer = new byte[1024 * 8];

        while (true) {
            try {
                DatagramPacket in = new DatagramPacket(buffer, buffer.length);
                server.receive(in);

                byte[] bytes = in.getData();
                ByteArrayInputStream byteIn = new ByteArrayInputStream(bytes);
                DataInputStream din = new DataInputStream(byteIn);

                cmd = din.readUTF();

                if (cmd.equalsIgnoreCase(Config.ACT_CONNECT)) {
                    // Yêu cầu kết nối tới server

                    response(Config.OK, in.getAddress(), in.getPort());

                } else if (cmd.equalsIgnoreCase(Config.ACT_LOGIN)) {
                    // Yêu cầu đăng nhập vào database

                    String name = din.readUTF();
                    int port = din.readInt();
                    String user = din.readUTF();
                    String pass = din.readUTF();

                    db = new Database(name, port, user, pass);

                    if (db.connect()) {
                        response(Config.OK, in.getAddress(), in.getPort());
                    } else {
                        response(Config.FAIL, in.getAddress(), in.getPort());
                    }
                } else if (cmd.equalsIgnoreCase(Config.ACT_ADD)) {
                    // Yêu cầu thêm dữ liệu điểm sinh viên

                    String msv = din.readUTF();
                    String ten = din.readUTF();
                    float toan = din.readFloat();
                    float van = din.readFloat();
                    float anh = din.readFloat();

                    if (db != null) {
                        boolean add = db.add(msv, ten, toan, van, anh);

                        if (add) {
                            System.out.println("them thanh cong!");
                            sendToClient(Config.OK, "Them thanh cong", in.getAddress(), in.getPort());

                        } else {
                            System.out.println("MSV bi trung, k the them");
                            sendToClient(Config.FAIL, "MSV bi trung, k the them", in.getAddress(), in.getPort());
                        }
                    } else {
                        System.out.println("Chua ket noi toi csdl");
                    }
                } else if (cmd.equalsIgnoreCase(Config.ACT_LIST)) {
                    // Yêu cầu lấy danh sách tất cả điểm sinh viên

                    List<Student> list = db.getListStudents();
                    sendListStudents(list, in.getAddress(), in.getPort());
                }
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Phản hồi từ server về lại client
     *
     * @param statusCode mã trạng thái
     * @param address địa chỉ
     * @param port cổng
     */
    public void response(int statusCode, InetAddress address, int port) {
        try {
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            try (DataOutputStream dout = new DataOutputStream(byteOut)) {
                dout.writeInt(statusCode);
            }

            byte[] data = byteOut.toByteArray();
            DatagramPacket out = new DatagramPacket(data, data.length, address, port);
            server.send(out);
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Chuyển danh sách điểm sinh viên vào gói tin để gửi về client
     *
     * @param list danh sách điểm sinh viên
     * @param address
     * @param port
     */
    public void sendListStudents(List<Student> list, InetAddress address, int port) {
        try {
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            try (DataOutputStream dout = new DataOutputStream(byteOut)) {
                dout.writeInt(list.size());

                for (Student s : list) {
                    dout.writeUTF(s.getMaSV());
                    dout.writeUTF(s.getHoTen());
                    dout.writeFloat(s.getDiemTB());
                }
            }

            byte[] data = byteOut.toByteArray();
            DatagramPacket out = new DatagramPacket(data, data.length, address, port);
            server.send(out);
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Phản hồi về cho client thông điệp nào đấy
     *
     * @param statusCode max trạng thái
     * @param message thông điệp
     * @param address
     * @param port
     */
    public void sendToClient(int statusCode, String message, InetAddress address, int port) {
        try {
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            try (DataOutputStream dout = new DataOutputStream(byteOut)) {
                dout.writeInt(statusCode);
                dout.writeUTF(message);
            }

            byte[] data = byteOut.toByteArray();
            DatagramPacket out = new DatagramPacket(data, data.length, address, port);
            server.send(out);
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
