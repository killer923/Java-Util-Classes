package com.killer923.java.util;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * operations such as contains() and isEmpty() may see stale values since they are not properly synchronized.
 * 
 * @author Abhishek.Bhatia
 *
 * @param <E>
 */
public class ConcurrentLinkedHashSet<E> extends LinkedHashSet<E> implements Set<E>
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3613833793983575105L;
	private boolean isUpdating = false;

	
	/**
	 * This method returns the size of the Set.
	 * 
	 * @return : The size of the set. It returns -1, if the thread has been interrupted.
	 */
	@Override
	public synchronized int size()
	{
		while (isUpdating)
		{
			try
			{
				wait();
			} catch (InterruptedException e)
			{
				Thread.currentThread().interrupt();
				e.printStackTrace();
				return -1;
			}
		}
		isUpdating=true;//setting true so that proper size at that time is returned, i.e. no one updates the set while calculating its size.
		int size= super.size();
		isUpdating=false;
		notifyAll();
		return size;
	}

	@Override
	public synchronized boolean addAll(Collection<? extends E> arg0)
	{
		while (isUpdating)
		{
			try
			{
				wait();
			} catch (InterruptedException e)
			{
				Thread.currentThread().interrupt();
				e.printStackTrace();
				return false;
			}
		}
		isUpdating = true;
		boolean result = false;
		result = super.addAll(arg0);
		isUpdating = false;
		notifyAll();
		return result;
	}

	@Override
	public synchronized boolean add(E arg0)
	{
		while (isUpdating)
		{
			try
			{
				wait();
			} catch (InterruptedException e)
			{
				Thread.currentThread().interrupt();
				e.printStackTrace();
				return false;
			}
		}
		isUpdating = true;
		boolean result = super.add(arg0);
		isUpdating = false;
		notifyAll();
		return result;
	}

	@Override
	public boolean remove(Object arg0)
	{
		while (isUpdating)
		{
			try
			{
				wait();
			} catch (InterruptedException e)
			{
				Thread.currentThread().interrupt();
				e.printStackTrace();
				return false;
			}
		}
		isUpdating = true;
		boolean result = super.remove(arg0);
		isUpdating = false;
		notifyAll();
		return result;
	}

	@Override
	public boolean removeAll(Collection<?> arg0)
	{
		while (isUpdating)
		{
			try
			{
				wait();
			} catch (InterruptedException e)
			{
				Thread.currentThread().interrupt();
				e.printStackTrace();
				return false;
			}
		}
		isUpdating = true;
		boolean result = super.removeAll(arg0);
		isUpdating = false;
		notifyAll();
		return result;
	}
}
