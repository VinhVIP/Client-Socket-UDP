# Đề tài giữa kỳ môn Lập trình mạng

**Đề tài 15: Xây chương trình giao diện socket client – server bằng java với giao thức UDP**
 	
**Client:**

- Nhập vào tên địa chỉ, cổng kết nối với server, nếu không thành công thì thông báo nhập lại còn thành công thì kết nối.

- Sau đó vào giao diện cho phép nhập tên, cổng, username và password của sql gửi lên server để thực hiện kết nối csdl.

- Sau khi server kết nối csdl thành công thì cho phép nhập từng dòng dữ liệu nội dung gồm: họ tên sinh viên, mã sinh viên, điểm thi toán, văn và tiếng anh gửi lên server.

- Hiển thị kết quả server trả về gồm họ tên sinh viên, mã sinh viên và điểm trung bình của từng bạn lên màn hình
 	
**Server:** 

- Nhận thông tin sql từ client và kết nối sql server.

- Nhận dữ liệu từ client thực hiện mã hóa bằng thuật toán DES sai đó lưu vào sql và nhận thông số từ giao diện để đọc giải mã dữ liệu, sau đó tính điểm trung bình của từng bạn và gửi về cho client.


# Script tạo table

```sql
CREATE DATABASE LTM;

CREATE TABLE STUDENT(
	MSV nvarchar(50),
	TEN nvarchar(50) not null,
	TOAN nvarchar(50) not null,
	VAN nvarchar(50) not null,
	ANH nvarchar(50) not null,

	PRIMARY KEY(MSV)
)
```

# Chạy chương trình

Chạy 2 file **Server.java** và **Client.java**