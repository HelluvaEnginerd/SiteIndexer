package siteIndexer.src.main.java.com.hayden.demo;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.io.IOException;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Crawler {
    // Max pages just to not run on forever. Can be set as high as you want, like Integer.MAX_VALUE
    private static final int MAX_PAGES = 10;
    private static Set<String> pagesVisited = new HashSet<String>();
    private List<String> pagesToVisit = new LinkedList<String>();
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";
    private static List<Webpage> webpageList = new LinkedList<Webpage>();
    private Webpage webpage;

    private Crawler() {
    }
    public static void main(String[] args) {
        // leave for debugging
        System.out.println("Hello Internet!");

        Crawler crawler = new Crawler();

        // crawl it bro
        crawler.index("https://www.canyons.edu/academics/computerscience/faculty/ferguson/cs122syllabus.php");

        System.out.println("Rough sitemap");
        System.out.println("Pages visited: " + pagesVisited.size());

        // print out the sitemap
        for (Webpage w : webpageList) {
            w.printPage();
        }
    }

    private String nextUrl()
    {
        String nextUrl;
        do
        {
            nextUrl = this.pagesToVisit.remove(0);
        } while(this.pagesVisited.contains(nextUrl));
        this.pagesVisited.add(nextUrl);
        return nextUrl;
    }

    public void index(String url)
    {
        while(this.pagesVisited.size() < MAX_PAGES)
        {
            String currentUrl;
            if(this.pagesToVisit.isEmpty())
            {
                currentUrl = url;
                this.pagesVisited.add(url);
            }
            else
            {
                currentUrl = this.nextUrl();
            }
            crawl(currentUrl);
        }
        System.out.println("\n**Done** Visited " + this.pagesVisited.size() + " web page(s)");
    }

    public boolean crawl(String url)
    {
        try
        {
            Connection connection = Jsoup.connect(url).userAgent(USER_AGENT);
            Webpage webpage = new Webpage(connection.get());
            if(connection.response().statusCode() == 200)
            {
                // TODO: add logger
                System.out.println("\n**Visiting** Received web page at " + url);
            }
            // TODO: be able to read non-HTML
            if(!connection.response().contentType().contains("text/html"))
            {
                System.out.println("**Failure** Retrieved something other than HTML");
                return false;
            }

            Elements linksOnPage = webpage.getHtmlDocument().select("a[href]");
            Elements imagesOnPage = webpage.getHtmlDocument().getElementsByTag("img");
            // System.out.println("Found (" + linksOnPage.size() + ") links");
            for(Element link : linksOnPage)
            {
                String nextPage = link.absUrl("href");
                // only go to links within the domain
                // TODO: make this check much more robust - domain could be later in the URL
                String domain = webpage.getDomain();
                if(nextPage.contains(domain) && !pagesVisited.contains(nextPage)) {
                    this.pagesToVisit.add(nextPage);
                }
                webpage.addLink(nextPage);
            }
            webpageList.add(webpage);
            return true;
        }
        catch(IOException ioe)
        {
            // Unsuccessful HTTP request
            return false;
        }
    }

}
