package com.flatironschool.javacs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import org.jsoup.select.Elements;

public class WikiPhilosophy {
	
	final static WikiFetcher wf = new WikiFetcher();
	
	/**
	 * Tests a conjecture about Wikipedia and Philosophy.
	 * 
	 * https://en.wikipedia.org/wiki/Wikipedia:Getting_to_Philosophy
	 * 
	 * 1. Clicking on the first non-parenthesized, non-italicized link
     * 2. Ignoring external links, links to the current page, or red links
     * 3. Stopping when reaching "Philosophy", a page with no links or a page
     *    that does not exist, or when a loop occurs
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		
        // some example code to get you started

		// String url = "https://en.wikipedia.org/wiki/Java_(programming_language)";
		// Elements paragraphs = wf.fetchWikipedia(url);

		// Element firstPara = paragraphs.get(0);
		
		// Iterable<Node> iter = new WikiNodeIterable(firstPara);
		// for (Node node: iter) {
		// 	if (node instanceof TextNode) {
		// 		System.out.print(node);
		// 	}
  //       }

        // the following throws an exception so the test fails
        // until you update the code
        // String msg = "Complete this lab by adding your code and removing this statement.";
        // throw new UnsupportedOperationException(msg);

		List<String> urlList = new ArrayList<String>();
		String url = "https://en.wikipedia.org/wiki/Java_(programming_language)";

		boolean philosophy = false;
		while (!philosophy) {
			Elements paragraphs = wf.fetchWikipedia(url);
			urlList.add(url);
			Element firstPara = paragraphs.get(0);
			Iterable<Node> iter = new WikiNodeIterable(firstPara);
			String firstValidURL = getFirstValidURL(iter, url);
			if (firstValidURL != null) {
				if (url.equals("https://en.wikipedia.org/wiki/Philosophy")) {
					urlList.add(firstValidURL);
					philosophy = true;
				
				} else {
					url = firstValidURL;
				}
			} else {
				philosophy = true;
				System.out.println("An error has occurred!");
			}
			
			
		}

		for (String visited : urlList) {
			System.out.println(visited);
		}
	}

	public static String getFirstValidURL(Iterable<Node> iter, String currentUrl)  {
		for (Node node : iter) {
			if (node instanceof Element) {
				Element element = (Element) node;
				if (isValid(element, currentUrl)) {
					return "https://en.wikipedia.org" + node.attr("href");
				}
			}
		}
		return null;
	}

	public static boolean isValid(Element element, String currentURL) {
		String link = "https://en.wikipedia.org" + element.attr("href");
		if (link.equals(currentURL)) {
			return false;
		}
		if (isItalics(element) || isEmpty(element) || isHelpURL(element) || isCitation(element)) {
			return false;
		}
		return true;

	}

	public static boolean isItalics(Element element) {
		Element current = element;
		while (current != null) {
			if (current.tagName().equals("i") || current.tagName().equals("em")) {
				return true;
			}
			current = current.parent();
		}
		return false;
	}

	public static boolean isEmpty(Element element) {
		if (element.attr("href").equals("")) {
			return true;
		}
		return false;
	}

	public static boolean isHelpURL(Element element) {
		if (element.attr("href").startsWith("/wiki/Help:")) {
			return true;
		}
		return false;
	}

	public static boolean isCitation(Element element) {
		if (element.attr("href").startsWith("#cite_note")) {
			return true;
		}
		return false;
	}

}
