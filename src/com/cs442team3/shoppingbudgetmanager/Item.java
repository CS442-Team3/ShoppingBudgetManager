/**
 * 
 */
package com.cs442team3.shoppingbudgetmanager;

/**
 * @author KaranJeet
 *
 */
public class Item {
	private int id;
	private String name,date,price;
	
	public Item()
	{}
	
	public Item(int id,String name,String date,String price)
	{
		this.id=id;
		this.name=name;
		this.date=date;
		this.price=price;
	}
	
	public void setId(int id)
	{
		this.id=id;
	}
	
	public int getId()
	{
		return id;
	}
	
	public void setName(String name)
	{
		this.name=name;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setDate(String date)
	{
		this.date=date;
	}
	
	public String getDate()
	{
		return date;
	}
	
	public void setPrice(String price)
	{
		this.price=price;
	}
	
	public String getPrice()
	{
		return price;
	}
}
