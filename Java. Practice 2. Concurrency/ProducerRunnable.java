public class ProducerRunnable implements Runnable {
    private final Store store;

    public ProducerRunnable(Store store) {
        this.store = store;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            try {
                store.addProducts(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
