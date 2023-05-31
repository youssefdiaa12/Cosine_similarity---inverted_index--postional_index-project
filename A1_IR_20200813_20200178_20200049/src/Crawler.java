import org.jsoup.nodes.Document;
import java.io.IOException;
import java.util.ArrayList;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
public class Crawler {
    public static void main(String[] args) {
        String url = "https://koora.alkoora.live/p/kora-360-360-koora.html";
        crawl(1, url, new ArrayList<String>());
    }

    private static void crawl(int level, String url, ArrayList<String> visited) {
        if (level <= 1) {
            Document doc=request(url,visited);
            if(doc!=null) {
                for(Element link:doc.select("a[href]")) {
                    String next_link=link.absUrl("href");
                    if(!visited.contains(next_link)) {
                        crawl(level++,next_link,visited);
                    }
                }
            }
        }
    }

    private static Document request(String url, ArrayList<String> visited) {
        try {
            Connection con = Jsoup.connect(url);
            Document doc = con.get();
            if (con.response().statusCode() == 200) {
                System.out.println("Link: "+url);
                System.out.println(doc.title());
                visited.add(url);
                return doc;
            }
            return null;
        } catch (IOException e) {
            // TODO: handle exception
            return null;
        }
    }
}