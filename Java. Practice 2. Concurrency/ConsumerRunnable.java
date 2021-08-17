public class ConsumerRunnable implements Runnable {

    private final Store store;

    public ConsumerRunnable(Store store) {
        this.store = store;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            try {
                store.buyProducts(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
