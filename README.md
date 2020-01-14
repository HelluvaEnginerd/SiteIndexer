# SiteIndexer

## What we want:

Single Domain webcrawler.
Output a simple summary/sitemap for each page in the domain including:
* links to pages under the same domain
* links to external URLs
* links to static content (images)

Provide README to BUILD, RUN, and TEST the code.

### BUILD:
    * Dependencies:
        * Apache Maven 3.6.3
        * JUnit 4.12
        * JSoup 1.8.3
        * maven-checkstyle-plugin 3.0.0
        * checkstyle 8.10
        * maven-jar-plugin 3.1.0
    * Build Instructions (from SiteIndexer/siteindexer)
        mvn install
        mvn package

### RUN:

    mvn exec:java -Dexec.mainClass="siteIndexer.src.main.java.com.hayden.demo.Crawler"

### TEST:

    TODO - see below


Dicuss plans for improvments when more time is given to the project.

## FUTURE IMPROVEMENTS:

* Unit tests
    * Abstract out all logic into methods for unit testing.
        * Example:

            fixUrl() - takes string and ensures http(s)://www. and .com are on either end.

    * Add data generators (random URL generator for above example) to stretch the business logic and edge cases
* Integration Tests
    * Create sample webpages to crawl and return the output. Compare with a 'gold' output to ensure no regression in functionality.
    * Run it on extremely large sites (reddit, tumblr, etc) and up number of pages to visit to ensure it can run at scale.

* Build
    * Add automated build pipeline (Github seems to like TravisCI) to scan, test, build, and deploy the image to a test environment for human-in-the-loop regression testing.

* Run and Output
    * Make number of pages to index configurable
    * Make internal and external visitation configurable
    * Allow searching for keyword(s) on indexed pages
    * Create webpage with input field for page to crawl.
        * Output pages crawled in 'card' format, where each card is a page that was indexed - with dropdowns for internal links, external links, and static content.
            * display static content (images) on said card.

* Deployment
    * Package in docker webserver (Apache Tomcat, NGINX) to deploy webpage


## Include Reasoning and trade-offs for design:

* Java vs other languages:

    This decision was simply out of convenience. I know maven, I had experience with the JSoup library from webservice work, and Java is simple to design, write, and read.

    * Runner Ups:
        * python
        * Go
        * Ruby
        * Perl

* JSoup vs. hand-written:

    JSoup is near-industry standard and provides many convenience functions.
