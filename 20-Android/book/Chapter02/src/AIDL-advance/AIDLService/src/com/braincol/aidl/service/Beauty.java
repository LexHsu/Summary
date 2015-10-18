package com.braincol.aidl.service;

import android.os.Parcel;
import android.os.Parcelable;
/**
 * 
 * @author chenzheng_java
 * @description  Parcelable��android�ṩ��һ����serializableЧ�ʸ��ߵ����кŽӿ�
 * 				�������Ҫ�̳�ParcelableŶ�������к���ô���Դ��ݡ����԰ɣ���
 * ��ʵ��������Ҫ��������Ҫ�����飺
 * ��һ��ʵ��Parcelable�ӿ�
 * �ڶ�������һ��Parcelable.Creator���͵�CREATOR����
 * ������Ҫ�ṩһ��Beauty.aidl�ļ�����������Ϊparcelable Beauty��������֮��������aidl�ļ�������Beautyʱ�㲻����ʾ�����ˡ�
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
	 * ���������к�
	 * dest ���Ƕ��󼴽�д���Ŀ�Ķ���
	 * flags �йض������кŵķ�ʽ�ı�ʶ
	 * ����Ҫע�⣬д���˳��Ҫ����createFromParcel�����ж�����˳����ȫ��ͬ������������д���Ϊname��
	 * ��ô��createFromParcel��Ҫ�ȶ�name
	 */
	@Override
	public void writeToParcel(Parcel dest, int flags) {
			
			dest.writeString(name);
			dest.writeInt(age);
			dest.writeString(sex);
	}
	/**
	 * ����Ҫ�������кŴ��ݵ�ʵ�����ڲ�һ��Ҫ�����ó�����������ֻ����CREATOR,����Ҳ������
	 * Parcelable.Creator<T>
	 */
	public static final Parcelable.Creator<Beauty> CREATOR = new Creator<Beauty>() {
		
		/**
		 * ����һ��Ҫ���кŵ�ʵ��������飬�����д洢�Ķ�����Ϊnull
		 */
		@Override
		public Beauty[] newArray(int size) {
			return new Beauty[size];
		}
		
		/***
		 * �������кŵ�Parcel���󣬷����к�Ϊԭ����ʵ�����
		 * ����˳��Ҫ��writeToParcel��д��˳����ͬ
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
