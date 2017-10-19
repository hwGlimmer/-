package util;

/**
 * ���ݿ����������
 */
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import model.Student;

public class JDBCUtils {

    private static Properties pp = null;// ����Properties�ļ�����
    private static FileInputStream fis = null;// �����ļ�������

    // ��ȡProperties�ļ�Ҫ�õ�������
    private static String url = null;
    private static String user = null;
    private static String password = null;
    private static String driver = null;

    // �ֱ���Connection��PreparedStatement��ResultSet����
    private static Connection ct = null;
    private static PreparedStatement ps = null;
    private static ResultSet rs = null;

// 1.�����������ȡproperties�ļ���Ϣֻ�ü���һ�Σ����Խ��������static�������
    // ��db.properties�ж�ȡ��Ӧ��������Ϣ
    static {
        try {

            pp = new Properties();// ����Properties����
            fis = new FileInputStream("conf\\db.properties");
            pp.load(fis);

            driver = pp.getProperty("driver");
            url = pp.getProperty("url");
            user = pp.getProperty("user");
            password = pp.getProperty("password");

            // ��������
            Class.forName(driver);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {

                try {
                    fis.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                fis = null;
            }

        }
    }

    // 2.��װ������ӵķ���getConnection();
    public static Connection getConnection() {
        try {
            ct = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ct;
    }

    // 3.��װһ���ر���Դ�ķ���close()
    public static void close(ResultSet rs, PreparedStatement ps, Connection ct) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            rs = null;// ��rs��Ϊ�գ�ʹ���������ջ��ƻ���
        }
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            ps = null;
        }
        if (ct != null) {

            try {
                ct.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            ct = null;
        }
    }

    // 4.��װһ������ѧ�Ų�ѯѧ���ķ���
    public static Student findById(int stuid) throws Exception {
        Student stu = null;
        ct = getConnection();// ע��������������Լ���װ�ķ���
        String sql = "select student.stuid,student.name,student.sex,student.pwd,student.email,grade.chinese,grade.math,grade.english from student left join grade on student.stuid=grade.stuid_fk where stuid=?;";
        try {
            ps = ct.prepareStatement(sql);
            ps.setInt(1, stuid);
            rs = ps.executeQuery(); // ִ�в�ѯ����
            if (rs.next()) {
                stu = new Student();
                stu.setStuid(rs.getInt(1));
                stu.setName(rs.getString(2));
                stu.setSex(rs.getString(3));
                stu.setPwd(rs.getString(4));
                stu.setEmail(rs.getString(5));
                //�ж��Ƿ��з���,���ûû�з��� ��ֵ��Ϊ0000��ʾû�з���
                float gradenull = 10000;
                String tchinese = rs.getString(6);
                String tmath = rs.getString(7);
                String tenglish = rs.getString(8);

                if ((tchinese != null || tmath != null) || tenglish != null) {
                    stu.setChinese(rs.getFloat(6));
                    stu.setMath(rs.getFloat(7));
                    stu.setEnglish(rs.getFloat(8));
                } else {
                    stu.setChinese(gradenull);
                    stu.setMath(gradenull);
                    stu.setEnglish(gradenull);
                }

            }
        } catch (Exception e) {
            throw e;
        } finally { // ��������׳������տ϶���Ҫ�������ݿ�Ĺرղ�����
            close(rs, ps, ct);
        }
        return stu;
    }

    // 5. ��װһ����ѯȫ��ѧ���ķ���
    public static List<Student> findAll() throws Exception {
        List<Student> all = new ArrayList<Student>();

        try {
            ct = getConnection();// ע��������������Լ���װ�ķ���
            String sql = "select student.stuid,student.name,student.sex,student.pwd,student.email,grade.chinese,grade.math,grade.english from student left join grade on student.stuid=grade.stuid_fk;";
            ps = ct.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Student stu = new Student();
                stu.setStuid(rs.getInt(1));
                stu.setName(rs.getString(2));
                stu.setSex(rs.getString(3));
                stu.setPwd(rs.getString(4));
                stu.setEmail(rs.getString(5));

                float gradenull = 10000;
                String tchinese = rs.getString(6);
                String tmath = rs.getString(7);
                String tenglish = rs.getString(8);

                if ((tchinese != null || tmath != null) || tenglish != null) {
                    stu.setChinese(rs.getFloat(6));
                    stu.setMath(rs.getFloat(7));
                    stu.setEnglish(rs.getFloat(8));
                } else {
                    stu.setChinese(gradenull);
                    stu.setMath(gradenull);
                    stu.setEnglish(gradenull);
                }
                all.add(stu); // ���е������򼯺��в���
            }
        } catch (Exception e) {
            throw e;
        } finally { // ��������׳������տ϶���Ҫ�������ݿ�Ĺرղ�����
            close(rs, ps, ct);
        }
        return all;
    }

    // 6.��װһ����½��֤�ķ���
    public static boolean IsLogin(String name, String pwd) throws Exception {
        boolean flag = false;
        ct = getConnection();// ע��������������Լ���װ�ķ���
        String sql = "select name,pwd from student where name=? and pwd=?";
        try {
            ps = ct.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, pwd);
            rs = ps.executeQuery(); // ִ�в�ѯ����
            if (rs.next()) {
                flag = true;
            }
        } catch (Exception e) {
            throw e;
        } finally { // ��������׳������տ϶���Ҫ�������ݿ�Ĺرղ�����
            close(rs, ps, ct);
        }
        return flag;
    }

    // 7.��װһ������ѧ���ķ���
    public static boolean doCreate(Student stu) {
        boolean flag = false;
        try {
            ct = getConnection();// ע��������������Լ���װ�ķ���
            String sql = "insert into student(name,sex,pwd,email)values(?,?,?,?)";
            ps = ct.prepareStatement(sql);

            ps.setString(1, stu.getName()); // ���е����ݴ�user����ȡ��
            ps.setString(2, stu.getSex()); // ���е����ݴ�user����ȡ��
            ps.setString(3, stu.getPwd()); // ���е����ݴ�user����ȡ��
            ps.setString(4, stu.getEmail()); // ���е����ݴ�user����ȡ��

            int sum = ps.executeUpdate();
            if (sum > 0) {
                flag = true;
            } else {
                flag = false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(rs, ps, ct);
        }
        return flag;

    }

    // 8.��װһ��¼������ķ���
    public static boolean doCreate2(Student stu) {
        boolean flag = false;
        String sql = "insert into grade(stuid_fk,chinese,math,english)values(?,?,?,?)";
        try {
            ct = getConnection();// ע��������������Լ���װ�ķ���
            ps = ct.prepareStatement(sql);

            ps.setInt(1, stu.getStuid_fk()); // ���е����ݴ�user����ȡ��
            ps.setFloat(2, stu.getChinese()); // ���е����ݴ�user����ȡ��
            ps.setFloat(3, stu.getMath()); // ���е����ݴ�user����ȡ��
            ps.setFloat(4, stu.getEnglish()); // ���е����ݴ�user����ȡ��

            int sum = ps.executeUpdate();
            if (sum > 0) {
                flag = true;
            } else {
                flag = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(rs, ps, ct);
        }

        return flag;

    }

    // 9.��װһ�� ����ѧ��ѧ��ɾ��ѧ����Ϣ�ķ���
    public static boolean doDelete(int stuid) throws Exception {
        boolean flag = false;
        ct = getConnection();// ע��������������Լ���װ�ķ���
        String sql = "DELETE student,grade FROM student left join grade on student.stuid=grade.stuid_fk where student.stuid=?";
        try {
            ps = ct.prepareStatement(sql);
            ps.setInt(1, stuid);
            if (ps.executeUpdate() > 0) {// �����Ѿ�������һ��
                flag = true;
            }
        } catch (Exception e) {
            throw e;
        } finally { // ��������׳������տ϶���Ҫ�������ݿ�Ĺرղ�����
            close(rs, ps, ct);
        }
        return flag;
    }

    // 10.��װһ���޸�ѧ����Ϣ�ķ���
    public static boolean doUpdate(Student stu) throws Exception {
        boolean flag = false;
        ct = getConnection();

        String sql = "update student left join grade on student.stuid=grade.stuid_fk "
                + "set student.name=?,student.sex=?,student.email=?,grade.chinese=?,"
                + "grade.math=?,grade.english=?where student.stuid=?;";

        try {
            ps = ct.prepareStatement(sql);

            ps.setString(1, stu.getName()); // ���е����ݴ�user����ȡ��
            ps.setString(2, stu.getSex()); // ���е����ݴ�user����ȡ��

            ps.setString(3, stu.getEmail());
            ps.setFloat(4, stu.getChinese());
            ps.setFloat(5, stu.getMath());
            ps.setFloat(6, stu.getEnglish());
            ps.setInt(7, stu.getStuid());

            if (ps.executeUpdate() > 0) {// �����Ѿ�������һ��
                flag = true;
            }
        } catch (Exception e) {
            throw e;
        } finally { // ��������׳������տ϶���Ҫ�������ݿ�Ĺرղ�����
            close(rs, ps, ct);
        }
        return flag;
    }

    // 11.�õ���������
    public static ResultSet getRs() {
        return rs;
    }

    public static PreparedStatement getPs() {
        return ps;
    }

    public static Connection getCt() {
        return ct;
    }

    //12.��ѯ�ķ���
    public static ResultSet executeQuery(String sql) {
        try {
            ct = getConnection();// ע��������������Լ���װ�ķ���
            ps = ct.prepareStatement(sql);
            rs = ps.executeQuery();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return rs;
    }

}
