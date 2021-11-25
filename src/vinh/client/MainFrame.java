package vinh.client;

import java.awt.HeadlessException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author Vinh
 */
public class MainFrame extends javax.swing.JFrame {

    public MainFrame() {
        initComponents();
        setTitle("Điểm");
        Config.centerScreen(this);
        
        // Hiển thị danh sách sinh viên khi vừa mở frame
        getListStudent();
    }

    /**
     * Gửi yêu cầu thêm điểm sinh viên lên server
     * 
     * @param msv
     * @param ten
     * @param toan
     * @param van
     * @param anh 
     */
    public void add(String msv, String ten, float toan, float van, float anh) {
        DatagramSocket client = null;

        try {
            client = new DatagramSocket();
            InetAddress server = InetAddress.getByName(Config.serverName);
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();

            try (DataOutputStream dout = new DataOutputStream(byteOut)) {
                dout.writeUTF(Config.ACT_ADD);      // lệnh yêu cầu thêm sinh viên

                dout.writeUTF(msv.toUpperCase().trim());    // in hoa mã sinh viên
                dout.writeUTF(Config.formatName(ten));      // chuẩn hóa tên
                dout.writeFloat(toan);
                dout.writeFloat(van);
                dout.writeFloat(anh);
            }

            byte[] bytes = byteOut.toByteArray();

            DatagramPacket out = new DatagramPacket(bytes, bytes.length, server, Config.serverPort);
            client.send(out);

            System.out.println("Da gui du lieu len server");

            byte[] buffer = new byte[1024];
            DatagramPacket in = new DatagramPacket(buffer, buffer.length);
            client.receive(in);

            byte[] data = in.getData();
            ByteArrayInputStream byteIn = new ByteArrayInputStream(data);
            DataInputStream din = new DataInputStream(byteIn);

            int statusCode = din.readInt();
            if (statusCode == Config.OK) {
                // Thêm thành công
                
                // Load lại danh sách sinh viên
                getListStudent();
                
                // Reset các ô nhập liệu
                tfMSV.setText("");
                tfHoTen.setText("");
                tfToan.setText("");
                tfVan.setText("");
                tfAnh.setText("");
            }

            String message = din.readUTF();
            JOptionPane.showMessageDialog(null, message);
        } catch (HeadlessException | IOException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (client != null) {
                client.close();
            }
        }
    }

    /**
     * Gửi yêu cầu lấy danh sách tất cả điểm sinh viên
     */
    public void getListStudent() {
        DatagramSocket client = null;

        try {
            client = new DatagramSocket();
            InetAddress server = InetAddress.getByName(Config.serverName);
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();

            try (DataOutputStream dout = new DataOutputStream(byteOut)) {
                dout.writeUTF(Config.ACT_LIST);     // lệnh lấy điểm sinh viên
            }

            byte[] bytes = byteOut.toByteArray();

            DatagramPacket out = new DatagramPacket(bytes, bytes.length, server, Config.serverPort);
            client.send(out);

            System.out.println("Da gui yeu cau lay danh sach len server");

            byte[] buffer = new byte[1024];
            DatagramPacket in = new DatagramPacket(buffer, buffer.length);
            client.receive(in);

            byte[] data = in.getData();
            ByteArrayInputStream byteIn = new ByteArrayInputStream(data);
            DataInputStream din = new DataInputStream(byteIn);

            putDataIntoTable(din);

        } catch (HeadlessException | IOException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (client != null) {
                client.close();
            }
        }
    }

    /**
     * Lấy dữ liệu từ server trả về để thêm vào table
     * 
     * @param din   Data từ server trả về
     * @throws IOException 
     */
    private void putDataIntoTable(DataInputStream din) throws IOException {
        int size = din.readInt();
        Vector title = new Vector();
        title.add("Mã SV");
        title.add("Họ tên");
        title.add("Điểm trung bình");

        Vector rows = new Vector();
        while (size-- > 0) {
            Vector v = new Vector();
            v.add(din.readUTF());
            v.add(din.readUTF());
            v.add(String.format("%.1f", din.readFloat()));

            rows.add(v);
        }

        TableModel model = new DefaultTableModel(rows, title) {
            @Override
            public boolean isCellEditable(int i, int i1) {
                return false;
            }

        };
        table.setModel(model);
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        tfMSV = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        tfHoTen = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        tfToan = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        tfVan = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        tfAnh = new javax.swing.JTextField();
        btnAdd = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        jLabel7 = new javax.swing.JLabel();
        btnRefresh = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 22)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(51, 51, 255));
        jLabel1.setText("NHẬP ĐIỂM SINH VIÊN");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel2.setText("Mã SV:");

        tfMSV.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel3.setText("Họ tên:");

        tfHoTen.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel4.setText("Toán:");

        tfToan.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel5.setText("Văn:");

        tfVan.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel6.setText("Anh:");

        tfAnh.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        btnAdd.setBackground(new java.awt.Color(51, 51, 255));
        btnAdd.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnAdd.setForeground(new java.awt.Color(255, 255, 255));
        btnAdd.setText("Thêm");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6))
                .addGap(42, 42, 42)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(tfVan, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 211, Short.MAX_VALUE)
                    .addComponent(tfToan, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tfHoTen, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tfMSV, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tfAnh))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(147, Short.MAX_VALUE)
                .addComponent(btnAdd)
                .addGap(142, 142, 142))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(tfMSV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(tfHoTen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(tfToan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(tfVan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(tfAnh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 37, Short.MAX_VALUE)
                .addComponent(btnAdd)
                .addGap(28, 28, 28))
        );

        jLabel8.setFont(new java.awt.Font("Tahoma", 3, 12)); // NOI18N
        jLabel8.setText("Trần Quang Vinh");

        jLabel9.setFont(new java.awt.Font("Tahoma", 3, 12)); // NOI18N
        jLabel9.setText("N18DCCN246");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(58, 58, 58)
                        .addComponent(jLabel1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel8))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel9)))
                .addContainerGap(13, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addComponent(jLabel1)
                .addGap(53, 53, 53)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel9))
        );

        table.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Mã sinh viên", "Họ tên", "Điểm trung bình"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Float.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        table.setRowHeight(20);
        table.setSelectionBackground(new java.awt.Color(204, 255, 204));
        jScrollPane1.setViewportView(table);

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 22)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(51, 51, 255));
        jLabel7.setText("ĐIỂM TRUNG BÌNH");

        btnRefresh.setBackground(new java.awt.Color(0, 255, 153));
        btnRefresh.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnRefresh.setText("Refresh");
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addGap(96, 96, 96)
                        .addComponent(btnRefresh)
                        .addGap(29, 29, 29))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 564, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(btnRefresh))
                .addGap(24, 24, 24)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 461, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        String msv = tfMSV.getText();
        String ten = tfHoTen.getText();

        // Kiểm tra các ràng buộc
        if (Config.isEmpty(msv)) {
            JOptionPane.showMessageDialog(null, "Mã sinh viên không được bỏ trống");
            return;
        }
        if (Config.isEmpty(ten)) {
            JOptionPane.showMessageDialog(null, "Tên sinh viên không được bỏ trống");
            return;
        }

        String messToan = checkMark(tfToan.getText(), "Toán");
        String messVan = checkMark(tfVan.getText(), "Văn");
        String messAnh = checkMark(tfAnh.getText(), "Anh");

        if (messToan != null) {
            JOptionPane.showMessageDialog(null, messToan);
            return;
        }
        if (messVan != null) {
            JOptionPane.showMessageDialog(null, messVan);
            return;
        }
        if (messAnh != null) {
            JOptionPane.showMessageDialog(null, messAnh);
            return;
        }
        
        float toan, van, anh;
        toan = Float.parseFloat(tfToan.getText());
        van = Float.parseFloat(tfVan.getText());
        anh = Float.parseFloat(tfAnh.getText());

        // Gửi dữ liệu để gửi lên server
        add(msv, ten, toan, van, anh);

        // Cập nhật lai danh sách điểm sinh viên
        getListStudent();

    }//GEN-LAST:event_btnAddActionPerformed

    /**
     * Kiểm tra các ràng buộc về điểm
     * 
     * @param mark  giá trụ điểm    
     * @param name  môn học tương ứng với điểm đó
     * @return      thông báo lỗi nếu có
     */
    private String checkMark(String mark, String name) {
        if (mark == null || mark.trim().length() == 0) {
            return String.format("Điểm %s không được bỏ trống", name);
        }
        float f;
        try {
            f = Float.parseFloat(mark);
        } catch (NumberFormatException e) {
            return String.format("Định dạng điểm %s không hợp lệ", name);
        }
        if (f < 0 || f > 10) {
            return String.format("Điểm %s chỉ được nằm trong phạm vi [0-10]", name);
        }

        mark = f + "";
        int dot = mark.indexOf(".");
        if (dot != -1) {
            if (mark.length() - dot > 2) {
                return String.format("Điểm %s chỉ được lấy 1 chữ số phần thập phân", name);
            }
        }

        return null;
    }

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        getListStudent();
    }//GEN-LAST:event_btnRefreshActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable table;
    private javax.swing.JTextField tfAnh;
    private javax.swing.JTextField tfHoTen;
    private javax.swing.JTextField tfMSV;
    private javax.swing.JTextField tfToan;
    private javax.swing.JTextField tfVan;
    // End of variables declaration//GEN-END:variables
}
