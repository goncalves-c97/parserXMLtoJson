
import com.parserxmltojson.RegexTools;
import com.parserxmltojson.TagClass;
import com.parserxmltojson.TagItem;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

public class main {

    public static void main(String[] args) throws FileNotFoundException, IOException {

        Stack<TagClass> pilha = new Stack<>();
        Scanner scanner = new Scanner(new File("FileXML.txt"));
        String textToFile = "";
        ArrayList<TagClass> tagObjects = new ArrayList<>();

        while (scanner.hasNextLine()) {

            String line = scanner.nextLine();
            ArrayList<TagItem> matches = new ArrayList<>();

            do {
                TagItem tagItem = RegexTools.GetXmlMatch(line);

                if (tagItem != null) {
                    if (null == tagItem.getType()) {
                        line = line.replace(tagItem.getName(), "");
                    } else {
                        switch (tagItem.getType()) {
                            case "opening":
                                line = line.replace("<" + tagItem.getName() + ">", "");
                                matches.add(tagItem);
                                
                                if("".equals(line)){
                                    pilha.lastElement().getTagClass().add(new TagClass(tagItem.getName()));
                                }
                                break;
                            case "closing":
                                line = line.replace("</" + tagItem.getName() + ">", "");
                                matches.add(tagItem);
                                break;
                            default:
                                line = line.replace(tagItem.getName(), "");
                                break;
                        }
                    }
                } else if (!"".equals(line)) {

                    ArrayList<TagItem> lastTagPair = new ArrayList<>(matches.subList(matches.size() - 2, matches.size()));

                    if (lastTagPair.get(0).getName().equals(lastTagPair.get(1).getName())
                            && "opening".equals(lastTagPair.get(0).getType())
                            && "closing".equals(lastTagPair.get(1).getType())) {
                        //tagObjects.get(tagObjects.size() - 1).getTagClass().add(new TagClass(lastTagPair.get(0).getName(), line));
                        pilha.lastElement().getTagClass().add(new TagClass(lastTagPair.get(0).getName(), line));
                        break;
                    }
                } else if (tagObjects.isEmpty()) {
                    tagObjects.add(new TagClass(matches.get(0).getName()));
                    pilha.add(tagObjects.get(0));
                    break;
                } else if ("opening".equals(matches.get(matches.size() - 1).getType())) {
                    //tagObjects.get(tagObjects.size() - 1).getTagClass().add(new TagClass(matches.get(0).getName()));
                    pilha.lastElement().getTagClass().add(new TagClass(matches.get(matches.size() - 1).getName(), line));
                    break;
                } else if ("closing".equals(matches.get(matches.size() - 1).getType())) {
                    //tagObjects.get(tagObjects.size() - 1).getTagClass().add(new TagClass(matches.get(0).getName()));
                    pilha.pop();
                    break;
                }

            } while (true);                
        }

        FileWriter fw = new FileWriter("prog-check.txt");

        fw.write(textToFile);

        fw.close();
    }
}

//else if ("closing".equals(matches.get(matches.size() - 1).getType())
//                       && tagObjects.get(tagObjects.size() - 1).getTagName().equals(matches.get(matches.size() - 1).getName())) {
//                    
//                }
