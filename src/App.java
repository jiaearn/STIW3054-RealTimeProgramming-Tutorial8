import java.io.*;
import java.util.Scanner;
import java.util.concurrent.*;

class WordCount implements Runnable {
    CountDownLatch latch;
    String word;
    int count;

    public WordCount(CountDownLatch latch, String word) {
        this.latch = latch;
        this.word = word;
    }

    @Override
    public void run() {
        try {
            wordCount();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(word + " - " + count);
        latch.countDown();
    }

    private void wordCount() throws IOException {
        File file = new File("src/RossBeresford.txt");
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);

        String[] words;
        String str;

        while ((str = br.readLine()) != null) {
            words = str.split(" ");
            for (String wordSearches : words) {
                if (wordSearches.contains(word)) {
                    count++;
                }
            }
        }
        fr.close();
    }

}

class App {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String word1, word2;
        double start, end, executionTime;

        ExecutorService executor = Executors.newSingleThreadExecutor();
        System.out.print("Input 1st word: ");
        word1 = sc.next() + sc.nextLine();
        System.out.print("Input 2st word: ");
        word2 = sc.next() + sc.nextLine();
        System.out.println();

        start = System.currentTimeMillis();
        CountDownLatch latch = new CountDownLatch(2);

        WordCount wordCount1 = new WordCount(latch, word1);
        executor.submit(wordCount1);
        WordCount wordCount2 = new WordCount(latch, word2);
        executor.submit(wordCount2);

        executor.shutdown();

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int total = wordCount1.count + wordCount2.count;
        System.out.printf("%n%s%d", "Total: ", total);
        end = System.currentTimeMillis();
        executionTime = (end - start) / 1000;
        System.out.printf("%n%n%s%.3f%s%n", "Execution time: ", executionTime, " seconds");
    }
}