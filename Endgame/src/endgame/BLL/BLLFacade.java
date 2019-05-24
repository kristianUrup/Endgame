/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package endgame.BLL;

import endgame.BE.Department;
import endgame.BE.Order;
import endgame.BE.Worker;
import endgame.BLL.Exception.BllException;
import endgame.DAL.Exception.DalException;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javafx.collections.ObservableList;

/**
 *
 * @author Kristian Urup laptop
 */


public class BLLFacade implements IBLLFacade
{
    private OrderManager OMA;
    private DepartmentManager DMA;
    private FileManager FM;
    private LogManager LM;
    private JSONManager JM;
    private WorkerManager WM;
    
    public BLLFacade() throws BllException
    {
        try
        {
            WM = new WorkerManager();
            OMA = new OrderManager();
            DMA = new DepartmentManager();
            FM = new FileManager();
            LM = new LogManager();
            JM = new JSONManager();
        } catch (IOException ex)
        {
            throw new BllException("Could not get files");
        }
    }

    @Override
    public List<Order> getAllOrders(Department department, int offset) throws BllException
    {
        return OMA.getAllOrders(department, offset);
    }

    @Override
    public Department getDepartment(String dName) throws BllException
    {
        return DMA.getDepartment(dName);
    }

    @Override
    public List<Department> getDepartments(Order order) throws BllException
    {
        return DMA.getDepartments(order);
    }

    @Override
    public void changeOrderState(Order order, Department department) throws BllException
    {
        OMA.changeOrderState(order, department);
    }

    @Override
    public String getConfig()
    {
        return FM.getConfig();
    }
    
    @Override
    public void setLastActivity(Order order, Department department, String messageLog)
    {
        LM.setLastActivity(order, department, messageLog);
    }

    @Override
    public String getLastActivity(Order order) throws BllException
    {
        return LM.getLastActivity(order);
    }

    @Override
    public int getOffSet()
    {
        return FM.getOffSet();
    }

    @Override
    public Order getOrder(Department department, Order order) throws BllException
    {
        return OMA.getOrder(department, order);
    }

    @Override
    public void getJsonFile() throws BllException
    {
        JM.getJsonFile();
    }

//    public List<Worker> getAllWorkers(Worker worker) throws BllException
//    {
//        return WM.getAllWorkers(worker);
//    }

    
    @Override
    public List<Department> getManagementDepartments() throws BllException
    {
        return DMA.getManagementDepartments();
    }

    @Override
    public List<Worker> getAllWorkers(Department department, Order order) throws BllException
    {
        return WM.getWorkers(department, order);
    }
    
    
    
}
