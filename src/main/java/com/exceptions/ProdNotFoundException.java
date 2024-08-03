package com.exceptions;

public class ProdNotFoundException extends RuntimeException
{
	public ProdNotFoundException(String msg)
	{
		super(msg);
	}
}