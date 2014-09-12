package shallowcopy;

public class Resume implements Cloneable {
 private String mName;
 private String mSex;
 private String mAge;
 private Experience work = new Experience();

 public Resume(String name) {
     this.mName = name;
 }

 public void setPersonalInfo(String sex, String age) {
     this.mSex = sex;
     this.mAge = age;
 }

 public void setWorkExperience(String workDate, String company) {
     work.setWorkDate(workDate);
     work.setCompany(company);
 }

 public void display() {
     System.out.println(mName + "     " + mSex + "     " + mAge);
     System.out.println("Experience：" + work.getWorkDate() + " "
             + work.getCompany());
 }

 public Resume clone() throws CloneNotSupportedException {
     return (Resume) super.clone();
 }
}