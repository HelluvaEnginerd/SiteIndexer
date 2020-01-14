package siteIndexer.src.main.java.com.hayden.demo;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.HashSet;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import org.jsoup.nodes.Document;

public class Webpage {
    private String address;
    private String domain;
    private Document htmlDocument;

    private Set<String> links = new HashSet<String>();
    private Set<String> staticContent = new HashSet<String>();

    // matches 'https://website.com'
    Pattern domainPattern = Pattern.compile("^(?:https?:\\/\\/)?(?:[^@\\/\\n]+@)?(?:www\\.)?([^:\\/?\n]+)");

    public Webpage(Document htmlDocument){
        this.htmlDocument = htmlDocument;
        this.address = htmlDocument.location();
        setDomain();
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    private void setDomain()
    {
        if (this.address != null)
        {
            Matcher matcher = domainPattern.matcher(address);
            if (matcher.find()) {
                domain = matcher.group(1);
            }
        }
    }

    public String getDomain() {
        return this.domain;
    }

    public Set<String> getExternalLinks() {
        return this.links
        .stream()
        .filter(l -> !l.contains(domain) && !l.isEmpty())
        .collect(Collectors.toSet());
    }

    public Set<String> getInternalLinks() {
        return this.links
        .stream()
        .filter(l -> l.contains(domain) && !l.isEmpty())
        .collect(Collectors.toSet());
    }

    public void addLink(String link) {
        this.links.add(link);
    }

    public void setLinks(Set<String> links) {
        this.links = links;
    }

    public Set<String> getStaticContent() {
        return this.staticContent;
    }

    public void setStaticContent(Set<String> staticContent) {
        this.staticContent = staticContent;
    }

    public void addStaticContent(String staticContent) {
        this.staticContent.add(staticContent);
    }

    public Document getHtmlDocument() {
        return this.htmlDocument;
    }

    public void setHtmlDocument(Document htmlDocument) {
        this.htmlDocument = htmlDocument;
    }

    public void printPage() {
        System.out.println("Web address: " + this.address);
        System.out.println("");

        System.out.println("Internal Links");
        for (String il : getInternalLinks()) {
            System.out.println(il);
        }
        System.out.println("");

        System.out.println("External Links");
        for (String el : getExternalLinks()) {
            System.out.println(el);
        }
        System.out.println("");

        System.out.println("Static Content");
        for (String sc : this.staticContent) {
            System.out.println(sc);
        }
        System.out.println("\n");
    }
}