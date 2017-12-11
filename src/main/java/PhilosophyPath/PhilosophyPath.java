package PhilosophyPath;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.validator.constraints.NotBlank;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.*;

@Document(collection="philosophypath")
@JsonIgnoreProperties(value = {"createdAt"}, allowGetters = true)

public class PhilosophyPath {

    @Id
    private String id;

    private String articlePath;

    private Boolean completed = false;

    private Date createdAt = new Date();

    public PhilosophyPath(String id, String articlePath) {
        this.id = id;
        this.articlePath = articlePath;
    }

    public String getId() {
        return id;
    }
    public String getArticlePath() {
        return articlePath;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public static List<String> getPathToPhilosophy(List<String> articleTitles) {
        return traverseLinks(articleTitles);
    }

    //recursively traverse through the first link on each article
    private static List<String> traverseLinks(List<String> articleTitles) {
        Set<String> set = new HashSet<>(articleTitles);

        if (articleTitles.contains("Philosophy")) {

            return articleTitles;
        } else if (set.size() < articleTitles.size()) {

            //If a duplicate gets added to the list (i.e. a unique set size < the list size), then we throw an exception and
            //end the program
            try {
                throw new CircularPathException();

            } catch (CircularPathException exp) {
                System.out.printf(exp.getMessage());
            }

            return articleTitles;
        } else {
            String nextLink = findLink(articleTitles.get(articleTitles.size() - 1));

            //If no viable links can be found, throw an exception
            if (nextLink.trim().length() == 0) {
                try {
                    throw new NoViableLinkException();

                } catch (NoViableLinkException exp) {
                    System.out.printf(exp.getMessage());
                }
            }

            articleTitles.add(nextLink);
            traverseLinks(articleTitles);

            return articleTitles;
        }
    }

    //find a link by grabbing the html of a page and parsing it
    private static String findLink(String title) {

        try {
            //grab a wikipedia article
            org.jsoup.nodes.Document doc = Jsoup.connect("http://en.wikipedia.org/wiki/" + title).get();
            String docToString = doc.toString();
            
            //remove words/links in parenthesis (i.e. "Europeans (disambiguation)")
            String textNoParens = docToString.replaceAll(" \\(.*?\\) ?", "");
            org.jsoup.nodes.Document docNoParens = Jsoup.parse(textNoParens);

            //only get links in paragraphs in .mw-content-ltr
            Elements links = docNoParens.select(".mw-content-ltr p a[href^=\"/wiki/\"]");

            List<String> validLinks = validLinks(links);
            System.out.println(validLinks.get(0));

            String firstValidLink = validLinks.get(0);

            return firstValidLink;
        } catch (Exception e) {

            System.out.println(e.getMessage());
            e.printStackTrace();
            return "";
        }
    }

    //Filter out common indicators that a link is not to another article
    private static List<String> validLinks(Elements links) {
        List<String> validLinks = new ArrayList<>();

        for (Element link : links) {
            String html = link.attr("href");

            if (       !html.contains("Help:")
                    && !html.contains("wikimedia")
                    && !html.contains("Wikipedia:")
                    && !html.contains("File:")
                    && !html.contains(".ogg")
                    && !html.contains("cite_note")
                    && !html.contains("/w/index.php")
                    && !html.contains("[")
                    && !html.contains("upload")
                    && !html.contains("#")
                    && !html.contains("<")
                    && html.length() > 0) {

                validLinks.add(html.replace("/wiki/", ""));
            }
        }

        return validLinks;
    }
}

