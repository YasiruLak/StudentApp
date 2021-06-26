package Controller;

import Model.Student;
import View.tm.StudentTM;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;

public class StudentFormController {
    public AnchorPane studentFormContext;
    public JFXTextField txtId;
    public JFXTextField txtAddress;
    public JFXTextField txtName;
    public JFXTextField txtContact;
    public TableView<StudentTM> tblStudent;
    public TableColumn colId;
    public TableColumn colName;
    public TableColumn colContact;
    public TableColumn colAddress;
    public TableColumn colOperate;

    static ArrayList<Student> studentList = new ArrayList();
    public JFXTextField txtSearch;
    public JFXButton btnSaveAndUpdate;

    public void initialize(){
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("contact"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colOperate.setCellValueFactory(new PropertyValueFactory<>("btn"));

        txtSearch.textProperty().addListener((observable, oldValue, txt) -> {
            for (Student s:studentList
                 ) {
                if (s.getId().contains(txt)
                        ||
                        s.getName().contains(txt)
                        ||
                        s.getAddress().contains(txt)
                        ||
                        s.getContact().contains(txt)
                ){
                    System.out.println(s);
                }
            }
        });
    }

    public void backToHomeOnAction(ActionEvent actionEvent) throws IOException {
        URL resource = getClass().getResource("../View/DashBoardForm.fxml");
        Parent load = FXMLLoader.load(resource);
        Stage window = (Stage) studentFormContext.getScene().getWindow();
        window.setScene(new Scene(load));
    }
    public boolean isExists(Student student){
        for (Student t:studentList
        ) {
            if(t.getId().equalsIgnoreCase(student.getId())){
                return false;
            }
        }
        return true;
    }

    public void saveUpdateOnAction(ActionEvent actionEvent) {
        String x = btnSaveAndUpdate.getText();

        if (btnSaveAndUpdate.getText().equalsIgnoreCase("Save")){
            Student student = new Student(txtId.getText(), txtName.getText(), txtContact.getText(), txtAddress.getText());
            txtId.clear();
            txtName.clear();
            txtAddress.clear();
            txtContact.clear();

            if (isExists(student)) {
                if (studentList.add(student)) {
                    loadAllStudents();
                    new Alert(Alert.AlertType.CONFIRMATION, "Saved..", ButtonType.CLOSE).show();
                } else {
                    new Alert(Alert.AlertType.WARNING, "Try Again..", ButtonType.CLOSE).show();
                }
            }else{
                new Alert(Alert.AlertType.WARNING,"Already Exists", ButtonType.CLOSE).show();
            }
        }else{
            System.out.println("");
        }

        tblStudent.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            try {
                loadStudentData(newValue);
            } catch (IOException e) {
                e.printStackTrace();
            }

        });

    }

    private void loadStudentData(StudentTM tm) throws IOException {
        txtId.setText(tm.getId());
        txtName.setText(tm.getName());
        txtContact.setText(tm.getContact());
        txtAddress.setText(tm.getAddress());

        FXMLLoader loader =new FXMLLoader(getClass().getResource("../View/StudentForm.fxml"));
        Parent parent = loader.load();
        StudentDataFormController controller = loader.getController();
        controller.setData(tm);
        Scene scene = new Scene(parent);
        new Stage().setScene(scene);
        Stage stage =new Stage();
        stage.setScene(scene);
        stage.show();
    }

    private void loadAllStudents(){
        ObservableList<StudentTM> tmObservableList = FXCollections.observableArrayList();
        for (Student temp: studentList
             ) {
            Button btn = new Button("Delete..");
            StudentTM studentTM = new StudentTM(temp.getId(), temp.getName(), temp.getContact(), temp.getAddress(), btn);
            tmObservableList.add(
                    studentTM
            );

            btn.setOnAction((e)->{

                ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
                ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Are you suer you want to delete?",yes,no);
                alert.setTitle("Confirmation alert");
                Optional<ButtonType> result = alert.showAndWait();

                if (result.orElse(no)==yes){
                    studentList.remove(temp);
                    loadAllStudents();
                }else{

                }
            });
        }
        tblStudent.setItems(tmObservableList);
    }

    public void newStudentOnAction(ActionEvent actionEvent) {

    }

    public void moveToName(ActionEvent actionEvent) {
        txtName.requestFocus();
    }

    public void moveToContact(ActionEvent actionEvent) {
        txtContact.requestFocus();
    }

    public void moveToAddress(ActionEvent actionEvent) {
        txtAddress.requestFocus();
    }

}
