package annotion.example;

import annotion.Observers;

public class Application {

    @Observers
    public static void receive(Student student){
        System.out.println(student);
    }
}
