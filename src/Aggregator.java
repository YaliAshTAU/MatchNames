import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Aggregator {
    private Map<String, List<Location>> results;

    public Aggregator(){
        results = new HashMap<>();
    }

    public void aggregate(Map<String, List<Location>> result){
        for (Map.Entry<String, List<Location>> entry : result.entrySet()){ // add all the entries from each matcher
            if (!results.containsKey(entry.getKey()))
                results.put(entry.getKey(), entry.getValue());
            else
                results.get(entry.getKey()).addAll(entry.getValue());
        }
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (Map.Entry<String, List<Location>> entry : results.entrySet()){
            str.append(entry.getKey());
            str.append(" --> [").append(entry.getValue().stream().map(Location::toString).collect(Collectors.joining(", "))).append("]\n");
        }
        str.deleteCharAt(str.length()-1);
        return str.toString();
    }

    public void print(){
        System.out.println(this);
    }
}
