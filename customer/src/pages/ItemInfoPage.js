import "../assets/scss/item-info.scss";
import 'swiper/css';
import 'swiper/css/navigation';
import 'swiper/css/pagination';
import {useParams} from "react-router";
import {useCallback, useEffect, useState} from "react";
import {Swiper, SwiperSlide} from "swiper/react";
import SwiperCore, {Navigation, Pagination} from 'swiper/core';
import {AiOutlineHeart, AiOutlineMinus, AiOutlinePlus, AiOutlineShopping} from "react-icons/ai";
import client from "../lib/api/client";

SwiperCore.use([Navigation, Pagination]);

const ItemInfoPage = () => {
  const { itemId } = useParams();
  const [item, setItem] = useState(null);
  const [quantity, setQuantity] = useState(1);
  const [price, setPrice] = useState(0);

  // 상세 조회
  useEffect(() => {
    client.get(`/item/${itemId}`)
      .then(response => {
        setItem(response.data);
        setPrice(response.data.price);
      });

  }, [itemId]);

  // 바로 주문
  const directOrder = useCallback((e) => {
    e.preventDefault();

    const reqFormData = new FormData();
    reqFormData.append("itemId", itemId);
    reqFormData.append("quantity", quantity);

    const response = client.post("/customer/order", reqFormData);

    console.log(response);

  }, []);

  const decrease = useCallback((e) => {
    e.preventDefault();
    if(quantity <= 1) return;

    setQuantity(quantity - 1);
    setPrice(item.price * (quantity - 1));
  }, [quantity, price]);

  const increase = useCallback((e) => {
    e.preventDefault();

    setQuantity(quantity + 1);
    setPrice(item.price * (quantity + 1));
  }, [quantity, price]);

  const quantityHandler = useCallback((e) => {
    const value = e.target.value;

    if(isNaN(value) || value <= 0) return;

    setQuantity(Number(value));
    setPrice(Number(item.price * value));
  }, [quantity, price]);

  return (
    <div className="item-info-container">
      {item && (
        <>
          <p className="item-type">{item.itemType}</p>
          <p className="item-title">{item.name}_{item.code}</p>

          {/* 상단 상세정보 영역*/}
          <div className="item-summary-wrap">
            <div className="item-image-box">
              <Swiper
                allowTouchMove={false}
                navigation={true}
                pagination={{clickable: true}}
              >
                <SwiperSlide>
                  <img src={item.mainImage} className="detail-image" alt=""/>
                </SwiperSlide>
                {item.imageDetailList.length > 0 &&
                  item.imageDetailList.map((image, index) => (
                    <SwiperSlide key={`slide-${index}`}>
                      <img src={image.path} className="detail-image" alt={`slide-${index}`} />
                    </SwiperSlide>
                  )
                )}
              </Swiper>
            </div>

            <div className="item-summary-box">
              <hr />

              <h5>Price Info <span className="product-info">가격정보</span></h5>
              <ul className="info">
                <li>
                  <p>마이샵 판매가</p>
                  <strong>{item.price.toLocaleString()} 원</strong>
                </li>
              </ul>

              <hr/>

              <h5>Product Info <span className="product-info">제품정보</span></h5>
              <ul className="info">
                <li>
                  <p>브랜드 <font color="#ccc">/</font> 품번</p>
                  <strong>{item.brandName} <font color="#ccc">/</font> {item.code}</strong>
                </li>
                <li>
                  <p>등록일 <font color="#ccc">/</font> 성별</p>
                  <strong>{item.createDate.split(" ")[0]} <font color="#ccc">/</font> {item.genderType}</strong>
                </li>
              </ul>

              <hr/>

              <h5>Delivery Info <span className="delivery-info">배송정보</span></h5>
              <ul className="info">
                <li>
                  <p>출고 정보</p>
                  <strong>결제 3일 이내 출고</strong>
                </li>
                <li>
                  <p>배송 정보</p>
                  <strong>국내 배송 / 입점사 배송 / 롯데택배</strong>
                </li>
              </ul>

              <hr/>

              <div className="quantity-box">
                <span className="item-name">{item.name}</span>
                <div className="quantity-control">
                  <button className="decrease" onClick={decrease}><AiOutlineMinus /></button>
                  <input className="quantity" onChange={quantityHandler} type="text" value={quantity} />
                  <button className="increase" onClick={increase}><AiOutlinePlus/></button>
                </div>
                <span className="price">{price.toLocaleString()}원</span>
              </div>

              <div className="order-box">
                <button className="direct-order" onClick={directOrder}>바로 구매</button>
                <button className="btn-like"><AiOutlineHeart /></button>
                <button className="btn-basket"><AiOutlineShopping /></button>
              </div>
            </div>
          </div>

          {/* 하단 내용 영역*/}
          <div className="item-detail-wrap">
            {item.imageDetailList.length > 0 &&
              item.imageDetailList.map((image, index) => (
                <img src={image.path} className="detail-image" key={index} alt=""/>
              )
            )}
          </div>

        </>
      )}
    </div>
  );
}

export default ItemInfoPage;