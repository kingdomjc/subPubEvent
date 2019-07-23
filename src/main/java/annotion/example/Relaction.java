package annotion.example;


import annotion.DOMPublisher;

public class Relaction {

    public static void main(String[] args)  {
        Student student=new Student();
        student.setId(1);
        student.setName("sdfsd");
        DOMPublisher.publish(student);
    }


}
