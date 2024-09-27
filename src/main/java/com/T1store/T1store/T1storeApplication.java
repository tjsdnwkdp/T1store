package com.T1store.T1store;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class T1storeApplication implements CommandLineRunner {

	@Autowired
	private TeamKitCrawler teamKitCrawler;
	@Autowired
	private CollectionCrawler collectionCrawler;
	@Autowired
	private ShopCrawler shopCrawler;
	@Autowired
	private CollaborationCrawler collaborationCrawler;

	public static void main(String[] args) {
		SpringApplication.run(T1storeApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
//		teamKitCrawler.crawlAndSaveItems(); // 크롤링 및 데이터 저장 수행
//		collaborationCrawler.crawlAndSaveItems();
//		collectionCrawler.crawlAndSaveItems();
//		shopCrawler.crawlAndSaveItems();
	}
}
