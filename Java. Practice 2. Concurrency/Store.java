public class Store {
    private int maxCapacity = 5;
    private int capacity = 0;

    public Store(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public synchronized void addProducts(int value) throws InterruptedException {
        if (value > maxCapacity && value <= 0)
            return;

        while (capacity >= maxCapacity) {

            System.out.printf("%s is waiting!\n", Thread.currentThread().getName());
            wait();
        }
        this.capacity += value;
        System.out.printf("%s: add %d product\n", Thread.currentThread().getName(), value);
        System.out.printf("====== Products: %s ======\n", capacity);
        notify();
    }

    public synchronized void buyProducts(int value) throws InterruptedException {
        if (value > capacity && value <= 0 )
            return;

        while (capacity < 1) {

            System.out.printf("%s is waiting!\n", Thread.currentThread().getName());
            wait();
        }
        this.capacity -= value;
        System.out.printf("%s: buy %d product\n", Thread.currentThread().getName(), value);
        System.out.printf("====== Products: %s ======\n", capacity);
        notify();
        Thread.sleep(1000);
    }
}
