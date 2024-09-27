package com.T1store.T1store.service;


import com.T1store.T1store.entity.ItemImg;
import com.T1store.T1store.repository.ItemImgRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemImgService {

    @Value("${itemImgLocation}")//application.properties에 itemImgLocation
    private String itemImgLocation;

    private final ItemImgRepository itemImgRepository;
    private final FileService fileService;

    public void saveItemImg(ItemImg itemImg, MultipartFile itemImgFile) throws Exception {
        String oriImgName = itemImgFile.getOriginalFilename(); //원본 이미지 경로
        String imgName = "";
        String imgUrl = "";
        System.out.println(oriImgName);
        //파일 업로드
        if(!StringUtils.isEmpty(oriImgName)) { //oriImgName 비어있지 않으면 실행
            System.out.println("*********************");
            imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());
            System.out.println(imgName);
            imgUrl = "/images/item/" + imgName;
        }
        System.out.println("<=====================>");
        //상품 이미지 정보 저장
        // oriImgName : 상품 이미지 파일의 원래 이름
        // imgName : 실제 로컬에 저장된 상품 이미지 파일의 이름
        // imgUrl : 로컬에 저장된 상품 이미지 파일을 불러오는 경로
        itemImg.updateItemImg(oriImgName, imgName, imgUrl);
        System.out.println("#######################");
        itemImgRepository.save(itemImg);
    }

    public void updateItemImg(Long itemImgId, MultipartFile itemImgFile) throws Exception {
        if(!itemImgFile.isEmpty()) { //상품의 이미지를 수정한 경우 상품 이미지 업데이트
            ItemImg saveditemImg = itemImgRepository.findById(itemImgId)
                    .orElseThrow(EntityNotFoundException::new); //기존 엔티티 조회
            //기존에 등록된 상품 이미지 파일이 있는 경우 파일 삭제
            if(!StringUtils.isEmpty(saveditemImg.getImgName())) {
                fileService.deleteFile(itemImgLocation+"/"+saveditemImg.getImgName());
            }
            String oriImgName = itemImgFile.getOriginalFilename();
            String imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes()); //파일 업로드
            String imgUrl = "/images/item/"+imgName;
            /* 변경된 상품 이미지 정보를 세팅
            상품 등록을 하는 경우에 ItemImgRepository.save()로직을 호출하지만
            여기선 호출을 하지 않았음.
            savedItemImg 엔티티는 현재 영속성 컨텍스트에서 관리되는 상태임.
            때문에 데이터를 변경하는 것만으로 변경 감지기능이 동작.
            트랜잭션 끝날 때 update 쿼리가 실행됨. spring: "영속성 컨텍스트에 바꼈네? DB도 바꿔야겠다!"
            #영속성 상태여야 사용가능하다#
            */
            saveditemImg.updateItemImg(oriImgName, imgName, imgUrl);
        }
    }
}
