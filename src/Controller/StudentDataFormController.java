package Controller;


import View.tm.StudentTM;
import com.jfoenix.controls.JFXTextField;

public class StudentDataFormController {
    public JFXTextField txtName;

    public void setData(StudentTM tm){

        txtName.setText(tm.getName());
        System.out.println(tm);

    }

}
