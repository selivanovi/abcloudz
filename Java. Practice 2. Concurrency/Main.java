public class Main {

    public static void main(String[] args) {
        Store store = new Store(5);

        Thread producer = new Thread(new ProducerRunnable(store), "Producer");
        Thread consumer =  new Thread(new ConsumerRunnable(store),"Consumer");

        producer.start();
        consumer.start();
    }
}
