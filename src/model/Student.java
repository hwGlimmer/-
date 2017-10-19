package model;
/**
 * 学生基类
 * @author Glimmer
 *
 */
public class Student {
	
	private int stuid;
	private  String name;
	private String sex;
	private String pwd;
	private String email;
	private int stuid_fk;
        
	private Float chinese;
	private Float math;
	private Float english;
	public Student() {
		super();
	}
	public Student(int stuid, String sex, String pwd, String name,
			String email, int stuid_fk, Float chinese, Float math,
			Float english) {
		super();
		this.stuid = stuid;
		this.sex = sex;
		this.pwd = pwd;
		this.name = name;
		this.email = email;
		this.stuid_fk = stuid_fk;
		this.chinese = chinese;
		this.math = math;
		this.english = english;
	}
	public int getStuid() {
		return stuid;
	}
	public void setStuid(int stuid) {
		this.stuid = stuid;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getStuid_fk() {
		return stuid_fk;
	}
	public void setStuid_fk(int stuid_fk2) {
		this.stuid_fk = stuid_fk2;
	}
	public Float getChinese() {
		return chinese;
	}
	public void setChinese(Float chinese) {
		this.chinese = chinese;
	}
	public Float getMath() {
		return math;
	}
	public void setMath(Float math) {
		this.math = math;
	}
	public Float getEnglish() {
		return english;
	}
	@Override
	public String toString() {
		return "学号：" + stuid + ", 姓名：" + name + ", 性别：" + sex
				+ ", 密码：" + pwd + ", 邮箱：" + email + "语文：" + chinese + ", 数学：" + math
				+ ", 英语：" + english;
	}
	public void setEnglish(Float english) {
		this.english = english;
	}
}
