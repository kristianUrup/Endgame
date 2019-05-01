/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package endgame.GUI.Controller;

import endgame.BE.Department;
import endgame.BE.Order;
import endgame.BLL.Exception.BllException;
import endgame.GUI.Model.OrderModel;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author Kristian Urup laptop
 */
public class PostItController implements Initializable
{

    Department department;
    Order ordersForDepartment;
    PlatformController pfcontroller;
    OrderModel OMO;
    Timer timer;
    Date date;
    
    @FXML
    private Label lblOrderNumber;
    @FXML
    private Label lblCustomer;
    @FXML
    private Label lblDeliveryDate;
    @FXML
    private Label lblLastActive;
    @FXML
    private ProgressBar estimatedProgress;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Button done;
    @FXML
    private TableView<Department> tableDepartmentList;
    @FXML
    private TableColumn<Department, String> cellDepartment;
    @FXML
    private TableColumn<Department, Boolean> cellStatus;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        try
        {
            pfcontroller = new PlatformController();
            OMO = new OrderModel();
            //setProgressBar();
        } catch (BllException ex)
        {
            Logger.getLogger(PostItController.class.getName()).log(Level.SEVERE, null, ex);
        }
        cellDepartment.setCellValueFactory(new PropertyValueFactory<>("name"));
        cellStatus.setCellValueFactory(new PropertyValueFactory<>("isDone"));
//        tableDepartmentList.setItems(departments());
        
    }
    
    public void setOrderInfo(Order order)
    {
        ordersForDepartment = order;
        lblOrderNumber.setText(ordersForDepartment.getOrderNumber());
        lblCustomer.setText(ordersForDepartment.getCustomer());

        Date date = ordersForDepartment.getDeliveryDate();

        DateFormat outputFormatter = new SimpleDateFormat("dd/MM/yyyy");
        String output = outputFormatter.format(date);

        lblDeliveryDate.setText(output);
        
        setProgressBar();
        updateOrder(order);
    }

    public void setDepartment(Department department)
    {
        this.department = department;
    }

    public Button getButton()
    {
        return done;
    }

    public void setDone() throws BllException
    {
        OMO.changeOrderState(ordersForDepartment, department);
    }

    public void showDeliveryDate(Order order) throws BllException
    {

        List<Department> departments = new ArrayList();

        Department d1 = new Department(1, "Fisk", false);
        Department d2 = new Department(2, "Funky", false);
        Department d3 = new Department(3, "Frederik", false);

        departments.add(d1);
        departments.add(d2);
        departments.add(d3);

        for (int i = 0; i > departments.size(); i++)
        {

            lblDeliveryDate.setText(ordersForDepartment.getDeliveryDate().toString());

            Date date = ordersForDepartment.getDeliveryDate();

            DateFormat outputFormatter = new SimpleDateFormat("dd/MM/yyyy");
            String output = outputFormatter.format(date);

            lblDeliveryDate.setText(output);
        }
    }
    
    

    private void setProgressBar()
    {
        try
        {
            estimatedProgress.setProgress(OMO.getProgressedTimeInProcent(ordersForDepartment));
        } catch (BllException ex)
        {
            Logger.getLogger(PostItController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    public void updateOrder(Order order){
        
        ordersForDepartment = order;
        TimerTask repeatedTask = new TimerTask() {
            @Override
            public void run()
            {
                setOrderInfo(order);
            }
        };
        Timer timer = new Timer();
        
        long delay = 5000;
        long period = 5000;
        timer.scheduleAtFixedRate(repeatedTask, delay, period);
        System.out.println("Updated post it note");
    }
        
    public void setStatusColor()
    {
        Department departments = (Department) departments();
        String colorgreen = "-fx-background-color: green";
    }
    
    public ObservableList<Department> departments()
    {
        ObservableList<Department> departments = FXCollections.observableArrayList();;
        
        Department d1 = new Department(1, "Fisk", true);
        Department d2 = new Department(2, "Funky", false);
        Department d3 = new Department(3, "Frederik", false);

        departments.add(d1);
        departments.add(d2);
        departments.add(d3);
        
        return departments;
    }
}
