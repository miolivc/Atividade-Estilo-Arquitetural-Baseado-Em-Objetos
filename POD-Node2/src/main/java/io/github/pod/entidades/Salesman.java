/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package io.github.pod.entidades;

/**
 *
 * @author wensttay
 */
public class Salesman
{
    private String personid;
    private String phone;

    public Salesman(String personid, String phone)
    {
        this.personid = personid;
        this.phone = phone;
    }

    public Salesman()
    {
    }
    
    /**
     * @return the personid
     */
    public String getPersonid()
    {
        return personid;
    }

    /**
     * @param personid the personid to set
     */
    public void setPersonid(String personid)
    {
        this.personid = personid;
    }

    /**
     * @return the phone
     */
    public String getPhone()
    {
        return phone;
    }

    /**
     * @param phone the phone to set
     */
    public void setPhone(String phone)
    {
        this.phone = phone;
    }
    
    
}
