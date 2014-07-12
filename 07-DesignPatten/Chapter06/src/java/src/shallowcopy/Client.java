package shallowcopy;

import prototype.Resume;

public class Client {

    public static void main(String[] args) throws CloneNotSupportedException {
        Resume resume = new Resume("Jack");
        resume.setPersonalInfo("Man", "29");
        resume.setWorkExperience("1998-2000", "Google");

        Resume resumeA = resume.clone();
        resumeA.setWorkExperience("1998-2006", "Facebook");

        Resume resumeB = resume.clone();
        resumeB.setPersonalInfo("Man", "24");

        resume.display();
        resumeA.display();
        resumeB.display();
    }
}