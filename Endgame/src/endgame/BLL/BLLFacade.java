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
import java.io.IOException;
import java.util.List;

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

    // DepartmentManager
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
    public List<Department> getManagementDepartments() throws BllException
    {
        return DMA.getManagementDepartments();
    }

    
    // OrderManager
    @Override
    public List<Order> getAllOrders(Department department, int offset) throws BllException
    {
        return OMA.getAllOrders(department, offset);
    }
    
    @Override
    public void changeOrderState(Order order, Department department) throws BllException
    {
        OMA.changeOrderState(order, department);
    }
    
    @Override
    public Order getOrder(Department department, Order order) throws BllException
    {
        return OMA.getOrder(department, order);
    }

    
    // FileManager
    @Override
    public String getConfig()
    {
        return FM.getConfig();
    }
    
    @Override
    public int getOffSet()
    {
        return FM.getOffSet();
    }
    
    // LogManager
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

    
    // JSONManager
    @Override
    public void getJsonFile() throws BllException
    {
        JM.getJsonFile();
    }
    
    
    // WorkerManager
    @Override
    public List<Worker> getAllWorkers(Department department, Order order) throws BllException
    {
        return WM.getWorkers(department, order);
    }
    
    
    
}
