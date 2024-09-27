package com.T1store.T1store;

import com.T1store.T1store.constant.Category;
import com.T1store.T1store.constant.ItemSellStatus;
import com.T1store.T1store.entity.Item;
import com.T1store.T1store.entity.ItemImg;
import com.T1store.T1store.repository.ItemImgRepository;
import com.T1store.T1store.repository.ItemRepository;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class ShopCrawler {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ItemImgRepository itemImgRepository;

    public void crawlAndSaveItems() throws IOException {
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();

        try {
            String url = "https://shop-t1.gg/category/shop/112";
            driver.get(url);

            while (true) {
                try {
                    WebElement moreButton = driver.findElement(By.cssSelector(".btnMore"));
                    moreButton.click();
                    Thread.sleep(2000);
                } catch (Exception e) {
                    break;
                }
            }

            String pageSource = driver.getPageSource();
            Document document = Jsoup.parse(pageSource);

            Elements nameElements = document.select("strong.name");
            Elements priceElements = document.select(".product_price");
            Elements imageElements = document.select("img.hover-thumb");
            Elements productLinks =  document.select("div.thumbnail > a");//링크추출

            for (int i = 0; i < nameElements.size(); i++) {
                Element nameElement = nameElements.get(i);
                // "상품명" 텍스트를 포함하는 span 태그를 제거
                nameElement.select("span.title").remove();
                // 남은 텍스트만 가져오기
                String itemName = nameElement.text().trim();
                // 가격 텍스트 가져오기
                String priceText = priceElements.get(i).text().replaceAll("[^0-9]", "").trim();
                // 빈 문자열인 경우 기본 값 설정
                int price;
                if (priceText.isEmpty()) {
                    price = 0;  // 기본 값으로 0 설정
                } else {
                    price = Integer.parseInt(priceText);
                }
                String imageUrl = "https:" + imageElements.get(i).attr("data-src");
                String productLink = "https://shop-t1.gg" + productLinks.get(i).attr("href");
                System.out.println("Navigating to: " + productLink);

                //아이템이름으로 중복 확인
                List<Item> existingItems = itemRepository.findByItemNm(itemName);
                if (!existingItems.isEmpty()) {
                    // 중복된 아이템이 있으면 저장하지 않음
                    continue;
                }

                // Item 엔티티 생성 및 저장
                Item item = new Item();
                item.setItemNm(itemName);
                item.setPrice(price);
                item.setStockNumber(100); // 기본 재고 설정
                item.setItemSellStatus(ItemSellStatus.SELL); // 기본 판매 상태 설정
                item.setCategory(Category.SHOP); // 카테고리 설정

                itemRepository.save(item);

                // ItemImg 엔티티 생성 및 저장
                ItemImg itemImg = new ItemImg();
                itemImg.setItem(item);
                itemImg.setImgUrl(imageUrl);
                itemImg.setImgName(itemName + "_image");
                itemImg.setOriImgName(imageUrl.substring(imageUrl.lastIndexOf("/") + 1));
                itemImg.setRepImgYn("Y"); // 대표 이미지로 설정

                itemImgRepository.save(itemImg);

                // 상세 페이지로 이동하여 상세 이미지 크롤링
                driver.get(productLink);
                Thread.sleep(2000);

                Document detailDocument = Jsoup.parse(driver.getPageSource());
                Elements detailImages = detailDocument.select("#prdDetail > div:nth-child(3) > div > div.edb-img-tag-w img");
                System.out.println(detailImages);

                for (Element detailImage : detailImages) {
                    System.out.println(detailImage);
                    String detailImageUrl = "https://shop-t1.gg" + detailImage.attr("ec-data-src");
                    ItemImg detailItemImg = new ItemImg();
                    detailItemImg.setItem(item);
                    detailItemImg.setImgUrl(detailImageUrl);
                    detailItemImg.setImgName(itemName + "_detail_image");
                    detailItemImg.setOriImgName(detailImageUrl.substring(detailImageUrl.lastIndexOf("/") + 1));
                    detailItemImg.setRepImgYn("N"); // 일반 이미지로 설정

                    itemImgRepository.save(detailItemImg);
                }
                driver.navigate().back();
                Thread.sleep(2000);
            }

        }catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        finally {
            driver.quit();
        }
    }
}
