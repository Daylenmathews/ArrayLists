/**
 * Array-based implementation of IndexedUnsortedList
 * Does not support ListIterator 
 * 
 * @author Daylen Mathews
 */
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class IUArrayList<T> implements IndexedUnsortedList<T> {
    private T[] array;
    private int rear;
    private static final int DEFAULT_CAPACITY = 10;

    /**
     * Constructor that uses default capacity 10
     */
    public IUArrayList(){
        this(DEFAULT_CAPACITY);
    }
    
    /**
     * Constructor that uses the specified initial capacity
     * @param initialCapacity initial array capacity 
     */
    @SuppressWarnings("unchecked")
    public IUArrayList(int initialCapacity){
        array = (T[])(new Object[initialCapacity]);
        rear = 0;
    }
    
    
    /**
     * Double array capacity of array is full
     */
    private void expandIfNecessary(){
    if (rear >= array.length){
        array = Arrays.copyOf(array, array.length*2);
    }
}
    @Override
    public void addToFront(T element){
        expandIfNecessary();
        for (int i = rear; i > 0; i--) {
            array[i] = array[i - 1];
        }
        array[0] = element;
        rear++;  
     }

    @Override
    public void addToRear(T element){
        expandIfNecessary();
        array[rear] = element;
        rear++;
    }

    @Override
    public void add(T element){
        addToRear(element);
    }

    @Override
    public void addAfter(T element, T target) {
        int targetIndex = indexOf(target);
        if (targetIndex < 0){
            throw new NoSuchElementException();
        }
        expandIfNecessary();

        for (int i = rear; i> targetIndex + 1; i--){
            array [i]= array[i-1];       
         }
         array[targetIndex+1] = element;
         rear++;
    }

    @Override
    public void add(int index, T element) {
        if (index < 0 || index > rear) {
            throw new IndexOutOfBoundsException();
        }
        expandIfNecessary();
        for (int i = rear; i > index; i--) {
            array[i] = array[i - 1];
        }
        array[index] = element;
        rear++;
    }


    @Override
    public T removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        T returnValue = array[0];
        for (int i = 0; i < array.length - 1; i++) {
            array[i] = array[i + 1];
        }
        rear--;
        return returnValue;
    }

    @Override
    public T removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        T returnValue = array[rear - 1];
        array[rear - 1] = null;
        rear--;
        return returnValue;
    }

    @Override
    public T remove(T element) {
        int elementIndex = indexOf(element);
        if (elementIndex < 0){
            throw new NoSuchElementException();
        }
        T retVal = array[elementIndex];
        for (int i = elementIndex; i < rear -1; i++){
            array[i] = array [i+1];
        }
        array[rear-1] = null;
        rear--;
        return retVal;
    }

    @Override
    public T remove(int index) {
        if (index < 0 || index >= rear) {
            throw new IndexOutOfBoundsException();
        }
        T returnValue = array[index];
        for (int i = index; i < rear; i++) {
            array[i] = array[i + 1];
        }
        rear--;
        array[rear] = null;
        return returnValue;
    }

    @Override
    public void set(int index, T element) {
        if (index < 0 || index >= rear) {
            throw new IndexOutOfBoundsException();
        }
        array[index] = element;
    }

    @Override
    public T get(int index) {
        if (index < 0 || index >= rear) {
            throw new IndexOutOfBoundsException();
        }
        return array[index];
    }

    @Override
    public int indexOf(T element) {
        int index = -1;

        for (int i = 0; i < rear && index > -1; i++) {
            if (array[i].equals(element)){
                index = i;
            }
        }
        return index;
    }

    @Override
    public T first() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return array[0];
    }

    @Override
    public T last() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return array[rear-1];

    }

    @Override
    public boolean contains(T target) {
        return indexOf(target) > -1;
    }

    @Override
    public boolean isEmpty() {
        return rear == 0;
    }

    @Override
    public int size() {
        return rear;
    }

    @Override
    public Iterator<T> iterator() {
        return new ALIterator();
    }

    @Override
    public ListIterator<T> listIterator() {
        throw new UnsupportedOperationException("Unimplemented method 'listIterator'");
    }

    @Override
    public ListIterator<T> listIterator(int startingIndex) {
        throw new UnsupportedOperationException("Unimplemented method 'listIterator'");
    }

    /**
     * Array List Iterator 
     */
    private class ALIterator implements Iterator<T> {
        private int nextIndex;
        private boolean canRemove;
        private int iterVersionNumber;
        
        public ALIterator() {
            nextIndex = 0;
            canRemove = false;
            int versionNumber = 0;
            iterVersionNumber = versionNumber;
        }
        @Override
        public boolean hasNext() {
            int versionNumber = 0;
            // If something changed, throw a concurrent modification exception
            if (iterVersionNumber != versionNumber) {
                throw new ConcurrentModificationException();
            }
            return nextIndex < rear;
    }
    @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            nextIndex++;
            canRemove = true;
            return array[nextIndex - 1];
        }   
        @Override
        public void remove() {
            int versionNumber = 0;
            if (iterVersionNumber != versionNumber) {
                throw new ConcurrentModificationException();
            }
            if (!canRemove) {
                throw new IllegalStateException();
            }
            canRemove = false;
            for (int i = nextIndex - 1; i < rear - 1; i++) {    
                array[i] = array[i + 1];
            }
            array[rear - 1] = null;
            rear--;
            nextIndex--;
            versionNumber++;
        }
    }
}