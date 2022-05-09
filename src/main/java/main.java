
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

    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {

        Scanner scanner = new Scanner(new File("FileXML.txt"));
        String allString = "";
        
        boolean valid = true;
        
        while (scanner.hasNextLine()) {
            allString += scanner.nextLine();
        }
        
        allString = RegexTools.RemoveXmlCommentsAndVersion(allString);
        
        Stack<TagClass> pilha = new Stack<>();
        TagItem tagItem;
        String jsonString = "";

        do {
            tagItem = RegexTools.GetXmlMatch(allString);

            if (tagItem == null) {
                break;
            }

            if (pilha.isEmpty()) {
                if ("opening".equals(tagItem.getType())) {
                    pilha.add(new TagClass(tagItem.getName()));
                    allString = allString.replaceFirst("<" + tagItem.getName() + ">", "");
                } else {
                    valid = false;
                }
            } else if ("opening".equals(tagItem.getType())) {
                TagClass newTag = new TagClass(tagItem.getName());

                pilha.lastElement().getTagClass().add(newTag);
                pilha.add(newTag);

                allString = allString.replaceFirst("<" + tagItem.getName() + ">", "");
            } else if ("content".equals(tagItem.getType())) {
                pilha.lastElement().setTagStringContent(tagItem.getName());
                allString = allString.replaceFirst(tagItem.getName(), "");
            } else if ("closing".equals(tagItem.getType())) {
                if (pilha.lastElement().getTagName().equals(tagItem.getName())) {
                    if (pilha.size() == 1) {
                        allString = allString.replaceFirst("</" + tagItem.getName() + ">", "");
                        if ("".equals(allString)) {
                            break;
                        } else {
                            valid = false;
                        }
                        //break;
                    }
                    pilha.pop();
                    allString = allString.replaceFirst("</" + tagItem.getName() + ">", "");
                } else {
                    valid = false;
                }
            }

        } while (valid);

        TagClass objectResult = pilha.pop();

        jsonString += "{\"" + objectResult.getTagName() + "\":{";

        if (!objectResult.getTagClass().isEmpty()) {
            ArrayList<TagClass> list = objectResult.getTagClass();
            jsonString += "\"" + list.get(0).getTagName() + "\":[";

            for (TagClass listItem : list) {
                jsonString += "{";

                ArrayList<TagClass> properties = listItem.getTagClass();

                for (TagClass property : properties) {
                    jsonString += "\"" + property.getTagName() + "\":\"" + property.getTagStringContent() + "\",";
                }

                jsonString = jsonString.substring(0, jsonString.length() - 1);
                jsonString += "},";
            }
            jsonString = jsonString.substring(0, jsonString.length() - 1);
            jsonString += "]}}";

            FileWriter fw = new FileWriter("FileJson.txt");
            fw.write(jsonString);
            fw.close();
        }
    }

}
