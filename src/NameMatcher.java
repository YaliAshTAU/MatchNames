import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NameMatcher implements Runnable {
    private final Set<String> items;
    private final String input;
    private final Map<String, List<Location>> result;
    private final int lineOffset;

    public NameMatcher(String input, Set<String> items, int startLine){
        this.items = items;
        this.input = input;
        this.lineOffset = startLine;
        this.result = new HashMap<>();
    }

    @Override
    public void run(){
        Pattern pattern = Pattern.compile("(" + String.join("|", items) + ")"); // pattern - find one of the items
        Matcher matcher = pattern.matcher(input);
        while (matcher.find()){ // as long as finds a name in chunk
            String item = matcher.group();
            if (!result.containsKey(item))
                result.put(item, new ArrayList<>());
            result.get(item).add(new Location(lineOffset, matcher.start())); //add to map
        }
    }

    public Map<String, List<Location>> getResult(){
        return this.result;
    }
}
