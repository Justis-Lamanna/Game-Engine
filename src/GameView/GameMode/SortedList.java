/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameView.GameMode;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * An implementation of a sorted list.
 * <p>Surprisingly, Java doesn't provide this sort of implementation. For
 * sprite usage, I needed a list that would be sorted through a comparator, so
 * all sprites of priority 0 would be grouped, followed by priority 1, and so
 * on. This is a simple extension of an ArrayList; Everything else is the same,
 * except add() must place it in the proper location, rather than at the end.
 * @author Justis
 * @param <E> The type contained in this SortedList.
 */
public class SortedList<E extends Comparable> extends ArrayList<E>
{
    /**
     * Add a value to this SortedList.
     * The value is added wherever [Previous Value] &lt; Value &lt;= [Next Value].
     * By that definition, for values that are equivalent, later additions will
     * come before earlier ones.
     * @param e The value to add to this SortedList.
     * @return True, per the specs.
     */
    @Override
    public boolean add(E e)
    {
        int index = 0;
        Iterator<E> iter = iterator();
        while(iter.hasNext())
        {
            if(e.compareTo(iter.next()) < 0){break;}
            index++;
        }
        super.add(index, e);
        return true;
    }
    
    /**
     * Throws an exception.
     * Since the concept of adding a value at some index would throw off
     * the concept of a sorted list, this method is disabled.
     * @param index The index, which is ignored.
     * @param element The value to add, which is ignored.
     * @throws UnsupportedOperationException This method is used.
     */
    @Override
    public void add(int index, E element)
    {
        throw new UnsupportedOperationException("Sorted lists can't be inserted by index");
    }
}
