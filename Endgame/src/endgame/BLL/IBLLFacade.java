/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package endgame.BLL;

import endgame.BE.Department;
import endgame.BE.Order;
import endgame.BLL.Exception.BllException;
import java.util.List;

/**
 *
 * @author Kristian Urup laptop
 */
public interface IBLLFacade
{
    public List<Order> getAllOrders(Department department) throws BllException;
    
    public Department getDepartment(String dName) throws BllException;
    
    public List<Department> getDepartments(Order order) throws BllException;
    
    public void changeOrderState(Order order, Department department) throws BllException;
    
    public String getConfig();
    
    public void setLastActivity(Order order, Department department) throws BllException;
            
    public String getLastActivity(Order order, Department department) throws BllException;
}
