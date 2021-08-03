public class ArrayList<T> {

    private static final int DEFAULT_SIZE = 10;

    private int size = 0;

    private T[] array;

    public ArrayList() {
        array = (T[]) new Object[DEFAULT_SIZE];
    }

    public ArrayList(int capacity) {
        array = (T[]) new Object[capacity];
    }

    public int getSize() {
        return size;
    }

    public int getLength() {
        return array.length;
    }

    public void add(T element) {
        ensureCapacity(size + 1);
        array[size] = element;
        size++;
    }

    public void addAtPosition(int index, T element) throws ArrayIndexOutOfBoundsException {
        checkPosition(index);
        ensureCapacity(size + 1);
        moveElements(index, 1);
        array[index] = element;
        size++;
    }

    public boolean addAll(T[] items) {
        if (items == null || items.length == 0) {
            return false;
        }
        ensureCapacity(size + items.length);
        System.arraycopy(items, 0, array, size, items.length);
        size += items.length;
        return true;
    }

    public boolean addAll(int index, T[] items) {
        if (items == null || items.length == 0) {
            return false;
        }
        ensureCapacity(size + items.length);
        moveElements(index, items.length);
        System.arraycopy(items, 0, array, index, items.length);
        size += items.length;
        return true;
    }

    public T remove(int index) {
        checkPosition(index);
        T element = array[index];
        moveElements(index + 1, -1);
        size--;
        return element;
    }

    public T remove(T element) {
        int index = indexOf(element);
        if(index == -1) return null;

        return remove(index);
    }

    public void clear() {
        size = 0;
        for (T element : array) {
            element = null;
        }
    }

    public void trimToSize() {
        if (size == array.length) return;
        T[] newArray = createArray(size);
        System.arraycopy(array, 0, newArray, 0, size);
        array = newArray;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < size; i++) {
            if (array[i] != null) {
                str.append(array[i].toString());
            } else str.append(array[i]);
            if (i != size - 1) {
                str.append(", ");
            }
        }
        return "[ " + str + " ]";
    }

    private void ensureCapacity(int capacity) {
        while (capacity >= array.length) increaseCapacity();
    }

    private void increaseCapacity() {
        T[] newArray = createArray(array.length * 3 / 2 + 1);
        System.arraycopy(array, 0, newArray, 0, size);
        array = newArray;
    }

    private T[] createArray(int size) {
        T[] array = (T[]) new Object[size];
        return array;
    }

    private void moveElements(int index, int distance) throws ArrayIndexOutOfBoundsException {
        checkPosition(index);
        System.arraycopy(array, index, array, index + distance, size - index);
    }

    public int indexOf(T element){
        for (int i = 0; i < size; i++){
            if(element.equals(array[i])){
                return i;
            }
        }
        return -1;
    }

    public T get(int index) throws ArrayIndexOutOfBoundsException {
        checkPosition(index);
        return array[index];
    }

    private void checkPosition(int position) throws ArrayIndexOutOfBoundsException {
        if(position >= size && position < 0) throw new ArrayIndexOutOfBoundsException();
    }
}