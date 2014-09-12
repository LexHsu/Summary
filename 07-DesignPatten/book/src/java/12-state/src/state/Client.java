package state;


import state.context.Work;

public class Client {
    public static void main(String[] args) {
        Work work = new Work();
        work.setHour(9);
        work.writeProgram();
        
        work.setHour(14);
        work.writeProgram();
        
        work.setHour(17);
        work.setFinish(true);
        work.writeProgram();
        
        work.setHour(19);
        work.writeProgram();
    }
}