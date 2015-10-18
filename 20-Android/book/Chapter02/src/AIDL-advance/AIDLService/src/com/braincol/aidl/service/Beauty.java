package com.braincol.aidl.service;

import android.os.Parcel;
import android.os.Parcelable;
/**
 * 
 * @author chenzheng_java
 * @description  Parcelable是android提供的一个比serializable效率更高的序列号接口
 * 				这里必须要继承Parcelable哦，不序列号怎么可以传递……对吧？！
 * 在实体类我们要做两件重要的事情：
 * 第一：实现Parcelable接口
 * 第二：定义一个Parcelable.Creator类型的CREATOR对象
 * 第三：要提供一个Beauty.aidl文件，其中内容为parcelable Beauty，定义了之后，在其他aidl文件中引用Beauty时便不会提示出错了。
 * @since 2011/03/18
 *
 */
public class Beauty implements Parcelable {

	String name ;
	int age ;
	String sex ;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	/**
	 * 将对象序列号
	 * dest 就是对象即将写入的目的对象
	 * flags 有关对象序列号的方式的标识
	 * 这里要注意，写入的顺序要和在createFromParcel方法中读出的顺序完全相同。例如这里先写入的为name，
	 * 那么在createFromParcel就要先读name
	 */
	@Override
	public void writeToParcel(Parcel dest, int flags) {
			
			dest.writeString(name);
			dest.writeInt(age);
			dest.writeString(sex);
	}
	/**
	 * 在想要进行序列号传递的实体类内部一定要声明该常量。常量名只能是CREATOR,类型也必须是
	 * Parcelable.Creator<T>
	 */
	public static final Parcelable.Creator<Beauty> CREATOR = new Creator<Beauty>() {
		
		/**
		 * 创建一个要序列号的实体类的数组，数组中存储的都设置为null
		 */
		@Override
		public Beauty[] newArray(int size) {
			return new Beauty[size];
		}
		
		/***
		 * 根据序列号的Parcel对象，反序列号为原本的实体对象
		 * 读出顺序要和writeToParcel的写入顺序相同
		 */
		@Override
		public Beauty createFromParcel(Parcel source) {
			String name = source.readString();
			int age = source.readInt();
			String sex = source.readString();
			Beauty beauty = new Beauty();
			beauty.setName(name);
			beauty.setAge(age);
			beauty.setSex(sex);
			
			return beauty;
		}
	};
	
	
	
}
