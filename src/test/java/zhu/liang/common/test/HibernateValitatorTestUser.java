package zhu.liang.common.test;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.URL;

import zhu.liang.common.util.HibernateValidatorUtil;


public class HibernateValitatorTestUser {
	
	private int id;
	
	@NotNull(message = "姓名不能为空！")
	private String name;
	
	@NotNull(message = "地址不能为空！")
	private String address;
	
	@Size(max = 11, min = 11, message = "长度只能为11位！")
	private String phoneNumber;
	
	@Email(message = "email地址无效！")
	@NotNull(message = "email地址不能为空！")
	private String email;
	
	@Min(value = 18, message = "必须年满18岁！")
	@Max(value = 30, message = "年龄不能大于30岁！")
	private int age;
	
	@URL(message = "无效的URL地址")
	@NotNull(message = "URL不能为空！")
	private String url;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}


	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	
	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}


	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public static void main(String args[]) {
		HibernateValitatorTestUser model = new HibernateValitatorTestUser();
		model.setName("hello");
		model.setEmail("testHibernateValidate@sohu.com");
		model.setAddress("湖南长沙");
		model.setPhoneNumber("15216808798");

	}
}