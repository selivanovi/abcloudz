import javax.swing.text.Element;
import java.lang.reflect.Method;
import java.security.cert.Extension;
import java.util.Arrays;
import java.util.Random;

public class Test {
    public static void main(String[] args) {
        Integer[] array = new Integer[3];
        ArrayList<Integer> arrayList = new ArrayList<>(2);
        Integer element = 5;

        for (int i = 1; i <= 5; i++) {
            addElement(arrayList, i);
        }

        //Add elements
        addAll(arrayList, array);
        //Add element to position
        addElement(arrayList, 4, 0);
        //Add element to start
        addElement(arrayList, 0, 0);
        //Add element to end
        addElement(arrayList, arrayList.getSize() - 1, 666);
        //Add elements to position
        addAll(arrayList, 3, array);
        //Remove element from position
        remove(arrayList, 3);
        //Remove element
        remove(arrayList, element);
        //Trim to size
        trimToSize(arrayList);
        //Clear
        clear(arrayList);
    }


    public static void print(ArrayList arrayList) {
        System.out.println(arrayList.toString() + " size: " + arrayList.getSize() + "\tlength: " + arrayList.getLength());
    }

    private static <E> void addElement(ArrayList arrayList, E element) {
        System.out.println("===== Add element =====");
        arrayList.add(element);
        print(arrayList);
        System.out.println();
    }

    private static <E> void addElement(ArrayList arrayList, int position, E element) {
        System.out.println("===== Add element to position =====");
        arrayList.addAtPosition(position, element);
        print(arrayList);
        System.out.println();
    }

    private static <E> void addAll(ArrayList<E> arrayList, E[] array) {
        System.out.println("===== Add elements =====");
        arrayList.addAll(array);
        print(arrayList);
        System.out.println();
    }

    private static <E> void addAll(ArrayList arrayList, int position, E[] element) {
        System.out.println("===== Add elements to position =====");
        arrayList.addAll(position, element);
        print(arrayList);
        System.out.println();
    }

    private static void remove(ArrayList arrayList, int index) {
        System.out.println("===== Remove element from position =====");
        System.out.println("Position: " + index);
        arrayList.remove(index);
        print(arrayList);
        System.out.println();
    }

    private static <E> void remove(ArrayList arrayList, E element) {
        System.out.println("===== Remove element =====");
        System.out.println("Element: " + element);
        arrayList.remove(element);
        print(arrayList);
        System.out.println();
    }

    private static void trimToSize(ArrayList arrayList) {
        System.out.println("===== Trim to size =====");
        arrayList.trimToSize();
        print(arrayList);
        System.out.println();
    }

    private static void clear(ArrayList arrayList) {
        System.out.println("==== Clear ====");
        arrayList.clear();
        print(arrayList);
        System.out.println();
    }


}
