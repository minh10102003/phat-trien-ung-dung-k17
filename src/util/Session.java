package util;

/**
 * Giữ lại thông tin username vừa đăng nhập thành công
 */
public class Session {
    // chỉ lưu username, sẽ dùng để lookup ra maNV
    public static String currentUsername;
    public static String currentRole;
}
