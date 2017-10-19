package util;

/**
 * 数据库操作工具类
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

    private static Properties pp = null;// 定义Properties文件对象
    private static FileInputStream fis = null;// 定义文件输入流

    // 读取Properties文件要用到的属性
    private static String url = null;
    private static String user = null;
    private static String password = null;
    private static String driver = null;

    // 分别定义Connection，PreparedStatement，ResultSet对象
    private static Connection ct = null;
    private static PreparedStatement ps = null;
    private static ResultSet rs = null;

// 1.加载驱动与读取properties文件信息只用加载一次，所以将代码放入static代码块中
    // 从db.properties中读取相应的配置信息
    static {
        try {

            pp = new Properties();// 创建Properties对象
            fis = new FileInputStream("conf\\db.properties");
            pp.load(fis);

            driver = pp.getProperty("driver");
            url = pp.getProperty("url");
            user = pp.getProperty("user");
            password = pp.getProperty("password");

            // 加载驱动
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

    // 2.封装获得链接的方法getConnection();
    public static Connection getConnection() {
        try {
            ct = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ct;
    }

    // 3.封装一个关闭资源的方法close()
    public static void close(ResultSet rs, PreparedStatement ps, Connection ct) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            rs = null;// 将rs置为空，使用垃圾回收机制回收
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

    // 4.封装一个根据学号查询学生的方法
    public static Student findById(int stuid) throws Exception {
        Student stu = null;
        ct = getConnection();// 注意这个方法是我自己封装的方法
        String sql = "select student.stuid,student.name,student.sex,student.pwd,student.email,grade.chinese,grade.math,grade.english from student left join grade on student.stuid=grade.stuid_fk where stuid=?;";
        try {
            ps = ct.prepareStatement(sql);
            ps.setInt(1, stuid);
            rs = ps.executeQuery(); // 执行查询操作
            if (rs.next()) {
                stu = new Student();
                stu.setStuid(rs.getInt(1));
                stu.setName(rs.getString(2));
                stu.setSex(rs.getString(3));
                stu.setPwd(rs.getString(4));
                stu.setEmail(rs.getString(5));
                //判断是否有分数,如果没没有分数 将值设为0000表示没有分数
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
        } finally { // 不管如何抛出，最终肯定是要进行数据库的关闭操作的
            close(rs, ps, ct);
        }
        return stu;
    }

    // 5. 封装一个查询全部学生的方法
    public static List<Student> findAll() throws Exception {
        List<Student> all = new ArrayList<Student>();

        try {
            ct = getConnection();// 注意这个方法是我自己封装的方法
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
                all.add(stu); // 所有的内容向集合中插入
            }
        } catch (Exception e) {
            throw e;
        } finally { // 不管如何抛出，最终肯定是要进行数据库的关闭操作的
            close(rs, ps, ct);
        }
        return all;
    }

    // 6.封装一个登陆验证的方法
    public static boolean IsLogin(String name, String pwd) throws Exception {
        boolean flag = false;
        ct = getConnection();// 注意这个方法是我自己封装的方法
        String sql = "select name,pwd from student where name=? and pwd=?";
        try {
            ps = ct.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, pwd);
            rs = ps.executeQuery(); // 执行查询操作
            if (rs.next()) {
                flag = true;
            }
        } catch (Exception e) {
            throw e;
        } finally { // 不管如何抛出，最终肯定是要进行数据库的关闭操作的
            close(rs, ps, ct);
        }
        return flag;
    }

    // 7.封装一个增加学生的方法
    public static boolean doCreate(Student stu) {
        boolean flag = false;
        try {
            ct = getConnection();// 注意这个方法是我自己封装的方法
            String sql = "insert into student(name,sex,pwd,email)values(?,?,?,?)";
            ps = ct.prepareStatement(sql);

            ps.setString(1, stu.getName()); // 所有的内容从user类中取出
            ps.setString(2, stu.getSex()); // 所有的内容从user类中取出
            ps.setString(3, stu.getPwd()); // 所有的内容从user类中取出
            ps.setString(4, stu.getEmail()); // 所有的内容从user类中取出

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

    // 8.封装一个录入分数的方法
    public static boolean doCreate2(Student stu) {
        boolean flag = false;
        String sql = "insert into grade(stuid_fk,chinese,math,english)values(?,?,?,?)";
        try {
            ct = getConnection();// 注意这个方法是我自己封装的方法
            ps = ct.prepareStatement(sql);

            ps.setInt(1, stu.getStuid_fk()); // 所有的内容从user类中取出
            ps.setFloat(2, stu.getChinese()); // 所有的内容从user类中取出
            ps.setFloat(3, stu.getMath()); // 所有的内容从user类中取出
            ps.setFloat(4, stu.getEnglish()); // 所有的内容从user类中取出

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

    // 9.封装一个 根据学生学号删除学生信息的方法
    public static boolean doDelete(int stuid) throws Exception {
        boolean flag = false;
        ct = getConnection();// 注意这个方法是我自己封装的方法
        String sql = "DELETE student,grade FROM student left join grade on student.stuid=grade.stuid_fk where student.stuid=?";
        try {
            ps = ct.prepareStatement(sql);
            ps.setInt(1, stuid);
            if (ps.executeUpdate() > 0) {// 至少已经更新了一行
                flag = true;
            }
        } catch (Exception e) {
            throw e;
        } finally { // 不管如何抛出，最终肯定是要进行数据库的关闭操作的
            close(rs, ps, ct);
        }
        return flag;
    }

    // 10.封装一个修改学生信息的方法
    public static boolean doUpdate(Student stu) throws Exception {
        boolean flag = false;
        ct = getConnection();

        String sql = "update student left join grade on student.stuid=grade.stuid_fk "
                + "set student.name=?,student.sex=?,student.email=?,grade.chinese=?,"
                + "grade.math=?,grade.english=?where student.stuid=?;";

        try {
            ps = ct.prepareStatement(sql);

            ps.setString(1, stu.getName()); // 所有的内容从user类中取出
            ps.setString(2, stu.getSex()); // 所有的内容从user类中取出

            ps.setString(3, stu.getEmail());
            ps.setFloat(4, stu.getChinese());
            ps.setFloat(5, stu.getMath());
            ps.setFloat(6, stu.getEnglish());
            ps.setInt(7, stu.getStuid());

            if (ps.executeUpdate() > 0) {// 至少已经更新了一行
                flag = true;
            }
        } catch (Exception e) {
            throw e;
        } finally { // 不管如何抛出，最终肯定是要进行数据库的关闭操作的
            close(rs, ps, ct);
        }
        return flag;
    }

    // 11.得到三个对象
    public static ResultSet getRs() {
        return rs;
    }

    public static PreparedStatement getPs() {
        return ps;
    }

    public static Connection getCt() {
        return ct;
    }

    //12.查询的方法
    public static ResultSet executeQuery(String sql) {
        try {
            ct = getConnection();// 注意这个方法是我自己封装的方法
            ps = ct.prepareStatement(sql);
            rs = ps.executeQuery();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return rs;
    }

}
