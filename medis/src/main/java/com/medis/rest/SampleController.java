package com.medis.rest;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import org.restexpress.Request;
import org.restexpress.Response;

public class SampleController
{
	public SampleController()
	{
		super();
	}

	public Object create(Request request, Response response)
	{
		//TODO: Your 'POST' logic here...
		return null;
	}

	public Object read(Request request, Response response)
	{
		//TODO: Your 'GET' logic here...
		return null;
	}

	public List<Object> readAll(Request request, Response response)
	{
		List<Object> ret = new ArrayList<>();
		try {
			Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
			while(interfaces.hasMoreElements()){
				NetworkInterface i = interfaces.nextElement();
				//Enumeration<InetAddress> addresses = i.getInetAddresses();
				ret.add(i.getDisplayName());
				System.out.println(i.getDisplayName());
				System.out.println("\t- name:" + i.getName());
				System.out.println("\t- idx:" + i.getIndex());
				System.out.println("\t- max trans unit (MTU):" + i.getMTU());
				System.out.println("\t- is loopback:" + i.isLoopback());
				System.out.println("\t- is PPP:" + i.isPointToPoint());
				System.out.println("\t- isUp:" + i.isUp());
				System.out.println("\t- isVirtual:" + i.isVirtual());
				System.out.println("\t- supportsMulticast:" + i.supportsMulticast());
			}
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}

	public void update(Request request, Response response)
	{
		//TODO: Your 'PUT' logic here...
		response.setResponseNoContent();
	}

	public void delete(Request request, Response response)
	{
		//TODO: Your 'DELETE' logic here...
		response.setResponseNoContent();
	}
}
