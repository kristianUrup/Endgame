/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package endgame.GUI.Controller;

import endgame.BE.Department;
import endgame.BE.Order;
import endgame.BE.Worker;
import endgame.BLL.Exception.BllException;
import endgame.GUI.Model.OrderModel;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author Kristian Urup laptop
 */
public class ExpandedPostItNoteController implements Initializable
{

    private OrderModel OMO;

    private Order ordersForDepartment;

    private Department department;
    
    private boolean isDone = false;

    @FXML
    private AnchorPane topAnchorPane;
    @FXML
    private Label topLabel;
    @FXML
    private Button done;
    @FXML
    private TableView<Department> tableDepartmentList;
    @FXML
    private TableColumn<Department, String> cellDepartment;
    @FXML
    private TableColumn<Department, String> cellStatus;
    @FXML
    private TableView<Worker> tableWorkersID;
    @FXML
    private TableColumn<Worker, Integer> cellWorkersID;
    @FXML
    private ProgressBar estimatedProgress;
    @FXML
    private Label lblOrderNumber;
    @FXML
    private Label lblCustomer;
    @FXML
    private Label lblDeliveryDate;
    @FXML
    private Label lblLastActive;
    @FXML
    private Label lblEndDate;
    @FXML
    private Label lblStartDate;
    
    @FXML
    private BorderPane borderPane;
    @FXML
    private ImageView crossBtn;
    @FXML
    private Label lblAnchorStatus;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        try
        {
            OMO = new OrderModel();
            cellWorkersID.setCellValueFactory(new PropertyValueFactory<>("salaryNumber"));
            cellDepartment.setCellValueFactory(new PropertyValueFactory<>("name"));
            cellStatus.setCellValueFactory(cellData -> cellData.getValue().getConditionProperty());
        } catch (BllException ex)
        {
            OMO.setLastActivity(ordersForDepartment, department, ex.getMessage());
        }

    }

    /**
     * Sets the information on the orders depending on the department
     * @param order 
     */
    public void setOrderInfo(Order order)
    {

        try
        {
            ordersForDepartment = order;
            lblOrderNumber.setText(order.getOrderNumber());
            lblCustomer.setText(order.getCustomer());

            Date date = order.getDeliveryDate();

            DateFormat outputFormatter = new SimpleDateFormat("dd/MM/yyyy");
            String output = outputFormatter.format(date);

            Date startDate = order.getStartDate();
            String startStringDate = new SimpleDateFormat("[ww:u]").format(startDate);
            lblStartDate.setText(startStringDate);

            Date endDate = order.getEndDate();
            String endStringDate = new SimpleDateFormat("[ww:u]").format(endDate);
            lblEndDate.setText(endStringDate);

            lblDeliveryDate.setText(output);

            setProgressBar();
            setAnchorStatusColor();

            tableDepartmentList.setItems(OMO.getAllDepartments(ordersForDepartment));
            setStatusColor();

            getLastActive();
            updateOrder(ordersForDepartment);
            updateDepartmentList();
            
            if(OMO.getConfig().toLowerCase().equals("management")) {
                done.setVisible(false);
            }
            
            tableWorkersID.setItems(OMO.getAllWorkers(department, ordersForDepartment));

        } catch (BllException ex)
        {
            OMO.setLastActivity(ordersForDepartment, department, ex.getMessage());
        }

    }

    /**
     * Sets the department
     * @param department 
     */
    public void setDepartment(Department department)
    {
        this.department = department;
    }
    
    /**
     * Gets the state of the order and returns isDone
     * @return 
     */
    public boolean getState() {
        return isDone;
    }

    /**
     * Sets the order as done
     * @throws BllException 
     */
    public void setDone() throws BllException
    {
        OMO.changeOrderState(ordersForDepartment, department);
        OMO.setLastActivity(ordersForDepartment, department, "Task was marked as done");
    }

    /**
     * Sets the progressbar in percentage
     */
    private void setProgressBar()
    {
        try
        {
            Date startDate = ordersForDepartment.getStartDate();
            Date endDate = ordersForDepartment.getEndDate();
            estimatedProgress.setProgress(OMO.getProgressedTimeInProcent(startDate, endDate));
        } catch (BllException ex)
        {
            OMO.setLastActivity(ordersForDepartment, department, ex.getMessage());
        }
    }

    /**
     * Updates the order information by filling out the information on the order
     * @param order 
     */
    public void updateOrder(Order order)
    {
        TimerTask repeatedTask = new TimerTask()
        {
            @Override
            public void run()
            {
                try
                {
                    Platform.runLater(() -> lblOrderNumber.setText(ordersForDepartment.getOrderNumber()));
                    Platform.runLater(() -> lblCustomer.setText(ordersForDepartment.getCustomer()));
                    Date date = ordersForDepartment.getDeliveryDate();

                    DateFormat outputFormatter = new SimpleDateFormat("dd/MM/yyyy");
                    String output = outputFormatter.format(date);

                    Platform.runLater(() -> lblDeliveryDate.setText(output));

                    Platform.runLater(() -> setProgressBar());

                    String lastActive = OMO.getLastActivity(ordersForDepartment);

                    Platform.runLater(() -> lblLastActive.setText(lastActive));
                } catch (BllException ex)
                {
                    OMO.setLastActivity(ordersForDepartment, department, ex.getMessage());
                }
            }
        };
        Timer timer = new Timer();

        long delay = 5000L;
        long period = 5000L;
        timer.scheduleAtFixedRate(repeatedTask, delay, period);
    }

    /**
     * Runs the refreshDepartments method, 
     * which runs through every department
     * and inserts them into a list
     */
    public void updateDepartmentList()
    {
        TimerTask repeatedTask = new TimerTask()
        {
            @Override
            public void run()
            {
                try
                {
                    OMO.refreshDepartments(ordersForDepartment);
                } catch (BllException ex)
                {
                    Logger.getLogger(PostItController.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        };
        Timer timer = new Timer();

        long delay = 5000L;
        long period = 5000L;

        timer.scheduleAtFixedRate(repeatedTask, delay, period);
    }

    /**
     * Sets the status color of the cellstatus depending on the item status
     * @throws BllException 
     */
    public void setStatusColor() throws BllException
    {
        cellStatus.setCellFactory(column ->
        {
            return new TableCell<Department, String>()
            {
                @Override
                protected void updateItem(String item, boolean empty)
                {
                    super.updateItem(item, empty);

                    if (item == null || empty)
                    {
                        setText(null);
                        setStyle("");
                    } else
                    {
                        if (item.equals("finished")) //færdig
                        {
                            setStyle("-fx-background-color: green");
                            setText("Done");
                        } else if (item.equals("behind"))
                        {
                            setStyle("-fx-background-color: red");
                            setText("Behind");
                        } else if (item.equals("not started"))
                        {
                            setStyle("-fx-background-color: yellow");
                            setText("Not started");
                        } else if (item.equals("ongoing"))
                        {
                            setStyle("-fx-background-color: #0080FF");
                            setText("Ongoing");
                        }

                    }
                }
            };
        });
    }

    /**
     * Gets the last active department on the order
     */
    public void getLastActive()
    {
        try
        {
            lblLastActive.setText(OMO.getLastActivity(ordersForDepartment));
        } catch (BllException ex)
        {
            OMO.setLastActivity(ordersForDepartment, department, ex.getMessage());
        }
    }

    /**
     * Gets the borderPane
     * @return 
     */
    public BorderPane getBorderPane()
    {
        return borderPane;
    }

    /**
     * Gets the clicked department and displays the department progression
     * @param event 
     */
    @FXML
    private void handlerDepartmentClicked(MouseEvent event)
    {
        Department depClicked = tableDepartmentList.getSelectionModel().getSelectedItem();
        if (depClicked != null)
        {
            try
            {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/endgame/GUI/View/DepartmentProgression.fxml"));
                Parent root = (Parent) loader.load();
                DepartmentProgressionController dpcontroller = loader.getController();
                dpcontroller.setDepartment(depClicked);

                Stage stage = new Stage();
                stage.setScene(new Scene(root));

                Stage stage2 = (Stage) borderPane.getScene().getWindow();
                stage.initOwner(stage2);
                stage.initModality(Modality.WINDOW_MODAL);
                stage.initStyle(StageStyle.UNDECORATED);

                stage.show();
            } catch (IOException ex)
            {
                Logger.getLogger(ExpandedPostItNoteController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Sets the status color on the postit note depending on the cond string
     */
    public void setAnchorStatusColor()
    {
        String cond = ordersForDepartment.getCondition();
        lblAnchorStatus.setStyle("-fx-text-fill: black");

        if (cond.equals("finished")) //færdig
        {
            topAnchorPane.setStyle("-fx-background-color: green");
            lblAnchorStatus.setText("Finished");
        } else if (cond.equals("behind"))
        {
            topAnchorPane.setStyle("-fx-background-color: red");
            lblAnchorStatus.setText("Behind");

        } else if (cond.equals("not started"))
        {
            topAnchorPane.setStyle("-fx-background-color: yellow");
            lblAnchorStatus.setText("Not Started");

        } else if (cond.equals("ongoing"))
        {
            topAnchorPane.setStyle("-fx-background-color: #0080FF");
            lblAnchorStatus.setText("Ongoing");
        }

    }

    /**
     * Handles the done button, displays an alertbox 
     * and if the alerboxes are clicked upon, then the isdone is set as true
     * @param event 
     */
    @FXML
    private void handleDoneBtn(ActionEvent event)
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Dialog");
        alert.setHeaderText("You are about to set this task to done");
        alert.setContentText("Are you sure you want to do this?");

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/endgame/Data/Dialogs.css").toExternalForm());
        dialogPane.getStyleClass().add("dialogPane");
        Optional<ButtonType> result = alert.showAndWait();
        if ((result.isPresent()) && (result.get() == ButtonType.OK))
        {
            try
            {
                setDone();
                isDone = true;
                Stage stage = (Stage) borderPane.getScene().getWindow();
                stage.close();
            } catch (BllException ex)
            {
                Logger.getLogger(ExpandedPostItNoteController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Closes the postit note
     * @param event 
     */
    @FXML
    private void handlerClosePostIt(MouseEvent event)
    {
        Stage stage = (Stage) borderPane.getScene().getWindow();
        stage.close();
    }
}
