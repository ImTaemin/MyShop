import {Button, Form, Modal} from "react-bootstrap";
import "../../scss/ItemRegister.scss"
import "../../scss/Debounce.scss";
import React, {useCallback, useState} from "react";
import {GenderType, ItemType} from "../common/Types.js";
import {Swiper, SwiperSlide} from "swiper/react";
import SwiperCore, {Navigation, Pagination} from 'swiper/core';
import 'swiper/css';
import 'swiper/css/navigation';
import 'swiper/css/pagination';
import client from "../../lib/api/client";
import {debounce} from "lodash";
import {FaCheck, FaTimesCircle} from "react-icons/fa";
import {registerItem} from "../../lib/api/item";

SwiperCore.use([Navigation, Pagination]);

const ItemRegisterModal = (props) => {
  const {show, onHide, handleSuccess, handleError} = props;
  const [isCheckCode, setIsCheckCode] = useState(false);

  const initForm = {
    code: "",
    name: "",
    price: 0,
    quantity: 0,
    itemType: "TOP",
    genderType: "UNISEX",
    imageList: [],
  }
  const [formData, setFormData] = useState(initForm);

  const {code, name, price, quantity, itemType, genderType, imageList} = formData;

  const unLoadModal = useCallback(() => {
    setFormData(initForm);

    setIsCheckCode(false);
    onHide(false);
  }, [onHide]);

  // 상품코드 확인
  const debounceCheckCode = useCallback(
    debounce(async (itemCode) => {
      try {
        const response = await client.get(`/item/exists/code/${itemCode}`);
        if (response.status === 200) {
          setIsCheckCode(true);
        } else {
          setIsCheckCode(false);
        }
      } catch (error) {
        setIsCheckCode(false);
      }
    }, 300), []);

  // 인풋 핸들러
  const inputHandler = useCallback((e) => {
    const {name, value} = e.target;

    setFormData((prevFormData) => ({
      ...prevFormData,
      [name]: value,
    }));

    if (name === "code") {
      debounceCheckCode(value);
    }
  }, [formData, debounceCheckCode]);

  // 이미지 추가
  const imageInsertHandler = (e) => {
    const file = e.target.files[0];
    setFormData((prev) => ({
      ...prev,
      imageList: [...prev.imageList, file],
    }));
  };

  // 이미지 삭제
  const imageDeleteHandler = (index) => {
    setFormData((prev) => {
      const newImageList = [...prev.imageList];
      newImageList.splice(index, 1);

      return {
        ...prev,
        imageList: newImageList
      }
    });
  };

  // 데이터 전송
  const submitHandler = async (e) => {
    e.preventDefault();

    if (!isCheckCode || code === '') {
      alert("상품 코드 중복을 확인하세요");
      return;
    }

    if (imageList.length === 0) {
      alert("최소 1개의 이미지를 등록해야 합니다.");
      return;
    }

    const reqFormData = new FormData();
    reqFormData.append("code", code.trim());
    reqFormData.append("name", name.trim());
    reqFormData.append("price", price.trim());
    reqFormData.append("quantity", quantity.trim());
    reqFormData.append("itemType", itemType);
    reqFormData.append("genderType", genderType);
    imageList.map((image) => {
      reqFormData.append("imageList", image);
    })

    const response = await registerItem(reqFormData);
    try {
      if (response.status === 200) {
        handleSuccess();
        unLoadModal();
        return;
      }
      handleError(response.data.msg);
      unLoadModal();
    } catch (error) {
      handleError(error.data.msg);
      unLoadModal();
    }
  };

  return (
    <Modal
      show={show}
      onHide={unLoadModal}
      size="lg"
      aria-labelledby="contained-modal-title-vcenter"
      centered
    >
      <Modal.Header closeButton>
        <Modal.Title id="contained-modal-title-vcenter">
          상품 등록
        </Modal.Title>
      </Modal.Header>
      <form encType="multipart/form-data" onSubmit={submitHandler}>

        <Modal.Body>
          <div className="input-content">
            <h4>이미지 선택</h4>

            <input
              // 같은 이미지 업로드 시 캐시된 파일 사용 x
              key={imageList.length}
              id="file-input"
              type="file"
              accept="image/*"
              style={{display: 'none'}}
              onChange={imageInsertHandler}
            />
            <div className="image-container">
              <Swiper
                allowTouchMove={false}
                navigation={true}
                pagination={{clickable: true}}
              >
                {imageList.length > 0 && (
                  imageList.map((image, index) => (
                    <SwiperSlide key={`slide-${index}`}>
                      <img src={URL.createObjectURL(image)} className="input-image" alt={`slide-${index}`}
                           onDoubleClick={() => imageDeleteHandler(index)}/>
                    </SwiperSlide>
                  ))
                )}
                <SwiperSlide key={imageList.length + 1}>
                  <label htmlFor="file-input" className="input-file">
                    <div className="diagonal-cross"/>
                  </label>
                </SwiperSlide>
              </Swiper>
            </div>


            <div className="input-container">
              <div className="input-wrap">
                <label>상품 코드</label>
                <div className="debounce-container">
                  <input
                    name="code"
                    type="text"
                    value={code}
                    className="form-control mt-1"
                    autoComplete='off'
                    onChange={inputHandler}
                    required
                  />
                  <div className="debounce-wrap">
                    {isCheckCode && (
                      <FaCheck/>
                    )}
                    {!isCheckCode && (
                      <FaTimesCircle/>
                    )}
                  </div>
                </div>
              </div>
              <div className="input-wrap">
                <label>상품명</label>
                <input
                  name="name"
                  type="text"
                  value={name}
                  className="form-control mt-1"
                  autoComplete='off'
                  onChange={inputHandler}
                  required
                />
              </div>
              <div className="input-wrap">
                <label>가격</label>
                <input
                  name="price"
                  type="text"
                  value={price}
                  pattern="^[0-9]+$"
                  className="form-control mt-1"
                  autoComplete='off'
                  onChange={inputHandler}
                  min="0"
                  required
                />
              </div>
              <div className="input-wrap">
                <label>재고</label>
                <input
                  name="quantity"
                  type="number"
                  value={quantity}
                  pattern="^[0-9]+$"
                  className="form-control mt-1"
                  autoComplete='off'
                  onChange={inputHandler}
                  min="1"
                  required
                />
              </div>
              <div className="input-wrap">
                <label>상품 타입</label>
                <Form.Select name="itemType" onChange={inputHandler}>
                  {ItemType.map((type, index) => {
                    return <option key={index}>{type}</option>
                  })}
                </Form.Select>
              </div>
              <div className="input-wrap">
                <label>상품 성별</label>
                <Form.Select name="genderType" onChange={inputHandler}>
                  {GenderType.map((type, index) => {
                    return <option key={index}>{type}</option>
                  })}
                </Form.Select>
              </div>
            </div>
          </div>
        </Modal.Body>
        <Modal.Footer>
          <Button type="submit">등록</Button>
        </Modal.Footer>
      </form>

    </Modal>
  );
}

export default ItemRegisterModal;