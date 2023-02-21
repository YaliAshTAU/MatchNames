import java.io.BufferedReader;
import java.net.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    private static final int MAX_LINES = 1000;
    private static Aggregator read(Set<String> items, String address) throws IOException, InterruptedException{
        URL urlAddress = new URL(address);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlAddress.openStream())); //Reader from file
        ExecutorService executorService = Executors.newCachedThreadPool();
        ArrayList<NameMatcher> chunks = new ArrayList<>(); //chunks after matcher
        StringBuilder chunk = new StringBuilder(); //chunk sent to Matcher
        int lineIndex = 0; // for lineOffset
        String line;
        while ((line = bufferedReader.readLine()) != null){
            chunk.append(line);
            lineIndex++;
            if (lineIndex % MAX_LINES == 0) { //Execute a new chunk using matcher
                NameMatcher nameMatcher = new NameMatcher(chunk.toString(), items, lineIndex-MAX_LINES);
                chunks.add(nameMatcher);
                executorService.execute(nameMatcher); // run
                chunk = new StringBuilder();
            }
        }
        if (chunk.length() > 0) { //If there is a leftover
            NameMatcher nameMatcher = new NameMatcher(chunk.toString(), items, ((int)Math.floor(lineIndex/MAX_LINES)
                    *MAX_LINES));
            chunks.add(nameMatcher);
            executorService.execute(nameMatcher);
        }
        bufferedReader.close();

        Aggregator aggregator = new Aggregator();
        executorService.shutdown(); //Verify finishing all the threads
        if (!executorService.awaitTermination(60, TimeUnit.SECONDS)){
                executorService.shutdownNow();
        }
        for (NameMatcher matcher: chunks){ //aggregate all the matchers
            aggregator.aggregate(matcher.getResult());
        }
        return aggregator;
    }

    public static void main(String[] args) throws IOException, InterruptedException{
        Set<String> items = new HashSet<>(List.of("James", "John", "Robert", "Michael", "William", "David",
                "Richard", "Charles", "Joseph", "Thomas", "Christopher", "Daniel", "Paul", "Mark", "Donald", "George",
                "Kenneth", "Steven", "Edward", "Brian", "Ronald", "Anthony", "Kevin", "Jason", "Matthew", "Gary",
                "Timothy", "Jose", "Larry", "Jeffrey", "Frank", "Scott", "Eric", "Stephen", "Andrew","Raymond",
                "Gregory", "Joshua", "Jerry", "Dennis", "Walter", "Patrick", "Peter", "Harold","Douglas", "Henry",
                "Carl", "Arthur", "Ryan", "Roger"));
        String address = "http://norvig.com/big.txt";
        Aggregator aggregator = read(items, address);
        aggregator.print();
    }
}
