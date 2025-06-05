# Ứng dụng Quản Lý Vé Tàu Lửa Tại Nhà Ga

## Giới thiệu
Đây là một ứng dụng Java được phát triển trong khuôn khổ môn học Phát triển ứng dụng, nhằm hỗ trợ việc quản lý quy trình bán vé tàu tại nhà ga. Ứng dụng được xây dựng dưới dạng ứng dụng desktop sử dụng Java Swing, kết nối với cơ sở dữ liệu SQL Server, cho phép người dùng tìm kiếm chuyến tàu, đặt vé, và quản lý thông tin liên quan đến nhân viên, tài khoản, hóa đơn và vé đã đặt.

### Tính năng chính
1. Tìm kiếm chuyến tàu
Hỗ trợ tìm kiếm các chuyến tàu theo ngày khởi hành, ga đi, ga đến.

Chỉ hiển thị các chuyến phù hợp với tiêu chí đã chọn.

2. Đặt vé tàu
Cho phép người dùng chọn chuyến tàu và số ghế để đặt vé.

Đảm bảo mỗi vé đặt là duy nhất, không trùng ghế hoặc thời gian.

3. Quản lý nhân viên (dành cho người quản lý)
Thêm, sửa, xóa thông tin nhân viên.

Phân quyền người dùng theo vai trò (quản lý, nhân viên bán vé).

4. Quản lý tài khoản đăng nhập
Tạo và chỉnh sửa tài khoản đăng nhập cho nhân viên.

Kiểm tra và xác thực người dùng khi đăng nhập.

5. Quản lý hóa đơn
Tạo hóa đơn tương ứng sau mỗi lần đặt vé.

Cho phép tra cứu, theo dõi các hóa đơn đã lập.

6. Quản lý vé tàu đã đặt
Hiển thị danh sách tất cả vé đã đặt.

Hỗ trợ lọc, tìm kiếm vé theo ngày, chuyến, hoặc khách hàng.

7. In vé tàu
Sau khi đặt vé thành công, hệ thống hỗ trợ in vé với đầy đủ thông tin chuyến đi.

### Công nghệ sử dụng
Ngôn ngữ lập trình: Java

### Giao diện người dùng: Java Swing

### Cơ sở dữ liệu: SQL Server

### Kiểu kết nối: JDBC

Cài đặt và sử dụng
Yêu cầu hệ thống
JDK 8 trở lên

SQL Server 2019 hoặc mới hơn

IDE hỗ trợ Java (IntelliJ IDEA, Eclipse, NetBeans, v.v.)

### Hướng dẫn cài đặt
Clone repository về máy:
git clone https://github.com/minh10102003/phat-trien-ung-dung-k17

Mở project trong IDE Java và cấu hình kết nối đến cơ sở dữ liệu trong file cấu hình tương ứng.

Khởi tạo cơ sở dữ liệu:

Tạo database QuanLyVeTau trong SQL Server.

Sử dụng file script .sql đi kèm để tạo bảng và dữ liệu mẫu (nếu có).

Chạy chương trình từ file Main.java.

### Tên đề tài: Quản lý vé tàu lửa tại nhà ga

### Môn học: Phát triển ứng dụng

Ghi chú
Dự án được phát triển với mục đích học tập.

Vui lòng liên hệ nhóm nếu muốn tái sử dụng hoặc phát triển thêm.
