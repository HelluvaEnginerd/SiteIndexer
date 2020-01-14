package siteIndexer.src.main.java.com.hayden.demo;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.io.BufferedReader; 
import java.io.IOException; 
import java.io.InputStreamReader; 

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Crawler {
    // Max pages just to not run on forever. Can be set as high as you want, like Integer.MAX_VALUE
    private static final int MAX_PAGES = 10;
    private static Set<String> pagesVisited = new HashSet<String>();
    private static List<String> pagesToVisit = new LinkedList<String>();
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";
    private static List<Webpage> webpageList = new LinkedList<Webpage>();
    private Webpage webpage;

    private Crawler() {
    }

    public static void main(final String[] args) {
        // leave for debugging
        System.out.println("Hello Internet!");

        final Crawler crawler = new Crawler();

        while (true) {
            System.out.print("Enter a site to crawl, or 'q' to quit: \n>");
            // Enter data using BufferReader
            final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            // Reading data using readLine
            String siteToCrawl = "q";
            try{
                siteToCrawl = reader.readLine();
            } catch (IOException e) {
                System.out.println("why did you do this? " + e.getMessage());
            }

            if (siteToCrawl.equals("q") || siteToCrawl.isEmpty()) {
                System.out.println("Exiting");
                break;
            }
            if (!siteToCrawl.toLowerCase().startsWith("http://") || !siteToCrawl.toLowerCase().startsWith("https://")) {
                siteToCrawl = "http://" + siteToCrawl;
            }

            System.out.println("Crawling: " + siteToCrawl);
            index(siteToCrawl);

            System.out.println("Rough sitemap");
            System.out.println("Pages visited: " + pagesVisited.size());
            // print out the sitemap
            for (final Webpage w : webpageList) {
                w.printPage();
            }
        }
    }

    public static void index(final String url) {
        while (pagesVisited.size() < MAX_PAGES) {
            String currentUrl;
            if (pagesToVisit.isEmpty()) {
                currentUrl = url;
                pagesVisited.add(url);
            } else {
                do {
                    currentUrl = pagesToVisit.remove(0);
                } while (pagesVisited.contains(currentUrl));
                pagesVisited.add(currentUrl);
            }
            crawl(currentUrl);
        }
    }

    public static boolean crawl(final String url) {
        try {
            final Connection connection = Jsoup.connect(url).userAgent(USER_AGENT);
            final Webpage webpage = new Webpage(connection.get());

            // TODO: be able to read non-HTML
            if (!connection.response().contentType().contains("text/html")) {
                System.out.println("Fail: Retrieved something other than HTML");
                return false;
            }

            final Elements linksOnPage = webpage.getHtmlDocument().select("a[href]");
            final Elements imagesOnPage = webpage.getHtmlDocument().getElementsByTag("img");

            for (Element e : imagesOnPage) {
                webpage.addStaticContent(e.attr("src"));
            }

            // System.out.println("Found (" + linksOnPage.size() + ") links");
            for (final Element link : linksOnPage) {
                final String nextPage = link.absUrl("href");
                // only go to links within the domain
                // TODO: make this check much more robust - domain could be later in the URL
                final String domain = webpage.getDomain();
                if (nextPage.contains(domain) && !pagesVisited.contains(nextPage)) {
                    pagesToVisit.add(nextPage);
                }
                webpage.addLink(nextPage);
            }
            webpageList.add(webpage);
            return true;
        } catch (final IOException ioe)
        {
            // Unsuccessful HTTP request
            return false;
        }
    }

}
