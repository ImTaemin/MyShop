import {Button, Form, Modal} from "react-bootstrap";
import {Swiper, SwiperSlide} from "swiper/react";
import {genderTypes, itemTypes} from "../common/Types";
import React, {useCallback, useEffect, useState} from "react";
import {updateItem} from "../../lib/api/item";

const ItemUpdateModal = (props) => {
  const {item, show, onHide, handleSuccess, handleError} = props;

  useEffect(() => {
    if (item) {
      setFormData({
        id: item.id,
        code: item.code,
        name: item.name,
        price: item.price,
        quantity: item.quantity,
        itemType: item.itemType,
        genderType: item.genderType,
        // 메인 이미지 맨 앞에 오게
        imageList: [item.mainImage, ...item.imageDetailList
          .map(image => image.path)]
      });
    }
  }, [item]);

  // 이 코드 없으면 오류발생
  const initForm = {
    id: "",
    code: "",
    name: "",
    price: "",
    quantity: "",
    itemType: "",
    genderType: "",
    imageList: [],
  }

  const [formData, setFormData] = useState(initForm);

  const {id, code, name, price, quantity, itemType, genderType, imageList} = formData;

  const unLoadModal = useCallback(() => {
    setFormData(initForm);

    onHide();
  }, []);

  // 인풋 핸들러
  const inputHandler = useCallback((e) => {
    const {name, value} = e.target;

    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
  }, [formData]);

  // 이미지 추가
  const imageInsertHandler = useCallback((e) => {
    const file = e.target.files[0];

    setFormData((prev) => ({
      ...prev,
      imageList: [...prev.imageList, file],
    }));
  }, [formData.imageList]);

  // 이미지 삭제
  const imageDeleteHandler = useCallback((index) => {
    setFormData((prev) => {
      const newImageList = [...prev.imageList];
      newImageList.splice(index, 1);

      return {
        ...prev,
        imageList: newImageList
      }
    });
  }, [formData.imageList]);

  // 이미지 URL 변환 함수
  const getImageFile = async (url, index) => {
    const lastIndex = url.lastIndexOf(".");
    if(lastIndex < 0) {
      return;
    }
    const extention = url.substring(lastIndex);

    const response = await fetch(url);
    const contentType = response.headers.get('Content-Type');
    const blob = await response.blob();
    const file = new File([blob], index + extention, {type: contentType});
    return file;
  };

  // 데이터 전송
  const submitHandler = async (e) => {
    e.preventDefault();

    if (imageList && imageList.length === 0) {
      alert("최소 1개의 이미지를 등록해야 합니다.");
      return;
    }

    const reqFormData = new FormData();

    reqFormData.append("requestItem", new Blob([JSON.stringify({
      "id": id,
      "code": code.trim(),
      "name": name.trim(),
      "price": price,
      "quantity": quantity,
      "itemType": itemType,
      "genderType": genderType
    })], {
      type: "application/json"
    }));

    for (let i = 0; i < imageList.length; i++) {
      const image = imageList[i];

      if ((typeof image) === "object") {
        // file
        reqFormData.append("imageList", image);
      } else if (typeof image === "string") {
        // image URL
        const imageLink = image.split("?", 1)[0]
          .replace("/download/storage/v1/b", "")
          .replace("/o/", "/");

        const file = await getImageFile(imageLink, i);
        reqFormData.append("imageList", file);
      }
    }

    const response = await updateItem(reqFormData);
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
          상품 수정
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
                      {(typeof image) === "object" && (
                        <img src={URL.createObjectURL(image)} className="input-image" alt={`slide-${index}`}
                             onDoubleClick={() => imageDeleteHandler(index)}/>
                      )}
                      {(typeof image) !== "object" && (
                        <img src={image} className="input-image" alt={`slide-${index}`}
                             onDoubleClick={() => imageDeleteHandler(index)}/>
                      )}
                    </SwiperSlide>
                  ))
                )}
                <SwiperSlide key={id}>
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
                    readOnly
                    required
                    style={{background: "#eeeeee"}}
                  />
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
                  {itemTypes.map((type, index) => {
                    return <option key={index}>{type}</option>
                  })}
                </Form.Select>
              </div>
              <div className="input-wrap">
                <label>상품 성별</label>
                <Form.Select name="genderType" onChange={inputHandler}>
                  {genderTypes.map((type, index) => {
                    return <option key={index}>{type}</option>
                  })}
                </Form.Select>
              </div>
            </div>
          </div>
        </Modal.Body>
        <Modal.Footer>
          <Button type="submit">수정</Button>
        </Modal.Footer>
      </form>

    </Modal>
  );
}

export default ItemUpdateModal;