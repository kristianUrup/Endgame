/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package endgame.BLL;

import endgame.BE.Department;
import endgame.DAL.DepartmentDAO;
import endgame.BE.Order;
import endgame.BLL.Exception.BllException;
import endgame.DAL.Exception.DalException;
import endgame.DAL.IDepartmentDAO;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author bonde
 */
public class DepartmentManager
{

    IDepartmentDAO iddao;

    public DepartmentManager()
    {
        iddao = new DepartmentDAO();
    }

    /**
     * Gets a specific department depending on the name
     * @param dName
     * @return
     * @throws BllException 
     */
    public Department getDepartment(String dName) throws BllException
    {
        try
        {
            return iddao.getDepartment(dName);
        } catch (DalException ex)
        {
            throw new BllException(ex.getMessage());
        }
    }
    
    /**
     * Gets a list containing all departments
     * @param order
     * @return
     * @throws BllException 
     */
    public List<Department> getDepartments(Order order) throws BllException
    {
        try
        {
            List<Department> departments = iddao.getDepartments(order);
            Comparator<Department> dependency;
            dependency = (Department t, Department t1) -> t.getStartDate().compareTo(t1.getStartDate());
            Collections.sort(departments, dependency);
            return departments;
        } catch (DalException ex)
        {
            throw new BllException(ex.getMessage());
        }
    }
    
    /**
     * Gets a List containing alle the department names except for management
     * @return
     * @throws BllException 
     */
    public List<Department> getManagementDepartments() throws BllException
    {
        try
        {
            return iddao.getManagementDepartments();
        }
        catch (DalException ex)
        {
            throw new BllException(ex.getMessage());
        }
    }

}
