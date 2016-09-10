package com.jiaxy.cache.helpers;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicMarkableReference;

/**
 * the cache key data structure
 *
 * http://www.ibm.com/developerworks/cn/java/j-lo-lockfree/index.html?ca=drs
 *
 * @author: wutao
 * @version: $Id:NoBlockingLinkedList.java 2014/01/15 16:14 $
 *
 *
 */
public class NoBlockingLinkedList<T> implements Serializable{



    private AtomicMarkableReference<Entry<T>> head;

    private AtomicInteger size = new AtomicInteger(0);


    public NoBlockingLinkedList() {
        Entry<T> headEntry = new Entry<T>(null,null);
        head = new AtomicMarkableReference(headEntry,false);
    }

    public void addFirst(T value) {
        addAfter(head.getReference().value,value);
    }

    public boolean addAfter(T after, T value) {
        boolean successful = false;
        while ( !successful ){
            boolean found = false;
            for (Entry<T> entry = head.getReference();entry != null && !isRemoved(entry);entry = entry.next.getReference()){
                if ( isEqual(entry.value,after) && !entry.next.isMarked()){
                    found = true;
                    Entry<T> nextEntry = entry.next.getReference();
                    Entry<T> newEntry = new Entry<T>(value,nextEntry);
                    successful = entry.next.compareAndSet(nextEntry,newEntry,false,false);
                    break;
                }
            }
            if ( !found ){
                return false;
            }
        }
        size.incrementAndGet();
        return true;
    }

    public boolean remove(T value) {

        boolean successful = false;
        while ( !successful ){
            boolean found = false;
            for ( Entry<T> entry = head.getReference() ,nextEntry = entry.next.getReference();nextEntry != null ;
                  entry = nextEntry,nextEntry = nextEntry.next.getReference()){
                if ( !isRemoved(nextEntry) && isEqual(nextEntry.value,value)){
                    found = true;
                    logicallyRemove(nextEntry);
                    successful = physicallyRemove(entry,nextEntry);
                    break;
                }
            }
            if ( !found ){
                return false;
            }
        }

        return true;
    }


    public int size(){
        return size.get();
    }

    void logicallyRemove(Entry<T> entry) {
        while (!entry.next.attemptMark(entry.next.getReference(), true)) {

        }
    }

    boolean physicallyRemove(Entry<T> leftEntry, Entry<T> entry) {
        Entry<T> rightEntry = entry;
        do{
            rightEntry = rightEntry.next.getReference();
        }while ( rightEntry != null && isRemoved(rightEntry));
        size.decrementAndGet();
        return leftEntry.next.compareAndSet(entry,rightEntry,false,false);
    }

    boolean isRemoved(Entry<T> entry) {
        return entry.next.isMarked();
    }

    boolean isEqual(T arg0, T arg1) {
        if (arg0 == null) {
            return arg0 == arg1;
        } else {
            return arg0.equals(arg1);
        }
    }


   public void show(){
       for ( Entry<T> entry = head.getReference() ,nextEntry = entry.next.getReference();nextEntry != null ;nextEntry = nextEntry.next.getReference()){
            System.out.println(nextEntry.value);
       }
   }

    private class Entry<T>{

        private T value;

        AtomicMarkableReference<Entry<T>> next;


        public Entry() {
        }

        public Entry(T value, Entry<T> next) {
            this.value = value;
            this.next = new AtomicMarkableReference(next,false);
        }
    }

}
